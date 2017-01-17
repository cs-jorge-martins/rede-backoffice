package backoffice.conciliation.page

import geb.Page

class LoginPage extends Page {
    static at = { title == "Control Rede Backoffice | Acesso" }
    static content = {
        errorMessage { $(".error-message").text() }
        loginForm { $("form") }
        loginButton(to: [HomePage, LoginPage]) { $("button") }
    }
}
