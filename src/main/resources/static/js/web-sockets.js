var stompClient_Likes = null;
var stompClient_Messages = null;
var stompClient_logged = null;
var stompClient_inform_all_logged = null;

function connect() {

    stompClient_Likes = Stomp.over(new SockJS('/websocket'));
    stompClient_Messages = Stomp.over(new SockJS('/websocket'));
    stompClient_logged = Stomp.over(new SockJS('/websocket'));
    stompClient_inform_all_logged = Stomp.over(new SockJS('/websocket'));

    informUserLikes();
    informUserReceivedMessages();
    informWhoIsOnline();
    allUsersLogStatus();
}

function sendName() {
    //  stompClient.send("/app/hello", {}, JSON.stringify({'name': 'Alfonso'}));
}

function informUserLikes() {

    stompClient_Likes.connect({}, function (frame) {
        stompClient_Likes.subscribe('/user/queue/like', function (like) {

            var response = JSON.parse(like.body);

            if (response.like === true) {
                toastr["success"](response.username + " likes you");
            } else {
                toastr["error"](response.username + " doesn't like you anymore");
            }
            //Basically we re-draw the cards with the new info
            //We make sure we are in main page first
            if (window.location.toString().match('/main')) {
                getAuthProfile();
            }


        });
    });
}

function informUserReceivedMessages() {

    stompClient_Messages.connect({}, function (frame) {
        stompClient_Messages.subscribe('/user/queue/message', function (like) {

            var response = JSON.parse(like.body);
            toastr["info"]("You received a new message from " + response.username + ": " + response.message);

            //Basically we re-display navBar with new info
            navBar();

            if (window.location.toString().match('/message')) {
                //
            }
        });
    });
}

function informWhoIsOnline() {

    stompClient_logged.connect({}, function (frame) {
        stompClient_logged.subscribe('/user/queue/online', function (like) {

            var response = JSON.parse(like.body);
            if (response.online === true) {
                toastr["info"](response.username + " has logged in");
            } else {
                toastr["info"](response.username + " has logged out");
            }
            //Basically we re-display navBar with new info
            //We make sure we are in main page first
            if (window.location.toString().match('/main')) {
                document.getElementById('online-circle-' + response.username).className += ' text-success';
            }
        });
    });
}

function allUsersLogStatus() {

    stompClient_inform_all_logged.connect({}, function (frame) {
        stompClient_inform_all_logged.subscribe('/user/queue/online-all', function (like) {

            var response = JSON.parse(like.body);
            if (window.location.toString().match('/main')) {

                var toggle = $('#online-circle-' + response.username);
                var element = document.getElementById('online-circle-' + response.username);
                toggle.tooltip('dispose');

                if (response.online === true) {
                    element.className = 'fa fa-circle pull-left text-success';
                    element.title = 'Online';
                } else {
                    element.className = 'fa fa-circle pull-left text-muted';
                    element.title = 'Offline';
                }
                toggle.tooltip('show');
            }
        });
    });
}