(function($) {
    var $checkboxes = $("#pv-permissions-table"),
        $submitButton = $("#submit-btn"),
        $modal = $(".modal").modal(),
        modalDataTarget = "modal-remove-pv-permissions";

    var checkedPermissions = function() {
        return $checkboxes.find("input:checked");
    };

    var submitButtonText = function(count) {
        var text = "remover " + (count || '') + " permissões";
        return (count === 1) ? text.replace("ões", "ão") : text;
    };

    var updateSubmitButton = function() {
        var count = checkedPermissions().length;

        $submitButton.text(submitButtonText(count));
        if (count === 0) {
            $submitButton
                .attr("data-target", "")
                .addClass("disabled");
        } else {
            $submitButton
                .attr("data-target", modalDataTarget)
                .removeClass("disabled");
        }
    };

    $modal.find(".btn.cancel").on("click", function() {
        checkedPermissions().prop("checked", false);
        updateSubmitButton();
    });

    $checkboxes.on("change", updateSubmitButton);

})(jQuery);