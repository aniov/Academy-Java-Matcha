let currentUser;
let currentUserProfile;
let visitorsData = [];

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
        if (!messages[i].read) {
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

function getVisitorsInfo() {

    $.ajax({
        url: "/visitors",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            visitorsData = data;
            createVisitorsContent();
            $("#visitors-modal").modal('show');
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot load visitors");
        }
    });
}

function showVisitors() {
    getVisitorsInfo();
}

function createVisitorsContent() {

    $(document.getElementById('visitors-body').innerHTML = '');

    let visitors = [];
    for (let i = 0; i < visitorsData.length; i++) {
        visitors[i] = $('<div>', {
            class: 'media mb-1'
        }).append($('<a>', {
                class: 'media-left waves-light'
            }).append($('<img>', {
                class: 'rounded-circle',
                src: "data:image/png;base64," + visitorsData[i].mainPhoto,
                style: 'height: 80px; width: 80px',
                onclick: "goSeeProfile('" + visitorsData[i].username + "')",
            }))
        ).append($('<div>', {
                class: 'media-body'
            }).append($('<h4>', {
                class: 'media-heading',
                text: visitorsData[i].username
            })
            ).append($('<ul>', {
                class: 'rating inline-ul'
            }).append($('<li>', {}
            ).append($('<i>', {
                class: 'fa fa-star amber-text'
            })).append($('<li>', {
                text: $.format.date(visitorsData[i].date, 'dd/MM/yyyy HH:mm:ss')
            }))
            )
            )
        );
    }


    $(document.getElementById('visitors-body')).append(visitors);

}

function goSeeProfile(username) {
    window.location.replace('/profile?name=' + username);
}
