var stompClient = null;
var current_user = null;
function connect() {
    var socket = new SockJS('/web_socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        current_user = frame.headers['user-name'];

        stompClient.subscribe("/user/queue/messages", function (message) {
                $('#messages_area').append(JSON.parse(message.body).text).append('<br>');
        });

        stompClient.subscribe("/user/queue/chats", function (chat) {
             console.log(JSON.parse(chat.body));
            $('#messages_area').html('');
             var messages = JSON.parse(chat.body).messages;
            for (let i = 0; i < messages.length; i++) {
                $('#messages_area').append(messages[i].text + '<br>');
            }
        });

        stompClient.subscribe("/topic/messages", function (message) {
            $('#messages_area').append(JSON.parse(message.body).text).append('<br>');
        });
    });
}

connect();

$(function () {
    $('#send_msg_btn').click(function () {
        stompClient.send('/app/send_to',{},JSON.stringify( {to:$('#recipient_select').val(), from:current_user, text:$('#message').val()}));
        $('#messages_area').append($('#message').val()).append('<br>');
    });

    $('#recipient_select').change(function (){
        stompClient.send('/app/get_chat',{},JSON.stringify( {to:$('#recipient_select').val(), from:current_user}));
    });


});

