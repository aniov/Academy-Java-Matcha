/**
 * Created by aniov on 7/13/2017.
 */

var TOKEN_KEY = "jwtToken";

//On window load we check if we have a token, and assume that is valid so we are logged
document.addEventListener('DOMContentLoaded', function () {
  //  if (localStorage.getItem(TOKEN_KEY) == null) {
        $("#register").hide();
        $("#login").show();
 //   }
}, false);

function login() {

    event.preventDefault();
    var loginData = {
        username: document.getElementById("username").value,
        plainPassword: document.getElementById("password").value
    };
    clearLoginForm();

    $.ajax({
        url: "/login",
        type: "POST",
        data: JSON.stringify(loginData),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            localStorage.setItem(TOKEN_KEY, data.token);

            console.log("SUCCESS " + TOKEN_KEY);

        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status === 401 || jqXHR.status === 400) {
                $("#loginError")
                    .show().fadeTo(2000, 500).slideUp(500, function () {
                    $("#loginError").slideUp(500);
                });
            } else {
                throw new Error("an unexpected error occurred: " + errorThrown);
            }
        }
    });
}

function register() {

    event.preventDefault();
    var loginData = {
        username: document.getElementById("registerUsername").value,
        email: document.getElementById("registerEmail").value,
        plainPassword: document.getElementById("registerPassword").value,
        repeatPlainPassword: document.getElementById("registerRepeatPassword").value
    };
    clearRegisterForm();

    $.ajax({
        url: "/register",
        type: "POST",
        data: JSON.stringify(loginData),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            console.log("SUCCESS " );
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status === 401 || jqXHR.status === 400) {

            } else {
                throw new Error("an unexpected error occurred: " + errorThrown);
            }
        }
    });
}

function goToRegister() {
    $("#login").hide();
    $("#register").show();
}

function goToLogin() {
    $("#register").hide();
    $("#login").show();
}

function clearLoginForm() {
    document.getElementById("username").value = '';
    document.getElementById("password").value = '';
}

function clearRegisterForm() {
    document.getElementById("registerUsername").value = '';
    document.getElementById("registerEmail").value = '';
    document.getElementById("registerPassword").value = '';
    document.getElementById("registerRepeatPassword").value = '';
}

function resetPassword() {

}