var profiles = [];

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
        success: function (data, textStatus, jqXHR) {
            console.log("Got the profiles here");
            profiles = data;
            console.log(profiles.length);
            //createCards();
            displayProfilesCards();

        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot get the profiles");
        }
    });


}

function displayProfilesCards() {

    var cards = [];
    for (var i = 0; i < profiles.length; i++) {

        var photo = profiles[i].mainPhoto;
        if (photo === null) {
            photo = "../photos/photo-avatar.png";
        } else {
            photo = "data:image/png;base64," + photo;
        }

        cards[i] =
            $('<div>', {class: 'col-lg-4 wow fadeIn'}).append(
                $('<div>', {class: 'card'}).append(
                    $('<div>', {class: 'view overlay hm-white-slight'}).append(
                        $('<img>', {class: 'img-fluid', src:  photo})
                    )
                ).append(
                    $('<div>', {class: 'card-block'}).append(
                        $('<h4>', {class: 'card-title', text: profiles[i].username})
                    ).append(
                        $('<p>', {class: 'card-text', text: profiles[i].address})
                    ).append(
                        $('<div>', {class: 'read-more'}).append(
                            $('<a>', {class: 'btn btn-primary', text: 'Read more', href: '#'})
                        )
                    )
                )
            )
        $(document.getElementsByClassName('row wow animated profile-cards')).append(cards[i]);
    }
}

function getAuthUserName() {

    $.ajax({
        url: "/user",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus, jqXHR) {
            console.log("User ata: " + data.username)
            $("#userName").html(data.username);
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read username");
        }
    });
    
}