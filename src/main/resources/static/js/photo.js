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

}

function showPictures() {

    // for (var i = 0; i < pictures.length; i++) {
    document.getElementById("img-big-1").href = "data:image/png;base64," + pictures[1].pictureData;
    document.getElementById("img-big-1").setAttribute("data-size", pictures[8].pictureWidth.toString() + "x" + pictures[8].pictureHeight.toString());
    document.getElementById("img-small-1").src = "data:image/png;base64," + pictures[1].pictureData;


    // }

}