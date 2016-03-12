// JavaScript source code
geocoder = new google.maps.Geocoder();
var latlng = new google.maps.LatLng(-34.397, 150.644);
var mapOptions = {
    zoom: 8,
    center: latlng
}
map = new google.maps.Map();

    var address = "28 knox lane, manalapan, nj";
    geocoder.geocode( { 'address': address}, function(results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
            print(results[0].latlng);
        } else {
            alert("Geocode was not successful for the following reason: " + status);
        }
    });