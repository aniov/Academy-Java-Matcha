let allMessages = [];
let totalPages = 0;
let pageNumber = 0;
let processing;

window.onload = function () {
    new WOW().init();
    $.get("navbar.html", function (data) {
        $("#nav-placeholder").replaceWith(data);
    });
    $.get("footer.html", function (data) {
        $("#footer-placeholder").replaceWith(data);
    });
    navBar();
    userMessages();
    /*Web Socket Connect*/
    connect();

};

$(document).ready(function () {

    let win = $(window);

    win.scroll(function () {
        //We make sure that only 1 page will be requested
        if (processing) {
            return false;
        }
        if (win.scrollTop() + win.height() >= $(document).height() - 400) {
            processing = true;
            pageNumber++;
            if (pageNumber < totalPages) {
                userMessages();
            }
        }
    });
});

function userMessages() {

    $.ajax({
        url: "/user/all-messages?page=" + pageNumber,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('meta[name="_csrf"]').attr('content'));
        },
        success: function (data, textStatus, jqXHR) {
            allMessages = data.content;
            totalPages = data.totalPages;
            createMessageCards();
            getUserMessages();
        },
        error: function (data, textStatus, jqXHR) {
            console.log("Cannot read received messages");
        }
    });
}

function createMessageCards() {

    let elements = [];

    for (let i = 0; i < allMessages.length; i++) {

        let colorCardReceive = 'aqua-gradient';
        let from_to = 'from';
        let sent_received = 'received: ';
        let toRight = '';
        let to_text = '';
        let received_pos = '';

        if (allMessages[i].sentMessage === true) {
            colorCardReceive = 'purple-gradient';
            from_to = 'to';
            sent_received = 'sent: ';
            toRight = ' justify-content-end mr-4';
            to_text = ' text-right';
            received_pos = 'pull-right';
        }

        let card =
            $('<div>', {
                class: 'card testimonial-card col-lg-3 ml-4 view overlay hm-white-light',
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

        let text =
            $('<div>', {class: 'col-lg-5'}).append(
                $('<h3>', {class: 'post-title font-bold blue-text' + to_text, text: from_to})
            ).append(
                $('<p>', {class: 'my-4', text: allMessages[i].message})
            ).append(
                $('<hr>', {class: 'extra-margin my-0'})
            ).append(
                $('<span>', {
                    text: sent_received + allMessages[i].created,
                    class: received_pos
                })
            );

        if (allMessages[i].sentMessage === true) {
            let tmp = card;
            card = text;
            text = tmp;
        }

        elements[i] =
            $('<div>', {
                class: 'row mt-3 wow fadeIn' + toRight,
                'data-wow-delay': '0.3s'
            }).append(card).append(text);


        $(document.getElementById('message-cards')).append(elements[i]).append(
            $('<hr>', {class: 'extra-margin my-0'})
        );
    }

    processing = false;
}