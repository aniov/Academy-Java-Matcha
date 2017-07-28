var profile;

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
            $("#userName").html(data.username);
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read username !!!!!!!");
        }
    });

    loadProfileData();
}

function loadProfileData() {

    $.ajax({
        url: "/user/profile",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus, jqXHR) {
            profile = data;
            $("#self-summary").html(data.aboutMe);
            $("#what-im-doing").html(data.whatImDoing);
            $("#good-at").html(data.goodAt);
            $("#favorite-stuff").html(data.favorites);
            $("#profile-username").html(data.username);

        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read username");
        }
    });
}

function editSummary() {
    document.getElementById("message-text").value = profile.aboutMe;
    $("#editStatus").modal('show');
    document.getElementById("fieldVariable").value = "aboutMe";
}

function editWhatImDoing() {
    document.getElementById("message-text").value = profile.whatImDoing;
    $("#editStatus").modal('show');
    document.getElementById("fieldVariable").value = "whatImDoing";
}

function editGoodAt() {
    document.getElementById("message-text").value = profile.goodAt;
    $("#editStatus").modal('show');
    document.getElementById("fieldVariable").value = "goodAt";
}

function editFavorites() {
    document.getElementById("message-text").value = profile.favorites;
    $("#editStatus").modal('show');
    document.getElementById("fieldVariable").value = "favorites";
}

document.getElementById("saveChanges").onclick = function () {

    var text = document.getElementById("message-text").value;
    var field = document.getElementById("fieldVariable").value;

    profile[field] = text;
    $.ajax({
        url: "/user/profile",
        type: "POST",
        data: JSON.stringify(profile),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            console.log("Edit success");
            $("#editStatus").modal('hide');
            loadProfileData();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Edit error");
            $("#editStatus").modal('hide');
        }
    });
}