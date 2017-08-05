var profiles = [];

window.onload = function () {

    $.get("navbar.html", function (data) {
        $("#nav-placeholder").replaceWith(data);
    });
    $.get("footer.html", function (data) {
        $("#footer-placeholder").replaceWith(data);
    });

    $.ajax({
        url: "/profiles",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus, jqXHR) {
            console.log("Got the profiles here");
            profiles = data;
            console.log(profiles.length);
            createCards();

        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot get the profiles");
        }
    });


}

function displayProfilesCards(photo, cards, i) {

    cards[i] =
        $('<div>', {class: 'col-lg-4 wow fadeIn'}).append(
            $('<div>', {class: 'card'}).append(
                $('<div>', {class: 'view overlay hm-white-slight'}).append(
                    $('<img>', {class: 'img-fluid'}).attr('src', photo)
                )
            )
        )
    $(document.getElementsByClassName('profile-cards')).append(cards[i]);
}

function createCards() {

    var cards = [];
    for (var i = 0; i < profiles.length; i++) {

        $.ajax({
            url: "/user/photo-main" + "?name=" + profiles[i].username,
            type: "GET",
            contentType: "application/json; charset=utf-8",
            success: function (data, textStatus, jqXHR) {
                displayProfilesCards("data:image/png;base64," + data.pictureData, cards, i);

            },
            error: function (data, textStatus, jqXHR) {
                console.log("Cannot load main photo !!");
            }
        });
    }
}