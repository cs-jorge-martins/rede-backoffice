package br.com.rede.ke.backoffice.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvBatch;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.exception.DomainException;
import br.com.rede.ke.backoffice.conciliation.domain.factory.PvFactory;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvPermissionService;
import br.com.rede.ke.backoffice.conciliation.domain.service.UserService;

@Controller
public class PvPermissionController {
    
    private PvPermissionService pvPermissionService;
    private UserService userService;
    
    /**
     * Constructor.
     * @param pvPermissionService pvPermissionService.
     * @param userService userService.
     */
    public PvPermissionController(PvPermissionService pvPermissionService, UserService userService) {
        this.pvPermissionService = pvPermissionService;
        this.userService = userService;
    }
 
    /**
     * Get pv permissions.
     * @param model page model.
     * @return mapping.
     */
    @GetMapping({"/", "/pv-permissions"})
    public String index(Model model,
            @PageableDefault(size = 20, sort = {"user.email", "pv.acquirerId", "pv.code"},
            direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String code,
            @RequestParam(required = false, defaultValue = "NULL") Acquirer acquirer,
            @RequestParam(required = false) String email) {

            model.addAttribute("code", code);
            model.addAttribute("acquirer", acquirer);
            model.addAttribute("email", email);
            model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
            Page<PvPermission> pvPermissions = pvPermissionService.findAllByAcquirerAndCodeAndEmail(acquirer, code, email,
                pageable);
            model.addAttribute("pvPermissions", pvPermissions);
        return "pv-permissions/index";
    }
    
    @GetMapping("/pv-permissions/primary")
    public String primary(Model model){
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        return "pv-permissions/primary";
    }
    
    /**
     * Post pv permissions.
     * @param model page model.
     * @param file uploaded file.
     * @param acquirer acquirer.
     * @param email user email.
     * @return mapping.
     */
    @PostMapping("/pv-permissions/primary")
    public String create(Model model,
                         @RequestParam MultipartFile file,
                         @RequestParam Acquirer acquirer,
                         @RequestParam String email) {

        try {
            User user = userService.getOrCreatePrimaryUser(email);
            PvBatch pvBatch = pvPermissionService.giveUserPermissionForHeadquarter(
                PvFactory.fromFileAndAcquirer(file, acquirer), user);

            model.addAttribute("userMessage", buildUserMessage(pvBatch));
            model.addAttribute("invalidPvs", pvBatch.getInvalidPvs());
        } catch (DomainException e) {
            model.addAttribute("userMessage", buildUserMessage(e));
        } catch (IOException e) {
            model.addAttribute("userMessage", buildInvalidFileMessage());
        }
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        return "pv-permissions/primary";
    }
    
    @GetMapping("/pv-permissions/secondary")
    public String secondary(Model model) {
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        return "pv-permissions/secondary";
    }
    
    private String buildUserMessage(Exception exception) {
        return String.format("Um erro ocorreu: %s", exception.getMessage());
    }

    private String buildInvalidFileMessage() {
        return "Erro ao processar arquivo";
    }

    private String buildUserMessage(PvBatch pvBatch) {
        String userMessage = "Operação ocorreu com sucesso";

        if (hasInvalidPvs(pvBatch)) {
            userMessage = "Com exceção dos PVs abaixo, a operação ocorreu com sucesso:";
        }
        return userMessage;
    }

    private boolean hasInvalidPvs(PvBatch pvBatch) {
        return pvBatch != null && !pvBatch.getInvalidPvs().isEmpty();
    }
}
