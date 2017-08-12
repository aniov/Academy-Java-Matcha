var allMessages = [];
window.onload = function () {

    $.get("navbar.html", function (data) {
        $("#nav-placeholder").replaceWith(data);
    });
    $.get("footer.html", function (data) {
        $("#footer-placeholder").replaceWith(data);
    });
    navBar();
    userMessages();
}

function userMessages() {

    $.ajax({
        url: "/user/all-messages",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            console.log(data);
            allMessages = data;
            createMessageCards();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read received messages");
        }
    });
}

function createMessageCards() {

    var elements = [];

    for (var i = allMessages.length - 1; i >= 0; i--) {

        var colorCardReceive = 'aqua-gradient';
        var from_to = 'from';
        var sent_received = 'received: ';
        var toRight = '';
        var to_text = '';

        if (allMessages[i].sentMessage === true) {
            colorCardReceive = 'purple-gradient';
            from_to = 'to';
            sent_received = 'sent: ';
            toRight = ' justify-content-end mr-4';
            to_text = ' text-right';
        }

        var card =
            $('<div>', {
                class: 'card testimonial-card col-lg-3 ml-4 view overlay hm-white-light'
            }).append(
                $('<a>', {
                    href: '/profile?name=' + allMessages[i].username
                }).append(
                    $('<div>', {class: 'mask'})
                ).append(
                    $('<div>', {class: 'card-up lighten-1 ' + colorCardReceive})
                ).append(
                    $('<div>', {class: 'avatar'}).append(
                        $('<img>', {
                            class: 'rounded-circle',
                            src: "data:image/png;base64," + allMessages[i].profilePhoto
                        })
                    )
                ).append(
                    $('<div>', {class: 'card-body'}).append(
                        $('<h4>', {class: 'card-title black-text', text: allMessages[i].username})
                    )
                )
            );

        var text =
            $('<div>', {class: 'col-lg-5'}).append(
                $('<h3>', {class: 'post-title font-bold blue-text' + to_text, text: from_to})
            ).append(
                $('<p>', {class: 'my-4', text: allMessages[i].message})
            ).append(
                $('<hr>', {class: 'extra-margin my-0'})
            ).append(
                $('<span>', {text: sent_received + allMessages[i].created})
            );


        if (allMessages[i].sentMessage === true) {
            var tmp = card;
            card = text;
            text = tmp;
        }

        elements[i] =
            $('<div>', {
                class: 'row mt-3 wow fadeIn' + toRight,
                'data-wow-delay': '0.2s',
            }).append(card).append(text);


        $(document.getElementById('message-cards')).append(elements[i]).append(
            $('<hr>', {class: 'extra-margin my-0'})
        );
    }
}