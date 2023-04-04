'use strict';

document.write(
    "<script src=\"https://code.jquery.com/jquery-3.6.1.min.js\" integrity=\"sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=\" crossorigin=\"anonymous\"></script>"
);


const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const messageArea = document.querySelector('#messageArea');
const connectingElement = document.querySelector('.connecting');
const colors = [
    '#2196F3',
    '#32c787',
    '#00BCD4',
    '#ff5652',
    '#ffc107',
    '#ff85af',
    '#FF9800',
    '#39bbb0'
];
let stompClient;
let username;

// roomId 파라미터 가져오기
const roomNo = document.querySelector('#roomNo').value.trim();
console.log(roomNo);

function connect() {
    username = document.querySelector('#name').value.trim();
    // 연결하고자하는 Socket 의 endPoint
    const socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    stompClient.subscribe(`/sub/chat/room/${roomNo}`, onMessageReceived);
    connectingElement.classList.add('hidden');
    loadChatHistory();
}

function loadChatHistory() {
    $.ajax({
        type: 'POST',
        url: `/chat/message/${roomNo}`,
        success: function (data) {
            data.forEach(appendChatMessage);
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
    const messageContent = messageInput.value.trim();
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

function onMessageReceived(payload) {
    const chat = JSON.parse(payload.body);
    appendChatMessage(chat);
}

function appendChatMessage(chat) {
    const messageElement = document.createElement('li');
    messageElement.classList.add('chat-message');

    const avatarElement = document.createElement('i');
    const avatarText = document.createTextNode(chat.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style.backgroundColor = getAvatarColor(chat.sender);
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