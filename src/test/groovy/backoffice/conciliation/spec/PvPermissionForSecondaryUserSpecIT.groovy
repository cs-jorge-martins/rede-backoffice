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

    def createPrimaryUser(primaryUserEmail) {
        def primaryUser = new User()
        primaryUser.setEmail(primaryUserEmail)
        userRepository.save(primaryUser)
    }

    def createSecondaryUserFor(primaryUser, secondaryUserEmail) {
        def secondaryUser = new User()
        secondaryUser.setEmail(secondaryUserEmail)
        secondaryUser.setPrimaryUser(primaryUser)
        userRepository.save(secondaryUser)
    }

    def createPv(code, acquirer) {
        pvRepository.save(new Pv(code, acquirer))
    }

    def createBranchPvFor(headquarterPv, code) {
        def branchPv = new Pv(code, headquarterPv.acquirer)
        branchPv.setHeadquarter(headquarterPv)
        pvRepository.save(branchPv)
    }

    def getFilePath(filename) {
        new File(Class.getResource(filename).toURI()).absolutePath
    }

    def "Dar permissão de PV a um Usuário secundário"() {
        setup: "o pv matriz existe no banco"
        def pv = createPv("11111111", Acquirer.CIELO)

        and: "o usuario requisitante é primario"
        def primaryUser = createPrimaryUser("usuario_primario_1@email.com")

        and: "o usuario requisitante tem permissão no pv matriz"
        pvPermissionRepository.save(new PvPermission(primaryUser, pv))

        and: "o usuario para dar permissão é secundario do requisitante"
        def secondaryUser = createSecondaryUserFor(primaryUser, "usuario_secundario_1@email.com")

        and: "na pagina de permissão de pvs para usuario secundario"
        to PvPermissionSecondaryPage

        when: "informo dados do fomulario"
        form.with {
            primaryEmail = primaryUser.email
            secondaryEmail = secondaryUser.email
            acquirer = "CIELO"
            file = getFilePath("/functional-testing-pvs-1")
        }

        then:
        submitButton.click()

        expect: "mensagem 'Operação realizada com sucesso!' deve aparecer"
        at PvPermissionSecondaryPage
        assert(messages.text().contains("Operação realizada com sucesso!"))
    }

    def "Dar permissão de PV a um Usuário secundário inexistente"() {
        setup: "o pv matriz existe no banco"
        def secondaryUserEmail = "usuario_secundario_2@email.com"
        def pv = createPv("22222222", Acquirer.CIELO)

        and: "o usuario requisitante é primario"
        def primaryUser = createPrimaryUser("usuario_primario_2@email.com")

        and: "o usuario requisitante tem permissão no pv matriz"
        pvPermissionRepository.save(new PvPermission(primaryUser, pv))

        and: "na pagina de permissão de pvs para usuario secundario"
        to PvPermissionSecondaryPage

        when: "informo dados do fomulario"
        form.with {
            primaryEmail = primaryUser.email
            secondaryEmail = secondaryUserEmail
            acquirer = "CIELO"
            file = getFilePath("/functional-testing-pvs-2")
        }

        then:
        submitButton.click()

        expect: "mensagem 'Operação realizada com sucesso!' deve aparecer"
        at PvPermissionSecondaryPage
        assert(messages.text().contains("Operação realizada com sucesso!"))
    }

    def "Dar permissão para um PV filial existente a um Usuário secundário"() {
        setup: "o pv filial existe no banco"
        def secondaryUserEmail = "usuario_secundario_3@email.com"
        def headquarterPv = createPv("33333333", Acquirer.CIELO)
        createBranchPvFor(headquarterPv, "33333334")

        and: "o usuario requisitante é primario"
        def primaryUser = createPrimaryUser("usuario_primario_3@email.com")

        and: "o usuario requisitante tem permissão no pv matriz"
        pvPermissionRepository.save(new PvPermission(primaryUser, headquarterPv))

        and: "o usuario para dar permissão é secundario do requisitante"
        createSecondaryUserFor(primaryUser, secondaryUserEmail)

        and: "na pagina de permissão de pvs para usuario secundario"
        to PvPermissionSecondaryPage

        when: "informo dados do fomulario"
        form.with {
            primaryEmail = primaryUser.email
            secondaryEmail = secondaryUserEmail
            acquirer = "CIELO"
            file = getFilePath("/functional-testing-pvs-3")
        }

        then:
        submitButton.click()

        expect: "mensagem 'Operação realizada com sucesso!' deve aparecer"
        at PvPermissionSecondaryPage
        assert(messages.text().contains("Operação realizada com sucesso!"))
    }

    def "Usuario requisitante é secundario"() {
        setup: "o usuario requisitante existe no banco como secundario"
        def primaryUser = createPrimaryUser("usuario_primario_4@email.com")
        def secondaryUser = createSecondaryUserFor(primaryUser, "usuario_secundario_4@email.com")

        and: "na pagina de permissão de pvs para usuario secundario"
        to PvPermissionSecondaryPage

        when: "informo dados do fomulario"
        form.with {
            primaryEmail = secondaryUser.email
            secondaryEmail = "usuario_novo@email.com"
            acquirer = "CIELO"
            file = getFilePath("/functional-testing-pvs-3")
        }

        then:
        submitButton.click()

        expect: "mensagem 'O usuario 'usuario_secundario_4@email.com' é um usuario secundario ao invés de primario.' deve aparecer"
        at PvPermissionSecondaryPage
        assert(messages.text().contains("O usuario 'usuario_secundario_4@email.com' é um usuario secundario ao invés de primario."))
    }

    def "Usuario requisitante inexistente"() {
        setup: "o usuario requisitante é inexistente no banco"
        def primaryUserEmail = "usuario_novo_5@email.com"

        and: "na pagina de permissão de pvs para usuario secundario"
        to PvPermissionSecondaryPage

        when: "informo dados do fomulario"
        form.with {
            primaryEmail = primaryUserEmail
            secondaryEmail = "usuario_novo@email.com"
            acquirer = "CIELO"
            file = getFilePath("/functional-testing-pvs-3")
        }

        then:
        submitButton.click()

        expect: "mensagem 'Usuário com email 'usuario_novo_5@email.com' não encontrado' deve aparecer"
        at PvPermissionSecondaryPage
        assert(messages.text().contains("Usuário com email 'usuario_novo_5@email.com' não encontrado"))
    }

    def "Usuario requisitante é primario e usuario a ser permitido é primario"() {
        setup: "Dado que o usuario requisitante e o usuario a ser permitido são primarios"
        def primaryUserA = createPrimaryUser("usuario_primario_a@email.com")
        def primaryUserB = createPrimaryUser("usuario_primario_b@email.com")

        and: "na pagina de permissão de pvs para usuario secundario"
        to PvPermissionSecondaryPage

        when: "informo dados do fomulario"
        form.with {
            primaryEmail = primaryUserA.email
            secondaryEmail = primaryUserB.email
            acquirer = "CIELO"
            file = getFilePath("/functional-testing-pvs-3")
        }

        then:
        submitButton.click()

        expect: "mensagem 'Usuario 'usuario_primario_b@email.com' é um usuario primario.' deve aparecer"
        at PvPermissionSecondaryPage
        assert(messages.text().contains("Usuario 'usuario_primario_b@email.com' é um usuario primario."))
    }

    def "Usuario a ser permitido é secundario de outro usuario primario"() {
        setup: "Dado que o usuarios a1 e b1 são primarios"
        def primaryUserA1 = createPrimaryUser("usuario_primario_a1@email.com")
        def primaryUserB1 = createPrimaryUser("usuario_primario_b1@email.com")

        and: "o usuario b1a1 é secundario do usuario b1"
        def secondaryUserB1A1 = createSecondaryUserFor(primaryUserB1, "usuario_secundario_b1a1@email.com")

        and: "na pagina de permissão de pvs para usuario secundario"
        to PvPermissionSecondaryPage

        when: "informo dados do fomulario"
        form.with {
            primaryEmail = primaryUserA1.email
            secondaryEmail = secondaryUserB1A1.email
            acquirer = "CIELO"
            file = getFilePath("/functional-testing-pvs-3")
        }

        then:
        submitButton.click()

        expect: "mensagem 'Usuario 'usuario_primario_a1@email.com' não é secundario do usuario 'usuario_secundario_b1a1@email.com'' deve aparecer"
        at PvPermissionSecondaryPage
        assert(messages.text().contains("Usuario 'usuario_primario_a1@email.com' não é secundario do usuario 'usuario_secundario_b1a1@email.com'"))
    }

    def "Pv com formato invalido"() {
        setup: "Dado que que existe um usuario primario"
        def primaryUser = createPrimaryUser("usuario_primario_7@email.com")
        def secondaryUserEmail = "usuario_secundario_7@email.com"

        and: "na pagina de permissão de pvs para usuario secundario"
        to PvPermissionSecondaryPage

        when: "informo dados do fomulario"
        form.with {
            primaryEmail = primaryUser.email
            secondaryEmail = secondaryUserEmail
            acquirer = "CIELO"
            file = getFilePath("/functional-testing-pvs-invalid-format")
        }

        then:
        submitButton.click()

        expect: "mensagem 'Pv '***invalid_pv_code***' com formato invalido' deve aparecer"
        at PvPermissionSecondaryPage
        assert(messages.text().contains("Pv '***invalid_pv_code***' com formato invalido"))
    }

    def "Dar permissão de PV quando o usuario primario não tem permissão"() {
        setup: "o pv matriz existe no banco"
        def secondaryUserEmail = "usuario_secundario_8@email.com"
        createPv("88888888", Acquirer.CIELO)

        and: "o usuario requisitante é primario"
        def primaryUser = createPrimaryUser("usuario_primario_8@email.com")

        and: "na pagina de permissão de pvs para usuario secundario"
        to PvPermissionSecondaryPage

        when: "informo os dados do formulario"
        form.with {
            primaryEmail = primaryUser.email
            secondaryEmail = secondaryUserEmail
            acquirer = "CIELO"
            file = getFilePath("/functional-testing-pvs-4")
        }

        then:
        submitButton.click()

        expect: "mensagem 'Usuário 'usuario_primario_8@email.com' não tem acesso ao Pv '88888888'' deve aparecer"
        at PvPermissionSecondaryPage
        assert(messages.text().contains("Usuário 'usuario_primario_8@email.com' não tem acesso ao Pv '88888888'"))
    }

    def "Pv não existente"() {
        setup: "o usuario requisitante é primario"
        def secondaryUserEmail = "usuario_secundario_9@email.com"
        def primaryUser = createPrimaryUser("usuario_primario_9@email.com")

        and: "na pagina de permissão de pvs para usuario secundario"
        to PvPermissionSecondaryPage

        when: "informo os dados do formulario"
        form.with {
            primaryEmail = primaryUser.email
            secondaryEmail = secondaryUserEmail
            acquirer = "CIELO"
            file = getFilePath("/functional-testing-pvs-5")
        }

        then:
        submitButton.click()

        expect: "mensagem 'Pv '99999999' não existe' deve aparecer"
        at PvPermissionSecondaryPage
        assert(messages.text().contains("Pv '99999999' não existe"))
    }
}
