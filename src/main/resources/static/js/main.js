/**
 * Created by aniov on 7/13/2017.
 */

// Tooltips Initialization
$(function () {
    $('[data-toggle="tooltip"]').tooltip()
})

function login() {

    event.preventDefault();
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    clearLoginForm();

    $.ajax({
        data: "username=" + username + "&password=" + password,
        timeout: 1000,
        type: 'POST',
        url: '/login'

    }).done(function (data, textStatus, jqXHR) {
        window.location.replace("/main");

    }).fail(function (data, jqXHR, textStatus) {

        $("#login-message").html(data.responseJSON.message)
            .show().fadeTo(4000, 500).slideUp(500, function () {
            $("#login-message").slideUp(500);
        });
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
            console.log("Created SUCCESS");
            $("#register-success-message").html(data.messages)
                .show().fadeTo(4000, 500).slideUp(500, function () {
                $("#register-success-message").slideUp(1000);
                $("#register-form").hide();
                $("#created-message").show();
            });

        },
        error: function (data, textStatus, jqXHR) {
            console.log("Error creating account " + data.responseJSON);
            $("#register-message").html(parseListMessages(data.responseJSON.messages))
                .show().fadeTo(4000, 500).slideUp(500, function () {
                $("#register-message").slideUp(500);
            });
        }
    });
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

function changePassword() {

    event.preventDefault();
    var passwordData = {
        plainPassword: document.getElementById("plainPassword").value,
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
        success: function (data, textStatus, jqXHR) {

            $("#success-message").html(data.messages)
                .show().fadeTo(5000, 500).slideUp(500, function () {
                $("#success-message").slideUp(500);
                window.location.replace("/login");
            });
        },
        error: function (data, textStatus, jqXHR) {
            $("#change-message").html(parseListMessages(data.responseJSON.messages))
                .show().fadeTo(4000, 500).slideUp(500, function () {
                $("#change-message").slideUp(500);
            });
        }
    });
}

function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
}

function parseListMessages(list) {
    var text = "<ul>";
    for (i = 0; i < list.length; i++) {
        text += "<li>" + list[i] + "</li>";
    }
    return text + "</ul>";
}

function clearChangePasswordForm() {
    document.getElementById("plainPassword").value = '';
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