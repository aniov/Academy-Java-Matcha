/**
 * Created by aniov on 7/13/2017.
 */

var TOKEN_KEY = "jwtToken";

//On window load we check if we have a token, and assume that is valid so we are logged
/*document.addEventListener('DOMContentLoaded', function () {
  //  if (localStorage.getItem(TOKEN_KEY) == null) {
        $("#register").hide();
        $("#login").show();
 //   }
}, false);*/

function login() {

    event.preventDefault();
    var loginData = {
        username: document.getElementById("username").value,
        plainPassword: document.getElementById("password").value,
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

            console.log("LOGIN SUCCESS");
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
    var registerData = {
        username: document.getElementById("registerUsername").value,
        email: document.getElementById("registerEmail").value,
        plainPassword: document.getElementById("registerPassword").value,
        repeatPlainPassword: document.getElementById("registerRepeatPassword").value
    };
    clearRegisterForm();

    $.ajax({
        url: "/register",
        type: "POST",
        data: JSON.stringify(registerData),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            console.log("SUCCESS " );

            $('#message').html(data.message + " " + data.error);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status === 401 || jqXHR.status === 400) {

            } else {
                throw new Error("an unexpected error occurred: " + errorThrown);
            }
        }
    });
}

/*function goToRegister() {
    $("#login").hide();
    $("#register").show();
}

function goToLogin() {
    $("#register").hide();
    $("#login").show();
}*/

function clearResetPasswordForm() {
    document.getElementById("email").value = '';
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

    event.preventDefault();
    var emailData = {
        email: document.getElementById("email").value,
    };
   // clearResetPasswordForm();

    $.ajax({
        url: "/resetpassword",
        type: "POST",
        data: JSON.stringify(emailData),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            console.log("OK " + data);
            window.location.replace("/");
        },
        error: function (jqXHR, textStatus, errorThrown) {

            console.log("ERROR");
        }
    });
}

function changePassword() {

    event.preventDefault();
    var passwordData = {
        plainPassword: document.getElementById("plainPassword").value,
        repeatPlainPassword: document.getElementById("repeatPlainPassword").value,
    };

    var token = getURLParameter('token');

    console.log("OK " + token);

    $.ajax({
        url: "/changepassword?token=" + token,
        type: "POST",
        data: JSON.stringify(passwordData),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            console.log("OK " + data);
            window.location.replace("/");
        },
        error: function (jqXHR, textStatus, errorThrown) {

            console.log("ERROR");
        }
    });
}

function getAuthTokenFromLocalStorage() {
    var token = localStorage.getItem(TOKEN_KEY);
    if (token) {
        return {"Authorization": token}; //this is the name after we will be looking for token in the back-end
    } else {
        return {};
    }
}

function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
}

/*headers: getAuthTokenFromLocalStorage(),*/