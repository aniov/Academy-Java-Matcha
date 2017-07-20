/**
 * Created by aniov on 7/13/2017.
 */

function login() {

    event.preventDefault();
       var username= document.getElementById("username").value;
       var password= document.getElementById("password").value;
    clearLoginForm();

    $.ajax({
        data: "username="+username+"&password="+password,
        timeout: 1000,
        type: 'POST',
        url: '/login'

    }).done(function(data, textStatus, jqXHR) {
        window.location.replace("/main");

    }).fail(function(data, jqXHR, textStatus) {

        $("#login-message").html(data.responseJSON.message)
            .show().fadeTo(4000, 500).slideUp(500, function () {
            $("#login-message").slideUp(500);
        });
    });

    /*$.ajax({
        url: "/login",
        type: "POST",
        data: "username="+username+"&password="+password,
        success: function (data, textStatus, jqXHR) {
            console.log(data);
          //  window.location.replace("/main");
        },
        error: function (data, textStatus, jqXHR) {

            console.log("ERROR LOG");

            $("#reset-login-message").html(data.responseJSON.message)
                .show().fadeTo(3000, 500).slideUp(500, function () {
                $("#reset-login-message").slideUp(500);
            });
        }
    });*/
}

/*$('#loginform').submit(function (event) {
    event.preventDefault();
    var data = 'username=' + $('#username').val() + '&password=' + $('#password').val();
    $.ajax({
        data: data,
        timeout: 1000,
        type: 'POST',
        url: '/login'

    }).done(function(data, textStatus, jqXHR) {
      //  var preLoginInfo = JSON.parse($.cookie('dashboard.pre.login.request'));
      //  window.location = preLoginInfo.url;
        window.location = "/main";
        console.log("OK");

    }).fail(function(data, jqXHR, textStatus) {
        console.log("ERROR LOG");

        $("#reset-login-message").html(data.responseJSON.message)
            .show().fadeTo(3000, 500).slideUp(500, function () {
            $("#reset-login-message").slideUp(500);
        });
    });
});*/

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
            console.log("Created SUCCESS");
            $("#register-success-message").html(data.message)
                .show().fadeTo(4000, 500).slideUp(500, function () {
                $("#register-message").slideUp(500);
            });

        },
        error: function (data, textStatus, jqXHR) {
            console.log("Error creating account");
            $("#register-message").html(data.responseJSON.message)
                .show().fadeTo(4000, 500).slideUp(500, function () {
                $("#register-message").slideUp(500);
            });
        }
    });
}

function clearChangePasswordForm() {
    document.getElementById("password").value = '';
    document.getElementById("repeatPlainPassword").value = '';
}

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
    clearResetPasswordForm();

    $.ajax({
        url: "/resetpassword",
        type: "POST",
        data: JSON.stringify(emailData),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {

            $("#success-message").html(data.message)
                .show().fadeTo(5000, 500).slideUp(500, function () {
                $("#success-message").slideUp(500);
            });
        },
        error: function (data, textStatus, jqXHR) {

            $("#reset-message").html(data.responseJSON.message)
                .show().fadeTo(3000, 500).slideUp(500, function () {
                $("#reset-message").slideUp(500);
            });
        }
    });
}

function changePassword() {

    event.preventDefault();
    var passwordData = {
        plainPassword: document.getElementById("password").value,
        repeatPlainPassword: document.getElementById("repeatPlainPassword").value,
    };
    clearChangePasswordForm();

    var token = getURLParameter('token');

    $.ajax({
        url: "/changepassword?token=" + token,
        type: "POST",
        data: JSON.stringify(passwordData),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
/*        success: function (data, textStatus, jqXHR) {

            $("#success-message").html(data.message)
                .show().fadeTo(5000, 500).slideUp(500, function () {
                $("#success-message").slideUp(500);
            });
        },
        error: function (data, textStatus, jqXHR) {

            $("#change-message").html(data.responseJSON.message)
                .show().fadeTo(3000, 500).slideUp(500, function () {
                $("#change-message").slideUp(500);
            });
        }*/
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

function showToken() {

    console.log(localStorage.getItem(TOKEN_KEY));

}