var profiles = [];
var likesGiven = [];
var likesReceived = [];
var authUsername;

window.onload = function () {

    $.get("navbar.html", function (data) {
        $("#nav-placeholder").replaceWith(data);
    });
    $.get("footer.html", function (data) {
        $("#footer-placeholder").replaceWith(data);
    });

    getAuthUserName();

    $.ajax({
        url: "/profiles",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            console.log("Got the profiles here");
            profiles = data;
            getAuthProfile();
            console.log(profiles.likesGiven);

        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot get the profiles");
        }
    });

    $('[data-toggle="tooltip"]').tooltip();
}

function displayProfilesCards() {

    //clear cards before re-making them
    var elements = document.getElementsByClassName('col-lg-4 wow fadeIn');
    while (elements.length > 0) {
        elements[0].parentNode.removeChild(elements[0]);
    }

    var cards = [];
    for (var i = 0; i < profiles.length; i++) {

        var photo = profiles[i].mainPhoto;
        if (photo === null) {
            photo = "../photos/photo-avatar.png";
        } else {
            photo = "data:image/png;base64," + photo;
        }

        var like = '';
        var message = '';
        console.log(likesGiven)
        if (likesGiven.some(function (e) {
                return e.username == profiles[i].username
            })) {
            like = 'text-danger';
            if (likesReceived.some(function (e) {
                return e.username == profiles[i].username
                })) {
                message = 'text-primary';
            }
        }

        cards[i] =
            $('<div>', {class: 'col-lg-4 wow fadeIn'}).append(
                $('<div>', {class: 'card'}).append(
                    $('<div>', {class: 'view overlay hm-white-slight'}).append(
                        $('<img>', {class: 'img-fluid', src: photo})
                    )
                ).append(
                    $('<div>', {class: 'card-block'}).append(
                        $('<h4>', {class: 'card-title', text: profiles[i].username}).append(
                            $('<a>', {
                                class: like + ' fa fa-heart pull-right',
                                onclick: "giveLike('" + profiles[i].username + "')",
                                data_toggle:'tooltip',  title: 'Like/Unlike'
                            })
                        ).append(
                            $('<a>', {
                                class: message + ' fa fa-commenting-o pull-right',
                                onclick: "sendMessage('" + profiles[i].username + "')",
                                data_toggle:'tooltip',  title: 'Send message'
                            })
                        )
                    ).append(
                        $('<p>', {class: 'card-text', text: profiles[i].address})
                    ).append(
                        $('<div>', {class: 'read-more'}).append(
                            $('<a>', {
                                class: 'btn btn-primary',
                                text: 'profile',
                                href: '/profile?name=' + profiles[i].username
                            })
                        )
                    )
                )
            )
        $(document.getElementsByClassName('row wow animated profile-cards')).append(cards[i]);
    }
}

function giveLike(username) {

    $.ajax({
        url: "/user/like?name=" + username,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            console.log("Like added to: " + username + data);
            getAuthProfile();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read username");
        }
    });
}

function getLikesGiven() {

}

function getLikesReceived() {

}

function sendMessage(username) {
    alert("Message to " + username);
}

function getAuthUserName() {

    $.ajax({
        url: "/user",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            console.log("User ata: " + data.username);
            authUsername = data.username;
            $("#userName").html(data.username);
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read username");
        }
    });
}

function getAuthProfile() {

    $.ajax({
        url: "/user/profile?name=" + authUsername,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus, jqXHR) {
            profile = data;
            likesGiven = profile.likesGiven;
            likesReceived = profile.likesReceived;
            displayProfilesCards();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read username");
        }
    });
}