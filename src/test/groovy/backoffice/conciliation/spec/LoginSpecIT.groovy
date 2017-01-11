package backoffice.conciliation.spec

import backoffice.conciliation.page.HomePage
import backoffice.conciliation.page.LoginPage
import br.com.rede.ke.backoffice.Application
import geb.spock.GebSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
                classes = Application,
                properties = "server.port=8181")
class LoginSpecIT extends GebSpec {

    def "do login with invalid credentials"() {
        given:
        to LoginPage

        when:
        loginForm.with {
            username = "invalid_username"
            password = "invalid_password"
        }
        and:
        loginButton.click()

        then:
        assert at(LoginPage)
        and:
        assert errorMessage == "nome de usuário ou senha inválidos"
    }

    def "do login with valid credentials"() {
        given:
        to LoginPage

        when:
        loginForm.with {
            username = "root"
            password = "pass"
        }
        and:
        loginButton.click()

        then:
        assert at(HomePage)
    }
}
