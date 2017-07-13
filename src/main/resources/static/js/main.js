/**
 * Created by aniov on 7/13/2017.
 */

var TOKEN_KEY = "jwtToken";

function login() {

    event.preventDefault();
    var loginData = {
        username: document.getElementById("username").value,
        plainPassword: document.getElementById("password").value,
    };
    //clearLoginForm();

    $.ajax({
        url: "/login",
        type: "POST",
        data: JSON.stringify(loginData),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            localStorage.setItem(TOKEN_KEY, data.token);

            console.log("SUCCES " + TOKEN_KEY);

        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status === 401 || jqXHR.status === 400) {
                $("#loginError")
                    .show().fadeTo(2000, 500).slideUp(500, function () {
                    $("#loginError").slideUp(500);
                });
            } else {
                throw new Error("an unexpected error occured: " + errorThrown);
            }
        }
    });
}