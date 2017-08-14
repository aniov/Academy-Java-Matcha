var pictures = [];
var profile;
var username;

window.onload = function () {
    $.get("navbar.html", function (data) {
        $("#nav-placeholder").replaceWith(data);
    });
    $.get("footer.html", function (data) {
        $("#footer-placeholder").replaceWith(data);
    });
    navBar();

    $.ajax({
        url: "/user",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus, jqXHR) {
            username = data.username;
            if (username === getURLParameter('name')) {
                showUploadPhoto();
            }
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read username !!!!!!!");
        }

    });

    getPictures();
}

function getPictures() {
    $.ajax({
        url: "/user/photos?name=" + getURLParameter('name'),
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus, jqXHR) {
            pictures = data;
            document.getElementById('photo-album-of').innerHTML = getURLParameter('name') + "'s ";
            //we reset the tooltips
            $('[data-toggle="tooltip"]').tooltip('dispose');
            displayPhotoCards();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot load pictures !!");
        }
    });
}

function addUploadedPicture(newPicture) {
    pictures.push(newPicture);
    displayPhotoCards();
}

function deletePhoto(photoPosition) {

    var toDelete = "?id=" + pictures[photoPosition].id;
    $.ajax({
        url: "/user/delete-photo" + toDelete,
        type: "DELETE",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
           getPictures();
            navBar();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot delete photo");
        }
    });
}

function setMainPhoto(photoPosition) {
    var toBeMain = "?id=" + pictures[photoPosition].id;
    $.ajax({
        url: "/user/set-main-photo" + toBeMain,
        type: "PUT",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            console.log("Main picture set");
            getPictures();
            navBar();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Error set picture as main");
        }
    });
}

function showUploadPhoto() {
    document.getElementById("uploadPhoto").style.display = "inline";
    //drag And Drop function
    dragAndDrop();
}

function displayPhotoCards() {
    //clear cards before re-making them
    var elements = document.getElementsByClassName('all-cards');
    while (elements.length > 0) {
        elements[0].parentNode.removeChild(elements[0]);
    }

    var cards = [];
    var cardButtons;
    var pictureData;
    var pictureSize;
    var set_main;

    //We have maximum 9 photos
    for (var i = 0; i < 9; i++) {

        cardButtons = '';
        pictureData = '';
        pictureSize = '';
        set_main = ' text-muted';

        if (i < pictures.length && pictures[i].profilePicture === true) {
            set_main = ' text-success';
        }

        if (username === getURLParameter('name') && pictures.length > i) {
            cardButtons = createPhotoButtons(i, set_main);
        }
        if (i < pictures.length) {
            pictureData = "data:image/png;base64," + pictures[i].pictureData;
            pictureSize = pictures[i].pictureWidth.toString() + "x" + pictures[i].pictureHeight.toString();
        } else {
            pictureData = "/photos/photo-avatar.png";
            pictureSize = "327x250";
        }
        cards[i] =
            $('<figure>', {
                class: 'col-lg-5 col-md-6 col-sm-12 wow fadeIn text-center all-cards',
                style: 'height: 300px; width: 400px;',
                'data-wow-delay': '0.' + (2 + i % 3) + 's'
            }).append(
                $('<a>', {
                    href: pictureData,
                    'data-size': pictureSize,
                    class: 'photo'
                }).append(
                    $('<img>', {
                        class: 'img-fluid z-depth-2',
                        src: pictureData,
                        style: 'max-width: 100%; max-height: 100%;',
                        alt: 'Gallery image'
                    })
                )
            ).append($('<div>', {class: 'img-ov'})
                .append(cardButtons)
            )

    }
    $(document.getElementById('photo-cards')).append(cards);
    $('[data-toggle="tooltip"]').tooltip();
}

function createPhotoButtons(i, set_main) {

    return (

        $('<div>').append(
            $('<a>', {
                class: 'fa fa-check-circle fa-3x set-profile-picture mr-5' + set_main,
                id: 'main_picture_button',
                'aria-hidden': 'true',
                onclick: "setMainPhoto('" + i + "')",
                text: '',
                'data-toggle': 'tooltip',
                title: 'Set as main profile photo',
                'data-placement': 'bottom'
            })
        ).append(
            $('<a>', {
                class: 'fa fa-times-circle fa-3x text-danger delete-profile-picture',
                'aria-hidden': 'true',
                type: 'button',
                onclick: "deletePhoto('" + i + "')",
                'data-toggle': 'tooltip',
                title: 'Delete photo',
                'data-placement': 'bottom'
            })
        ))
}

function goToProfile() {
    window.location.replace('/profile?name=' + getURLParameter('name'));
}