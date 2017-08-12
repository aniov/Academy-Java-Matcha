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
            pictures.splice(photoPosition, 1);
            //showPictures();
            displayPhotoCards();
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
    var elements = document.getElementsByClassName('col-lg-4 col-md-6 col-sm-12 wow fadeIn text-center');
    while (elements.length > 0) {
        elements[0].parentNode.removeChild(elements[0]);
    }

    var cards = [];
    var cardButtons;
    var pictureData;
    var pictureSize;
    //We have maximum 9 photos
    for (var i = 0; i < 9; i++) {

        if (username === getURLParameter('name')) {
            cardButtons = createPhotoButtons(i);
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
                class: 'col-lg-4 col-md-6 col-sm-12 wow fadeIn text-center',
                style: 'height: 300px; width: 400px;',
                'data-wow-delay': '0.' + (2 + i % 3) + 's'
            }).append(
                $('<a>', {
                    href: pictureData,
                    'data-size': pictureSize
                }).append(
                    $('<img>', {
                        class: 'img-fluid z-depth-2',
                        src: pictureData,
                        style: 'max-width: 100%; max-height: 100%;',
                        alt: 'Gallery image'
                    })
                )
            ).append(cardButtons);
    }
    $(document.getElementById('photo-cards')).append(cards);
}

function createPhotoButtons(i) {

    return photoButtons =

        $('<div>').append(
            $('<button>', {
                class: 'btn btn-outline-primary btn-sm btn-rounded waves-effect set-profile-picture',
                type: 'button',
                onclick: "setMainPhoto('" + i + "')",
                text: 'Set main'
            })
        ).append(
            $('<button>', {
                class: 'btn btn-outline-danger btn-sm btn-rounded waves-effect delete-picture',
                type: 'button',
                onclick: "deletePhoto('" + i + "')",
                text: 'delete'
            })
        );
}