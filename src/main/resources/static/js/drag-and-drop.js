function dragAndDrop() {
    window.ondragover = function (e) {
        e.preventDefault()
    };
    window.ondrop = function (e) {
        e.preventDefault();
        upload(e.dataTransfer.files[0]);
    };
}

function upload(file) {
    if (!file || !file.type.match(/image.*/))
        return;
    let fileData = new FormData();
    fileData.append("image", file);

    $.ajax({
        url: "/user/upload-photo",
        type: "POST",
        contentType: false,
        data: fileData,
        processData: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {

            console.log("Photo uploaded success");
            document.body.className = "uploaded";
            addUploadedPicture(data);
            //document.getElementById("circle-spinner").className = "preloader-wrapper";
        },
        error: function (xhr, status) {
            console.log("Upload photo error" + xhr.responseText);
            document.body.className = "error-upload";
            if (xhr.status === 507) {

                document.getElementById('modal-response').innerHTML = parseListMessages(xhr.responseJSON.messages);
                $("#errorPhotosModal").modal('show');
            }
            //setTimeout(document.body.className = "error-upload", 3000);
        }
    });
}