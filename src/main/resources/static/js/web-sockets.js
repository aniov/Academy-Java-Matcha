var stompClient_Likes = null;
var stompClient_Messages = null;
var stompClient_logged = null;

function connect() {

    stompClient_Likes = Stomp.over(new SockJS('/websocket'));
    stompClient_Messages = Stomp.over(new SockJS('/websocket'));
    stompClient_logged = Stomp.over(new SockJS('/websocket'));

    informUserLikes();
    informUserReceivedMessages();
    informWhoIsOnline();

}

function sendName() {
    //  stompClient.send("/app/hello", {}, JSON.stringify({'name': 'Alfonso'}));
}

function informUserLikes() {

    stompClient_Likes.connect({}, function (frame) {
        stompClient_Likes.subscribe('/user/queue/like', function (like) {

            var response = JSON.parse(like.body);

            if (response.like === true) {
                toastr["success"](response.username + " like you");
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

            }
        });
    });

}