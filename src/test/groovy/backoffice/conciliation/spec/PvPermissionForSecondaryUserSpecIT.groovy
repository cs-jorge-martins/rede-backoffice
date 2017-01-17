/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvPermissionForSecondaryUserSpecIT.groovy
 * Descrição: PvPermissionForSecondaryUserSpecIT.groovy.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 17/01/2017
 * Empresa  : ThoughtWorks
 */
package backoffice.conciliation.spec

import backoffice.conciliation.page.LoginPage
import backoffice.conciliation.page.PvPermissionSecondaryPage
import br.com.rede.ke.backoffice.Application
import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermissionId
import br.com.rede.ke.backoffice.conciliation.domain.entity.User
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionRepository
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository
import br.com.rede.ke.backoffice.conciliation.domain.repository.UserRepository
import geb.spock.GebSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional


@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = Application,
        properties = "server.port=8181")
class PvPermissionForSecondaryUserSpecIT extends GebSpec {

    @Autowired
    private PvRepository pvRepository

    @Autowired
    private UserRepository userRepository

    @Autowired
    private PvPermissionRepository pvPermissionRepository

    def setup() {
        to LoginPage

        loginForm.with {
            username = "root"
            password = "pass"
        }

        loginButton.click()
    }

    def "Dar permissão de PV a um Usuário secundário"() {
        setup: "o pv matriz existe no banco"
        def primaryUserEmail = "usuario_primario_1@email.com"
        def secondaryUserEmail = "usuario_secundario_1@email.com"
        def p = new Pv("11111111", Acquirer.CIELO)
        def pv = pvRepository.save(p)

        and: "o usuario requisitante é primario"
        def u = new User()
        u.setEmail(primaryUserEmail)
        def primaryUser = userRepository.save(u)

        and: "o usuario requisitante tem permissão no pv matriz"
        def id = new PvPermissionId(primaryUser.getId(), pv.getId())
        def pvPermission = pvPermissionRepository.save(new PvPermission(id, primaryUser, pv))

        and: "o usuario para dar permissão é secundario do requisitante"
        u = new User()
        u.setEmail(secondaryUserEmail)
        u.setPrimaryUser(primaryUser)
        def secondaryUser = userRepository.save(u)

        and: "na pagina de permissão de pvs para usuario secundario"
        to PvPermissionSecondaryPage

        when: "informo o usuario primario"
        form.with { primaryEmail = primaryUserEmail}

        and: "informo o usuario secundario"
        form.with { secondaryEmail = secondaryUserEmail }

        and: "informo o adquirente"
        form.with { acquirer = "CIELO" }

        and: "escolho um arquivo que tem o PV matriz"
        form.with { file = new File(Class.getResource("/functional-testing-pvs-1").toURI()).absolutePath }

        then:
        submitButton.click()

        expect: "mensagem 'Operação realizada com sucesso!' deve aparecer"
        at PvPermissionSecondaryPage
        assert(messages.text().contains("Operação realizada com sucesso!"))
    }

    def "Dar permissão de PV a um Usuário secundário inexistente"() {
        setup: "o pv matriz existe no banco"
        def primaryUserEmail = "usuario_primario_2@email.com"
        def secondaryUserEmail = "usuario_secundario_2@email.com"
        def p = new Pv("22222222", Acquirer.CIELO)
        def pv = pvRepository.save(p)

        and: "o usuario requisitante é primario"
        def u = new User()
        u.setEmail(primaryUserEmail)
        def primaryUser = userRepository.save(u)

        and: "o usuario requisitante tem permissão no pv matriz"
        def id = new PvPermissionId(primaryUser.getId(), pv.getId())
        def pvPermission = pvPermissionRepository.save(new PvPermission(id, primaryUser, pv))

        and: "na pagina de permissão de pvs para usuario secundario"
        to PvPermissionSecondaryPage

        when: "informo o usuario primario"
        form.with { primaryEmail = primaryUserEmail }

        and: "informo o usuario secundario"
        form.with { secondaryEmail = secondaryUserEmail }

        and: "informo o adquirente"
        form.with { acquirer = "CIELO" }

        and: "escolho um arquivo que tem o PV matriz"
        form.with { file = new File(Class.getResource("/functional-testing-pvs-2").toURI()).absolutePath }

        then:
        submitButton.click()

        expect: "mensagem 'Operação realizada com sucesso!' deve aparecer"
        at PvPermissionSecondaryPage
        assert(messages.text().contains("Operação realizada com sucesso!"))
    }
}
