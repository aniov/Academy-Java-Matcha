var pictures = [];

window.onload = function () {
    $.get("navbar.html", function (data) {
        $("#nav-placeholder").replaceWith(data);
    });
    $.get("footer.html", function (data) {
        $("#footer-placeholder").replaceWith(data);
    });

    $.ajax({
        url: "/user",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus, jqXHR) {
            $("#userName").html(data.username);
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read username !!!!!!!");
        }

    });

    getPictures();
}

function getPictures() {
    $.ajax({
        url: "/user/photos",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus, jqXHR) {
            pictures = data;
            showPictures();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot load pictures !!");
        }
    });
}

function addUploadedPicture(newPicture) {
    pictures.push(newPicture);
    showPictures();
}

function showPictures() {

    for (var i = 0; i < 9; i++) {
        if (i < pictures.length) {
            document.getElementById("img-big-" + (i + 1).toString()).href = "data:image/png;base64," + pictures[i].pictureData;
            document.getElementById("img-big-" + (i + 1).toString()).setAttribute("data-size", pictures[i].pictureWidth.toString() + "x" + pictures[i].pictureHeight.toString());
            document.getElementById("img-small-" + (i + 1).toString()).src = "data:image/png;base64," + pictures[i].pictureData;
        } else {
            document.getElementById("img-big-" + (i + 1).toString()).href = "/photos/photo-avatar.png";
            document.getElementById("img-big-" + (i + 1).toString()).setAttribute("data-size", "327x250");
            document.getElementById("img-small-" + (i + 1).toString()).src = "/photos/photo-avatar.png";
        }
    }

}

function deletePhoto(photoPosition) {

    var toDelete = "?id=" + pictures[photoPosition - 1].id;
    $.ajax({
        url: "/user/delete-photo" + toDelete,
        type: "DELETE",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            pictures.splice(pictures[photoPosition - 1], 1);
            showPictures();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot delete photo");
        }
    });

}

function setMainPhoto(photoPosition) {
    var toBeMain = "?id=" + pictures[photoPosition - 1].id;
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

function setAsMainIfOnePhoto() {
    setMainPhoto(0);
}