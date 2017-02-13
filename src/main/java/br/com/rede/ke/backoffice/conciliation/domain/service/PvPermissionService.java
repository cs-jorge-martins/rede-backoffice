/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvPermissionService.java
 * Descrição: PvPermissionService.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.rede.ke.backoffice.conciliation.domain.PrimaryUserPvPermissionRequest;
import br.com.rede.ke.backoffice.conciliation.domain.SecondaryUserPvPermissionRequest;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.exception.HeadquarterPermittedToMoreThanOnePrimayUserException;
import br.com.rede.ke.backoffice.conciliation.domain.exception.UserNotFoundException;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionRepository;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import br.com.rede.ke.backoffice.util.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionSpecifications.pvAcquirerEqualTo;
import static br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionSpecifications.pvCodeContains;
import static br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionSpecifications.userEmailContains;
import static org.springframework.data.jpa.domain.Specifications.not;
import static org.springframework.data.jpa.domain.Specifications.where;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * The Class PvPermissionService.
 */
@Service
public class PvPermissionService {

    private PvRepository pvRepository;
    private PvService pvService;
    private PvPermissionRepository pvPermissionRepository;
    private UserService userService;

    /**
     * Instantiates a new pv permission service.
     *
     * @param repository
     *            the repository
     * @param pvService
     *            the pv service
     * @param pvRepository
     *            the pv repository
     * @param userService
     *            the user service
     */
    public PvPermissionService(PvPermissionRepository repository, PvService pvService, PvRepository pvRepository,
        UserService userService) {
        this.pvPermissionRepository = repository;
        this.pvService = pvService;
        this.pvRepository = pvRepository;
        this.userService = userService;
    }

    /**
     * Find all by acquirer and code and email.
     *
     * @param acquirer
     *            the acquirer
     * @param code
     *            the code
     * @param email
     *            the email
     * @param pageable
     *            the pageable
     * @return the page
     */
    public Page<PvPermission> findAllByAcquirerAndCodeAndEmail(Acquirer acquirer, String code, String email,
        Pageable pageable) {
        Specifications<PvPermission> spec = where(not(pvAcquirerEqualTo(Acquirer.REDE)));

        if (!isEmpty(code)) {
            spec = spec.and(pvCodeContains(code));
        }

        if (!Acquirer.NULL.equals(acquirer)) {
            spec = spec.and(pvAcquirerEqualTo(acquirer));
        }

        if (!isEmpty(email)) {
            spec = spec.and(userEmailContains(email));
        }

        return pvPermissionRepository.findAll(spec, pageable);
    }

    /**
     * Create permission for primary user.
     *
     * @param pvPermissionRequest
     *            pv permission request.
     * @return List of processing results.
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Result<PvPermission, String>> createForPrimaryUser(
        final PrimaryUserPvPermissionRequest pvPermissionRequest) {
        return pvPermissionRequest.getPvs().stream()
            .map(pv -> createForPrimaryUser(pvPermissionRequest.getRequesterUserEmail(), pv))
            .collect(Collectors.toList());
    }

    /**
     * Create permission for primary user.
     *
     * @param requesterUserEmail requester user email.
     * @param pv pv.
     * @return A processing result.
     */
    private Result<PvPermission, String> createForPrimaryUser(String requesterUserEmail, Pv pv) {
        final User primaryUser = userService.getOrCreatePrimaryUser(requesterUserEmail);

        if (!pvService.isValidPv(pv)) {
            return Result.failure(String.format("O pv '%s' está no formato inválido (entre 1 e 10 caracteres, somente números)", pv.getCode()));
        }

        Optional<Pv> pvOpt = pvRepository.findByCodeAndAcquirerId(pv.getCode(), pv.getAcquirerId());
        if (pvOpt.isPresent() && !pvOpt.get().isHeadquarter()) {
            return Result.failure(String.format("O pv '%s' já está cadastrado como um pv filial", pv.getCode()));
        }

        final Pv headquarter = pvOpt.orElseGet(() -> pvRepository.save(pv));

        Optional<User> userFromPermission = getPrimaryUserPermittedToHeadquarter(headquarter);
        if (userFromPermission.isPresent() && !userFromPermission.get().equals(primaryUser)) {
            return Result.failure(
                String.format("Já existe uma permissão para o pv: '%s' para outro usuário primário.",
                pv.getCode()));
        }

        return Result.success(getOrCreatePvPermission(primaryUser, headquarter));
    }

