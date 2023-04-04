'use strict';

document.write("<script\n" +
    "  src=\"https://code.jquery.com/jquery-3.6.1.min.js\"\n" +
    "  integrity=\"sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=\"\n" +
    "  crossorigin=\"anonymous\"></script>")


var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

// roomId 파라미터 가져오기
const roomNo = document.querySelector('#roomNo').value.trim();
console.log(roomNo);

function connect() {
    username = document.querySelector('#name').value.trim();

    // 연결하고자하는 Socket 의 endPoint
    var socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
    console.log("연결 완료");
}

function onConnected() {
    // sub 할 url => /sub/chat/room/roomId 로 구독한다
    stompClient.subscribe('/sub/chat/room/' + roomNo, onMessageReceived);
    console.log("구독 완료");
    connectingElement.classList.add('hidden');

    $.ajax({
        type: 'POST',
        url: `/chat/message/${roomNo}`,
        success: function (data) {
            for (let i = 0; i < data.length; i++) {
                const chat = data[i];
                appendChatMessage(chat);
            }
        },
        error: function (err) {
            console.error(err);
        }
    });
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

// 메시지 전송때는 JSON 형식을 메시지를 전달한다.
function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            "chatRoom": {"roomNo": roomNo},
            "sender": username,
            "message": messageInput.value,
        };

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

// 메시지를 받을 때도 마찬가지로 JSON 타입으로 받으며,
// 넘어온 JSON 형식의 메시지를 parse 해서 사용한다.
function onMessageReceived(payload) {
    //console.log("payload 들어오냐? :"+payload);
    var chat = JSON.parse(payload.body);
    appendChatMessage(chat);
}

function appendChatMessage(chat) {
    const messageElement = document.createElement('li');
    messageElement.classList.add('chat-message');

    const avatarElement = document.createElement('i');
    const avatarText = document.createTextNode(chat.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(chat.sender);
    messageElement.appendChild(avatarElement);

    const usernameElement = document.createElement('span');
    const usernameText = document.createTextNode(chat.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

    const contentElement = document.createElement('p');
    const messageText = document.createTextNode(chat.message);
    contentElement.appendChild(messageText);
    messageElement.appendChild(contentElement);

    const timeElement = document.createElement('p');
    timeElement.classList.add('chat-time');
    const timeText = document.createTextNode(`${new Date(chat.createdAt).toLocaleDateString()} ${new Date(chat.createdAt).toLocaleTimeString()}`);
    timeElement.appendChild(timeText);
    messageElement.appendChild(timeElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

window.onload = function () {
    connect();
};
messageForm.addEventListener('submit', sendMessage, true)