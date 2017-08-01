var place_id;

function setPlaceId(placeId) {
    this.place_id = placeId;
}

function initMap() {

    var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: -33.8688, lng: 151.2195},
        zoom: 13
    });
    var input = document.getElementById('pac-input');

    var autocomplete = new google.maps.places.Autocomplete(
        input, {placeIdOnly: true});

   // autocomplete.bindTo('bounds', map);
   // map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

    var infowindow = new google.maps.InfoWindow();
    var infowindowContent = document.getElementById('infowindow-content');
    infowindow.setContent(infowindowContent);

    var marker = new google.maps.Marker({
        map: map
    });
    marker.addListener('click', function() {
        infowindow.open(map, marker);
    });

    if (place_id) {
        setLocation(place_id, map, marker, infowindowContent, infowindow);
    }

    autocomplete.addListener('place_changed', function() {
        infowindow.close();
        place = autocomplete.getPlace();

        console.log(autocomplete.getPlace().place_id);
        if (!place.place_id) {
            return;
        }
        setLocation(place.place_id, map, marker, infowindowContent, infowindow);
        saveUserLocation(place.place_id, place.name);
    });
}

function setLocation(place_id, map, marker, infowindowContent, infowindow) {
    var geocoder = new google.maps.Geocoder;
    geocoder.geocode({'placeId': place_id}, function(results, status) {
        if (status !== 'OK') {
            window.alert('Geocoder failed due to: ' + status);
            return;
        }
        map.setZoom(11);
        map.setCenter(results[0].geometry.location);
        // Set the position of the marker using the place ID and location.
        marker.setPlace({
            placeId: place_id,
            location: results[0].geometry.location
        });
        marker.setVisible(true);
        //infowindowContent.children['place-id'].textContent = place_id;
        infowindowContent.children['place-address'].textContent = results[0].formatted_address;

        infowindow.open(map, marker);
    });
}
