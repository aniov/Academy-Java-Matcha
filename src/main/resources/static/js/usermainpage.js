let profiles = [];
let likesGiven = [];
let likesReceived = [];
let authUsername;

window.onload = function () {

    $.get("navbar.html", function (data) {
        $("#nav-placeholder").replaceWith(data);
    });
    $.get("footer.html", function (data) {
        $("#footer-placeholder").replaceWith(data);
    });
    navBar();
    getAuthUserName();
    getProfiles();

    /*Web Socket Connect*/
    connect();
};

function getProfiles() {

    let url = '/profiles';
    let param = getURLParameter('interest');
    if (param !== null) {
        url += '?interest=' + param;
        document.getElementById('profiles-result-title').innerHTML = 'Results found by interest: ' + param;
    } else {
        document.getElementById('profiles-result-title').innerHTML = "Your best match's";
    }

    $.ajax({
        url: url,
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
}


function displayProfilesCards() {

    //clear cards before re-making them
    let elements = document.getElementsByClassName('col-lg-4 wow fadeIn');
    while (elements.length > 0) {
        elements[0].parentNode.removeChild(elements[0]);
    }

    let cards = [];
    for (let i = 0; i < profiles.length; i++) {

        let photo = profiles[i].mainPhoto;
        if (photo === null) {
            photo = "../photos/photo-avatar.png";
        } else {
            photo = "data:image/png;base64," + photo;
        }

        let like = 'text-muted';
        let message = 'text-muted';
        let likesMe = '';
        let toogleTitle = " doesn't like you";
        let like_unlike = 'Like';
        let online = ' text-muted';
        let online_toogle = 'Offline';
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
            }).append($('<div>', {
                    class: 'card card-cascade regular ovf-hidden'
                }).append($('<div>', {
                    class: 'view overlay hm-white-slight'
                }).append($('<img>', {
                    class: 'img-fluid',
                    src: photo
                }))
                ).append($('<div>', {
                    class: 'card-block'
                }).append(
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
                        class: message + ' fa fa-commenting pull-right ',
                        onclick: "tryToSendMessage('" + profiles[i].username + "')",
                        'data-toggle': 'tooltip',
                        title: 'Send message',
                        'data-placement': 'bottom'
                    })
                )
                ).append($('<i>', {
                    class: 'fa fa-circle pull-left' + online,
                    id: 'online-circle-' + profiles[i].username,
                    'aria-hidden': true,
                    'data-toggle': 'tooltip',
                    title: online_toogle,
                    'data-placement': 'bottom'
                })).append(
                $('<p>', {class: 'card-text', text: profiles[i].address})
                ).append(
                $('<div>', {class: 'read-more'}).append(
                    $('<span>', {
                        class: likesMe + ' fa fa-heartbeat pull-right',
                        'data-toggle': 'tooltip',
                        title: profiles[i].username + toogleTitle,
                        'data-placement': 'bottom'
                    })
                ).append(
                    $('<a>', {
                        class: 'btn btn-outline-info btn-rounded waves-effect',
                        text: 'profile',
                        href: '/profile?name=' + profiles[i].username
                    })
                )
                ).append($('<a>', {
                    class: 'activator btn-floating btn-myaction btn-sm transparent',
                    onclick: "getUserInterest('" + profiles[i].username + "','" + i + "')",
                }).append($('<i>', {
                    class: 'fa fa-angle-double-up',
                    style: 'text-shadow: -1px 0 grey, 0 0px grey, 1px 0 grey, 0 -1px grey;'
                }))
                )
                ).append($('<div>', {
                    class: 'card-reveal aqua-gradient'
                }).append($('<div>', {
                    class: 'content text-center'
                }).append($('<h5>', {
                    class: 'card-title pull-left',
                    text: profiles[i].username + "'s interests"
                }).append($('<i>', {
                    class: 'fa fa-close'
                })).append($('<hr>', {
                    class: 'extra-margin my-2'
                }))).append($('<div>', {
                    id: 'tags-display-' + i,
                    class: 'pull-left'
                }))
                )
                )
            );

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

    let text = document.getElementById("message-text").value;
    document.getElementById("message-text").value = '';
    let username = document.getElementById("toUsername").value;

    let message = {message: text};
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
};


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

function getUserInterest(username, index) {

    console.log("username: " + username + ", index: " + index);

    $.ajax({
        url: "/user/interest?username=" + username,
        type: "GET",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            createInterestChips(data, index);
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot see current logged user");
        }
    });

}

function createInterestChips(interests, index) {

    $(document.getElementById('tags-display-' + index).innerHTML = '');

    let tags = [];
    for (let i = 0; i < interests.length; i++) {
        tags[i] = $('<a>', {
            class: 'chip pull-left',
            onclick: "searchByTag('" + interests[i].interest + "')",
            text: interests[i].interest
        });
    }

    $(document.getElementById('tags-display-' + index)).append(tags.reverse());
}

function searchByTag(interest) {
    window.location.replace('/main?interest=' + interest);
}

let timeoutID = null;

function findProfilesByNameOrLocation(str) {

    if (!str) {
        getProfiles();
        return;
    }

    let location = document.getElementById("location-like").checked;
    let searchBy = 'name-like';
    if (location) {
        searchBy = 'location-like';
    }

    $.ajax({
        url: '/profile/search?' + searchBy + '=' + str,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            profiles = data;
            document.getElementById('profiles-result-title').innerHTML = 'Results found by ' + searchBy;
            displayProfilesCards();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot get the profiles");
        }
    });


}

$('#search').keyup(function (e) {
    clearTimeout(timeoutID);
    timeoutID = setTimeout(findProfilesByNameOrLocation.bind(undefined, e.target.value), 500);
});
