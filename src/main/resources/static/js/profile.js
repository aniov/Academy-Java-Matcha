window.onload = function(){
    $.get("navbar.html", function(data){
        $("#nav-placeholder").replaceWith(data);
    });
    $.get("footer.html", function(data){
        $("#footer-placeholder").replaceWith(data);
    });
    $.get("googlemap.html", function(data){
        $("#google-placeholder").replaceWith(data);
    });

    $.ajax({
        url: "/user",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus, jqXHR) {
            console.log("User ata: " + data.username)
            $("#userName").html(data.username);
        },
        error: function (data, textStatus, jqXHR) {

            console.log("Cannot read username !!!!!!!");
        }
    });

    loadProfileData();
}

function edit() {

}

function loadProfileData() {

    $.ajax({
        url: "/user/profile",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus, jqXHR) {
            console.log("User data: " + data.whatImDoing);
            $("#self-summary").html(data.aboutMe);
            $("#what-im-doing").html(data.whatImDoing);
            $("#good-at").html(data.goodAt);
            $("#favorite-stuff").html(data.favorites);

        },
        error: function (data, textStatus, jqXHR) {

            console.log("Cannot read username");
        }
    });

}