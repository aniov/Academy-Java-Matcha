var profile;
var authUser;

window.onload = function () {
    $.get("navbar.html", function (data) {
        $("#nav-placeholder").replaceWith(data);
    });
    $.get("footer.html", function (data) {
        $("#footer-placeholder").replaceWith(data);
    });
    $.get("googlemap.html", function (data) {
        $("#google-placeholder").replaceWith(data);
    });

    navBar();

    $.ajax({
        url: "/user/profile?name=" + getURLParameter('name'),
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data, textStatus, jqXHR) {
            profile = data;
            getAuthUser();
            loadProfileData();
            loadCards();
            setPlaceId(profile.googleLocationID);
            populateHeightDropDown();
            setInputDataInFields();
            $('.mdb-select').material_select();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read username");
        }
    });

    tags();

    /*Web Socket Connect*/
    connect();
};

function loadProfileData() {
    $("#self-summary").html(profile.aboutMe);
    $("#what-im-doing").html(profile.whatImDoing);
    $("#good-at").html(profile.goodAt);
    $("#favorite-stuff").html(profile.favorites);
    $("#profile-username").html("Profile of " + profile.username);
    var firstName = profile.firstName === null ? "" : profile.firstName;
    var lastName = profile.lastName === null ? "" : profile.lastName;
    $("#profile-realName").html(firstName + " " + lastName);
    if (profile.address !== null) {
        document.getElementById("pac-input").value = profile.address;
        document.getElementById("userLocation").innerHTML = profile.address;
    }

    showMainProfilePhoto();
}

function loadCards() {

    var sexOrientation = profile.sexualOrientation === null ? "" : profile.sexualOrientation + ", ";
    var gender = profile.gender === null ? "" : profile.gender + ", ";
    var status = profile.status === null ? "" : profile.status + ", ";
    var date = profile.bornDate === null ? "" : calculateAge(profile.bornDate) + " years old";
    var address = profile.address === null ? "" : "<p>From: " + profile.address + "</p>";
    if (profile.sexualOrientation || profile.gender || profile.status || profile.bornDate > 0) {
        $("#card-one").html(gender + sexOrientation + status + date + address);
    }

    var ethnicity = profile.ethnicity === null ? "" : profile.ethnicity + ", ";
    var bodyType = profile.bodyType === null ? "" : "Body: " + profile.bodyType + ", ";
    var height = profile.height === null ? "" : "Height: " + profile.height + " cm";
    if (profile.ethnicity || profile.bodyType || profile.height) {
        $("#card-two").html(ethnicity + bodyType + height);
    }
    $("#card-three").html("Looking for: " + profile.lookingFor);
}

function editSummary() {
    document.getElementById("message-text").value = profile.aboutMe;
    charsToWrite();
    $("#editStatus").modal('show');
    document.getElementById("fieldVariable").value = "aboutMe";
}

function editWhatImDoing() {
    document.getElementById("message-text").value = profile.whatImDoing;
    charsToWrite();
    $("#editStatus").modal('show');
    document.getElementById("fieldVariable").value = "whatImDoing";
}

function editGoodAt() {
    document.getElementById("message-text").value = profile.goodAt;
    charsToWrite();
    $("#editStatus").modal('show');
    document.getElementById("fieldVariable").value = "goodAt";
}

function editFavorites() {
    document.getElementById("message-text").value = profile.favorites;
    charsToWrite();
    $("#editStatus").modal('show');
    document.getElementById("fieldVariable").value = "favorites";
}

function editInfo() {
    setInputDataInFields();
    $("#editInfo").modal('show');
}

function setInputDataInFields() {
    /*Gender buttons*/
    document.getElementById("classGenderFemale").className = "btn btn-primary btn-rounded";
    document.getElementById("classGenderMale").className = "btn btn-primary btn-rounded";
    if (profile.gender === 'Man') {
        document.getElementById("classGenderMale").className += " active";
    } else if (profile.gender === 'Woman') {
        document.getElementById("classGenderFemale").className += " active";
    }
    /*Looking for buttons*/
    var looking = profile.lookingFor;
    document.getElementById("classLookingForWoman").className = "btn btn-primary btn-rounded";
    document.getElementById("classLookingForMan").className = "btn btn-primary btn-rounded";
    for (i = 0; i < looking.length; i++) {
        if (looking[i] === 'Man') {
            document.getElementById("classLookingForMan").className += " active";
            document.getElementById("lookingForMan").checked = "checked";
        }
        if (looking[i] === 'Woman') {
            document.getElementById("classLookingForWoman").className += " active";
            document.getElementById("lookingForWoman").checked = "checked";
        }
    }
    /*Marital status select box*/
    document.getElementById("status").value = profile.status;

    /*Ethnicity select box*/
    document.getElementById("ethnicity").value = profile.ethnicity;

    /*Body type*/
    document.getElementById("bodyType").value = profile.bodyType;

    /*Height*/
    document.getElementById("height").value = profile.height;

    /*Name*/
    document.getElementById("firstName").value = profile.firstName;
    document.getElementById("lastName").value = profile.lastName;
    initializeDatePicker();
    /*Born date*/
    if (profile.bornDate > 0) {
        var d = new Date(profile.bornDate);
        var date = (d.getMonth() + 1) + "/" + d.getDate() + "/" + d.getFullYear();
        document.getElementById("datepicker").value = date;
    }
}

