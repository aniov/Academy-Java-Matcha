window.ondragover = function (e) {
    e.preventDefault()
}
window.ondrop = function (e) {
    e.preventDefault();
    upload(e.dataTransfer.files[0]);
}

function upload(file) {
    if (!file || !file.type.match(/image.*/))
        return;
    var fileData = new FormData();
    fileData.append("image", file);

    $.ajax({
        url: "/user/upload-photo",
        type: "POST",
        contentType: false,
        data: fileData,
        processData: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
                //setTimeout(document.body.className = "uploading", 2000);
        },
        success: function (data, textStatus, jqXHR) {

            console.log("Basic Info Edit success");
            document.body.className = "uploaded";
            addUploadedPicture(data);
            //document.getElementById("circle-spinner").className = "preloader-wrapper";
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Edit error");
            document.body.className = "error-upload";
            //setTimeout(document.body.className = "error-upload", 3000);
        }
    });
}