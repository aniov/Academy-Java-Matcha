function getCurrentUser() {

    var userData = {
        email: document.getElementById("email").value,
    };
    clearResetPasswordForm();

    $.ajax({
        url: "/user/me",
        type: "GET",
        data: JSON.stringify(emailData),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            $("#success-message").html(data.messages)
                .show().fadeTo(5000, 500).slideUp(500, function () {
                $("#success-message").slideUp(500);
            });
        },
        error: function (data, textStatus, jqXHR) {

            $("#reset-message").html(parseListMessages(data.responseJSON.messages))
                .show().fadeTo(3000, 500).slideUp(500, function () {
                $("#reset-message").slideUp(500);
            });
        }
    });
}