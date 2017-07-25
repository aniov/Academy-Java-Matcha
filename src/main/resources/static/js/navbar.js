/*
window.onload = function getCurrentUser() {

    $.ajax({
        url: "/user/me",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus, jqXHR) {
            console.log("User ata: " + data.username)
            $("#userName").html(data.username);
        },
        error: function (data, textStatus, jqXHR) {

            console.log("Cannot read username");
        }
    });
}*/
