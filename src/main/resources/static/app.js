var stompClient = null;



function connect() {
    var socket = new SockJS('/websocket-example');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/user', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

showGreeting = (e) =>{
}