/*Save Basic info*/
document.getElementById("saveInfo").onclick = function () {
    /*My Gender*/
    var man = document.getElementById("genderMale").checked;
    var woman = document.getElementById("genderFemale").checked;
    if (man) {
        profile.gender = "Man";
    } else if (woman) {
        profile.gender = "Woman";
    }

    /*Looking for*/
    var looking = [];
    man = document.getElementById("lookingForMan").checked;
    woman = document.getElementById("lookingForWoman").checked;
    if (man) {
        looking.push("Man");
    }
    if (woman) {
        looking.push("Woman");
    }
    profile.lookingFor = looking;

    /*Marital status*/
    var e = document.getElementById("status");
    if (e.selectedIndex !== -1) {
        profile.status = e.options[e.selectedIndex].text;
    }

    /*Ethnicity*/
    e = document.getElementById("ethnicity");
    if (e.selectedIndex !== -1) {
        profile.ethnicity = e.options[e.selectedIndex].text;
    }

    /*Body type*/
    e = document.getElementById("bodyType");
    if (e.selectedIndex !== -1) {
        profile.bodyType = e.options[e.selectedIndex].text;
    }

    /*Height*/
    e = document.getElementById("height");
    if (e.selectedIndex !== -1) {
        profile.height = e.options[e.selectedIndex].value;
    }

    /*Name*/
    var firstName = document.getElementById("firstName").value;
    var lastName = document.getElementById("lastName").value;

    profile.firstName = firstName;
    profile.lastName = lastName;

    /*Born date*/
    var date = document.getElementById("datepicker").value;
    profile.bornDate = new Date(date);

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
            console.log("Basic Info Edit success");
            $("#editInfo").modal('hide');
            profile = data;
            loadProfileData();
            loadCards();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Edit error");
            $("#editInfo").modal('hide');
        }
    });

};

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
};

var text_max = 200;

function charsToWrite() {
    var text_length = $('#message-text').val().length;
    $('#count_message').html((text_max - text_length) + ' remaining');
}

$('#message-text').keyup(function () {
    var text_length = $('#message-text').val().length;
    var text_remaining = text_max - text_length;

    $('#count_message').html(text_remaining + ' remaining');
});

function initializeDatePicker() {

    var maxDate = new Date();
    var minDate = new Date();
    maxDate.setFullYear(maxDate.getFullYear() - 18);
    minDate.setFullYear(minDate.getFullYear() - 80);

    $('.datepicker').pickadate({
        selectYears: true,
        selectMonths: true,
        min: minDate,
        max: maxDate,
        today: '',
        close: 'Save',
        format: 'mm/dd/yyyy',
        closeOnSelect: false,
        closeOnClear: false
    });
}

/*Calculate Age*/
function calculateAge(birthDate) {

    birthDate = new Date(birthDate);
    var todayDate = new Date();

    var years = todayDate.getFullYear() - birthDate.getFullYear();
    if (todayDate.getMonth() < birthDate.getMonth() ||
        todayDate.getMonth() === birthDate.getMonth() && todayDate.getDate() < birthDate.getDate()) {
        years--;
    }
    return years;
}

function saveUserLocation(place_id, address) {

    profile.googleLocationID = place_id;
    profile.address = address;

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
            loadCards();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Edit error");
        }
    });

}

function showMainProfilePhoto() {

    var mainPhoto = profile.mainPhoto;
    if (mainPhoto === null) {
        mainPhoto = "../photos/photo-avatar.png";
    } else {
        mainPhoto = "data:image/png;base64," + mainPhoto;
    }
    document.getElementById("main-profile-photo").src = mainPhoto;
}

function getAuthUser() {
    $.ajax({
        url: "/user",
        type: "GET",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            console.log("Auth user is here: " + data.username);
            authUser = data.username;
            if (authUser === profile.username) {
                showEditButtons();
            }
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot see current logged user");
        }
    });

}

function showEditButtons() {
    document.getElementById("editInfoButton").style.display = "inline";
    document.getElementById("editSummaryButton").style.display = "inline";
    document.getElementById("editWhatImDoingButton").style.display = "inline";
    document.getElementById("editGoodAtButton").style.display = "inline";
    document.getElementById("editFavoritesButton").style.display = "inline";
    document.getElementById("pac-input").style.visibility = "visible";
}

function photoAlbum() {
    window.location.replace('/photo?name=' + profile.username);
}

//Create height modal dropdown
function populateHeightDropDown() {

    var min = 120;
    var max = 220;
    var select = document.getElementById('height');
    for (var i = min; i <= max; i++) {
        var opt = document.createElement('option');
        opt.value = i;
        opt.innerHTML = i + " cm";
        select.appendChild(opt);
    }
}

function tags() {

    $('.chips-placeholder').material_chip({
        placeholder: 'Enter a tag',
        secondaryPlaceholder: '+Interest'
    });

    $('.chips').on('chip.add', function(e, chip){
       addInterest(chip.tag);
    });
    
}

function addInterest(newInterest) {

    $.ajax({
        url: '/user/interest?i=' + newInterest.toString(),
        type: "POST",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            console.log("Interest added");
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Add interest error");
        }
    });
}

function getUserInterest() {
    $.ajax({
        url: "/user",
        type: "GET",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            console.log("Auth user is here: " + data.username);
            authUser = data.username;
            if (authUser === profile.username) {
                showEditButtons();
            }
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot see current logged user");
        }
    });

}