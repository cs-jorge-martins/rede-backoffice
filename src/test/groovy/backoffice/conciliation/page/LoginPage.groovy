package backoffice.conciliation.page

import geb.Page

class LoginPage extends Page {
    static at = { title == "control rede backoffice | acesso" }
    static content = {
        errorMessage { $(".error-message").text() }
        loginForm { $("#login-form") }
        loginButton(to: [HomePage, LoginPage]) { $("#login-form button") }
    }
}
