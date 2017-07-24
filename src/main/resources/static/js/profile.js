window.onload = function(){
    $.get("../static/navbar.html", function(data){
        $("#nav-placeholder").replaceWith(data);
    });
    $.get("../static/footer.html", function(data){
        $("#footer-placeholder").replaceWith(data);
    });
    $.get("../static/googlemap.html", function(data){
        $("#google-placeholder").replaceWith(data);
    });
}