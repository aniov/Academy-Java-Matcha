var currentUser;
var currentUserProfile;
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
           $("#new-messages").html(unreadMessages(data));
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read received messages");
        }
    });
}

function unreadMessages(messages) {

    var unread = 0;
    for (var i = 0; i < messages.length; i++) {
        if (! messages[i].isRead) {
          //  alert("oh");
            unread++;
        }
    }
    return unread;
}

function getMainPhoto() {

    var mainPhoto = currentUserProfile.mainPhoto;
    if (mainPhoto === null) {
        mainPhoto = "../photos/photo-avatar.png";
    } else {
        mainPhoto = "data:image/png;base64," + mainPhoto;
    }
    return mainPhoto;
}