    private Optional<User> getPrimaryUserPermittedToHeadquarter(Pv headquarter) {
        List<User> usersPermittedToHeadquarter = pvPermissionRepository.findAllByPv(headquarter).stream()
            .map(PvPermission::getUser)
            .filter(User::isPrimary)
            .collect(Collectors.toList());

        if (usersPermittedToHeadquarter.size() > 1) {
            throw new HeadquarterPermittedToMoreThanOnePrimayUserException(headquarter, usersPermittedToHeadquarter);
        }

        return usersPermittedToHeadquarter.stream().findFirst();
    }

    private PvPermission getOrCreatePvPermission(User primaryUser, Pv headquarter) {
        return pvPermissionRepository.findByUserAndPv(primaryUser, headquarter).orElseGet(() -> {
            PvPermission pvPermission = new PvPermission(primaryUser, headquarter);
            return pvPermissionRepository.save(pvPermission);
        });
    }

    /**
     * Creates for secondary User.
     *
     * @param pvPermissionRequest
     *            pv permission request
     * @return the list of pv permission result
     */
    public List<Result<PvPermission, String>> createForSecondaryUser(
        final SecondaryUserPvPermissionRequest pvPermissionRequest) {
        return pvPermissionRequest.getPvs().stream()
            .map((pv) -> createForSecondaryUser(pvPermissionRequest.getRequesterUserEmail(),
                                                pvPermissionRequest.getToBePermittedUserEmail(),
                                                pv))
            .collect(Collectors.toList());
    }

    /**
     * Creates the for secondary user.
     *
     * @param requesterUserEmail requester user email.
     * @param permittedUserEmail permitted user email.
     * @param pv pv.
     * @return the result.
     */
    private Result<PvPermission, String> createForSecondaryUser(String requesterUserEmail,
        String permittedUserEmail,
        Pv pv) {

        User primaryUser = userService.getPrimaryUser(requesterUserEmail)
            .orElseThrow(getUserNotFoundException(requesterUserEmail));
        User secondaryUser = userService.getOrCreateSecondaryUserFor(primaryUser, permittedUserEmail);

        if (!pvService.isValidPv(pv)) {
            return Result.failure(String.format("O pv '%s' está no formato inválido (entre 1 e 10 caracteres, somente números)", pv.getCode()));
        }

        Optional<Pv> pvOpt = pvRepository.findByCodeAndAcquirerId(pv.getCode(), pv.getAcquirerId());

        if (!pvOpt.isPresent()) {
            return Result.failure(String.format("O pv '%s' não existe", pv.getCode()));
        }

        Pv existingPv = pvOpt.get();

        if (!userService.hasAccess(primaryUser, existingPv)) {
            return Result.failure(String.format("Usuário '%s' não tem acesso ao pv '%s'",
                primaryUser.getEmail(), existingPv.getCode()));
        }

        PvPermission pvPermission = new PvPermission(secondaryUser, existingPv);

        pvPermissionRepository.save(pvPermission);
        return Result.success(pvPermission);
    }

    /**
     * Gets user not found exception.
     *
     * @param email
     *            the user email
     * @return the UserNotFoundException supplier
     */
    private Supplier<UserNotFoundException> getUserNotFoundException(String email) {
        return () -> new UserNotFoundException(String.format("Usuário com email '%s' não encontrado", email));
    }

    /**
     *  Delete pv permission.
     *
     * @param pvPermission to be deleted
     */
    public void delete(PvPermission pvPermission) {
        if (pvPermission.getUser().isPrimary()) {
            Pv headquarterPv = pvPermission.getPv();
            pvPermissionRepository.delete(getAllRelatedPvPermissions(headquarterPv));
        } else {
            pvPermissionRepository.delete(pvPermission);
        }

    }

    /**
     * Gets all related pv permissions.
     *
     * @param headquarterPv the headquarter pv
     * @return the pv permission list
     */
    private List<PvPermission> getAllRelatedPvPermissions(Pv headquarterPv) {
        List<Pv> branches = headquarterPv.getBranches();

        Stream<PvPermission> headquarterPvPermissions = pvPermissionRepository.findAllByPv(headquarterPv).stream();
        Stream<PvPermission> branchesPvPermissions = pvPermissionRepository.findAllByPvIn(branches).stream();

        return Stream.concat(headquarterPvPermissions, branchesPvPermissions)
            .collect(Collectors.toList());
    }
}
