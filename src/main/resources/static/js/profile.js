window.onload = function(){
    $.get("../static/navbar.html", function(data){
        $("#nav-placeholder").replaceWith(data);
    });
    $.get("../static/footer.html", function(data){
        $("#footer-placeholder").replaceWith(data);
    });
}