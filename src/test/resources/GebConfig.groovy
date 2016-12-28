import org.openqa.selenium.htmlunit.HtmlUnitDriver

baseUrl = "http://localhost:8181"

driver = {
    return new HtmlUnitDriver()
}
environments {
    htmlunit {
        new HtmlUnitDriver()
    }
}