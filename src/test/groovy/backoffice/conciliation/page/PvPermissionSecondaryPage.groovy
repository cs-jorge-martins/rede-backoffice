/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvPermissionSecondaryPage.groovy
 * Descrição: PvPermissionSecondaryPage.groovy.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 17/01/2017
 * Empresa  : ThoughtWorks
 */
package backoffice.conciliation.page

import geb.Page

/**
 * The class PvPermissionSecondaryPage.
 */
class PvPermissionSecondaryPage extends Page {
    static url = "/pv-permissions/secondary"
    static at = { title == "control rede backoffice | permissão de pvs para usuários secundários" }
    static content = {
        messages { $(".messages") }
        form { $("#pv-permission-secondary-form") }
        submitButton { $("#pv-permission-secondary-form button[type='submit']") }
    }
}
