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
    navBar();
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
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot get the profiles");
        }
    });

    $('.chips-placeholder').material_chip({
        placeholder: 'Enter a tag',
        secondaryPlaceholder: '+Tag',
    });

    /*Web Socket Connect*/
    connect();
};


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

        var like = 'text-muted';
        var message = 'text-muted';
        var likesMe = '';
        var toogleTitle = " doesn't like you";
        var like_unlike = 'Like';
        var online = ' text-muted';
        var online_toogle = 'Offline';
        if (profiles[i].online === true) {
            online = ' text-success';
            online_toogle = 'Online';
        }
        if (userLikesMe(profiles[i].username)) {
            likesMe = 'text-danger';
            toogleTitle = " likes you";
        }
        if (likesGiven.some(function (e) {
                return e == profiles[i].username
            })) {
            like = 'text-danger';
            if (userLikesMe(profiles[i].username)) {
                message = 'text-primary fa-commenting';
                like_unlike = 'Unlike';
            }
        }

        cards[i] =
            $('<div>', {
                class: 'col-lg-4 wow fadeIn',
                'data-wow-delay': '0.' + (2 + i % 3) + 's'
            }).append(
                $('<div>', {class: 'card card-cascade regular'}).append(
                    $('<div>', {class: 'view overlay hm-white-slight'}).append(
                        $('<img>', {class: 'img-fluid', src: photo})
                    )
                ).append(
                    $('<div>', {class: 'card-block'})
                        .append(
                            $('<h4>', {
                                class: 'card-title',
                                text: profiles[i].username
                            }).append(
                                $('<a>', {
                                    class: like + ' fa fa-heart pull-right',
                                    onclick: "giveLike('" + profiles[i].username + "')",
                                    'data-toggle': 'tooltip',
                                    title: like_unlike,
                                    'data-placement': 'bottom'
                                })
                            ).append(
                                $('<a>', {
                                    class: message + ' fa fa-commenting pull-right',
                                    onclick: "tryToSendMessage('" + profiles[i].username + "')",
                                    'data-toggle': 'tooltip',
                                    title: 'Send message',
                                    'data-placement': 'bottom'
                                })
                            )
                        ).append($('<i>', {
                        class: 'fa fa-circle pull-left' + online,
                        id : 'online-circle-' + profiles[i].username,
                        'aria-hidden': true,
                        'data-toggle': 'tooltip',
                        title: online_toogle,
                        'data-placement': 'bottom'
                    }))

                        .append(
                        $('<p>', {class: 'card-text', text: profiles[i].address})
                    ).append(
                        $('<div>', {class: 'read-more'}).append(
                            $('<span>', {
                                class: likesMe + ' fa fa-heartbeat pull-right',
                                'data-toggle': 'tooltip',
                                title: profiles[i].username + toogleTitle,
                                'data-placement': 'bottom'
                            })
                        )
                            .append(
                                $('<a>', {
                                    class: 'btn btn-outline-info btn-rounded waves-effect',
                                    text: 'profile',
                                    href: '/profile?name=' + profiles[i].username
                                })
                            )
                    )
                )
            )
        $(document.getElementsByClassName('row wow animated profile-cards')).append(cards[i]);
        //Initialize the tooltip
        $('[data-toggle="tooltip"]').tooltip();
    }
}

function giveLike(username) {
    //we reset the tooltips
    $('[data-toggle="tooltip"]').tooltip('dispose');

    $.ajax({
        url: "/user/like?name=" + username,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            console.log("Like added to: " + username + data.messages[0]);

            if (data.messages[0] === 'true') {
                toastr["success"]("You Like " + username);
                if (userLikesMe(username)) {
                    toastr["info"]("You are now linked !");
                }
            } else {
                toastr["warning"]("You un-like " + username);
                if (userLikesMe(username)) {
                    toastr["error"]("You are now disconnected with " + username);
                }
            }
            getAuthProfile();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read username");
        }
    });
}

function userLikesMe(username) {
    if (likesReceived.some(function (e) {
            return e == username
        })) {
        return true;
    }
}

function tryToSendMessage(username) {

    if (likesGiven.some(function (e) {
            return e == username
        }) && likesReceived.some(function (e) {
            return e == username
        })) {
        $("#message-to").html(username);
        document.getElementById("toUsername").value = username;
        $("#messageModal").modal('show');

    } else {
        $("#cannotMessageModal").modal('show');
    }
}

//Send message
document.getElementById("sendMessage").onclick = function () {

    var text = document.getElementById("message-text").value;
    document.getElementById("message-text").value = '';
    var username = document.getElementById("toUsername").value;

    var message = {message: text};
    $.ajax({
        url: "/user/send-message?name=" + username,
        type: "POST",
        data: JSON.stringify(message),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            console.log("Message sent ok");
            $("#messageModal").modal('hide');
            toastr["success"]("Message sent to " + username);
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Message send error");
            $("#messageModal").modal('hide');
            toastr["error"]("Error sending message to " + username);
        }
    });
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
            authUsername = data.username;
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