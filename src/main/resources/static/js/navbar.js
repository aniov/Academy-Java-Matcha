let currentUser;
let currentUserProfile;
function navBar() {

    $.ajax({
        url: "/user/profile",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            currentUserProfile = data;
            currentUser = data.username;
            $("#userName").html(data.username);
            document.getElementById("nav-user-photo").src = getMainPhoto();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read username");
        }
    });

    getUserMessages();
}

function getUserMessages() {

    $.ajax({
        url: "/user/messages",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
           $("#settings").html(unreadMessages(data));
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read received messages");
        }
    });
}

function unreadMessages(messages) {

    let unread = '';
    for (let i = 0; i < messages.length; i++) {
        if (! messages[i].read) {
            unread++;
        }
    }
    return unread;
}

function getMainPhoto() {

    let mainPhoto = currentUserProfile.mainPhoto;
    if (mainPhoto === null) {
        mainPhoto = "../photos/photo-avatar.png";
    } else {
        mainPhoto = "data:image/png;base64," + mainPhoto;
    }
    return mainPhoto;
}

function logout() {

    $.ajax({
        url: "/logout",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function () {
            window.location.replace("/login");
        },
        error: function () {
            console.log("Cannot logout");
        }
    });
}
