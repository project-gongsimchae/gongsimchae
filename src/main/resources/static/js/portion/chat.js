'use strict';

var chatPage = document.querySelector('#chat-page');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
// username은 HTML에서 Thymeleaf를 통해 설정됩니다.

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

// roomId 파라미터 가져오기
const url = new URL(location.href).searchParams;
const roomId = url.get('roomId');

function connect() {
    // 연결하고자 하는 Socket 의 endPoint
    var socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    // 이전 메시지 불러오기
    // sub 할 url => /sub/chat/room/roomId 로 구독한다
    stompClient.subscribe('/sub/chat/room/' + roomId, onMessageReceived);

    // 서버에 username 을 가진 유저가 들어왔다는 것을 알림
    // /pub/chat/enterUser 로 메시지를 보냄
    stompClient.send("/pub/chat/enterUser",
        {},
        JSON.stringify({
            "roomId": roomId,
            sender: username,
            type: 'ENTER'
        })
    );
    loadPreviousMessages(roomId);

    console.log("Connected and sent ENTER message");
    connectingElement.classList.add('hidden');
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

// 메시지 전송 함수
function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    var now = new Date();
    var hours = String(now.getHours()).padStart(2, '0'); // 두 자리 수로 포맷팅
    var minutes = String(now.getMinutes()).padStart(2, '0'); // 두 자리 수로 포맷팅
    var time = hours + ':' + minutes; // "HH:mm" 형식으로 시간 포맷팅

    if (messageContent && stompClient) {
        var chatMessage = {
            "roomId": roomId,
            sender: username,
            message: messageInput.value,
            time: time,
            type: 'TALK'
        };

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

// 나가기 메시지 전송 및 페이지 리디렉션 함수
function sendLeaveMessage() {
    var now = new Date();
    var hours = String(now.getHours()).padStart(2, '0');
    var minutes = String(now.getMinutes()).padStart(2, '0');
    var time = hours + ':' + minutes;

    if (stompClient) {
        var chatMessage = {
            "roomId": roomId,
            sender: username,
            message: '', // 빈 메시지로 설정
            time: time,
            type: 'LEAVE'
        };

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
    }

    // 페이지 리디렉션
    window.location.href = '/portioning';
}

// 서버에서 이전 메시지 불러오기
function loadPreviousMessages(roomId) {
    $.ajax({
        type: "GET",
        url: `/chat/previous/${roomId}`,
        success: function (messages) {
            if (messages && messages.length > 0) {
                messages.forEach(message => {
                    displayMessage(message);
                });
            }
        },
        error: function () {
            console.error('이전 메시지 불러오기 실패');
        }
    });
}

// 서버에서 메시지 수신 시 호출
function onMessageReceived(payload) {
    var chat = JSON.parse(payload.body);
    console.log("Received message:", chat);
    displayMessage(chat);
}

function displayMessage(chat) {
    var messageElement = document.createElement('li');

    if (chat.type === 'ENTER') {
        messageElement.classList.add('event-message');
        chat.content = chat.sender + "님이 입장하셨습니다.";
        getUserList();
    } else if (chat.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        chat.content = chat.sender + '님이 퇴장하셨습니다.';
        getUserList();
    } else {
        messageElement.classList.add('chat-message');

        if (chat.sender === username) {
            messageElement.classList.add('my-message');
        } else {
            messageElement.classList.add('other-message');

            var avatarElement = document.createElement('i');
            var avatarText = document.createTextNode(chat.sender[0]);
            avatarElement.appendChild(avatarText);
            avatarElement.style['background-color'] = getAvatarColor(chat.sender);
            messageElement.appendChild(avatarElement);

            var usernameElement = document.createElement('span');
            var usernameText = document.createTextNode(chat.sender);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
        }
    }

    var contentElement = document.createElement('p');

    if (chat.s3DataUrl != null) {
        var imgElement = document.createElement('img');
        imgElement.setAttribute("src", chat.s3DataUrl);
        imgElement.setAttribute("width", "250");
        imgElement.setAttribute("height", "250");


        contentElement.appendChild(imgElement);
    } else {
        var messageText = document.createTextNode(chat.message);
        contentElement.appendChild(messageText);
    }

    // 시간 정보를 별도의 span 태그로 추가
    var timeElement = document.createElement('span');
    timeElement.classList.add('message-time');
    timeElement.textContent = formatTime(chat.createDate); // createDate 사용

    // 메시지 내용과 시간 정보를 분리하여 추가
    var timeWrapper = document.createElement('span');
    timeWrapper.classList.add('time-wrapper');
    timeWrapper.appendChild(timeElement);

    // 메시지 내용 뒤에 시간 요소를 추가합니다.
    messageElement.appendChild(contentElement);
    messageElement.appendChild(timeWrapper); // 시간 정보를 메시지 내용 뒤에 추가

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

/// 파일 업로드 부분 ////
function uploadFile(){
    var file = $("#file")[0].files[0];
    var formData = new FormData();
    formData.append("file",file);
    formData.append("roomId", roomId);

    console.log(file)

    // ajax 로 multipart/form-data 를 넘겨줄 때는
    //         processData: false,
    //         contentType: false
    // 처럼 설정해주어야 한다.

    // 동작 순서
    // post 로 rest 요청한다.
    // 1. 먼저 upload 로 파일 업로드를 요청한다.
    // 2. upload 가 성공적으로 완료되면 data 에 upload 객체를 받고,
    // 이를 이용해 chatMessage 를 작성한다.
    $.ajax({
        type : 'POST',
        url : '/chat/s3/upload',
        data : formData,
        processData: false,
        contentType: false
    }).done(function (data){
        console.log("업로드 성공")

        var chatMessage = {
            "roomId": roomId,
            sender: username,
            message: username+"님의 파일 업로드",
            type: 'TALK',
            s3DataUrl : data.storeFilename, // Dataurl
            "fileName": data.originalFilename, // 원본 파일 이름
        };

        // 해당 내용을 발신한다.
        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
    }).fail(function (error){
        alert(error);
    })
}

// createDate를 HH:mm 형식으로 변환하는 함수
function formatTime(createDate) {
    var date = new Date(createDate);
    var hours = String(date.getHours()).padStart(2, '0');
    var minutes = String(date.getMinutes()).padStart(2, '0');
    return hours + ':' + minutes;
}

// 아바타 색상 설정 함수
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

// 유저 리스트 받기
function getUserList() {
    const $list = $("#list");

    $.ajax({
        type: "GET",
        url: "/chat/userlist",
        data: {
            "roomId": roomId
        },
        success: function (data) {
            var users = "";
            for (let i = 0; i < data.length; i++) {
                users += "<li class='dropdown-item'>" + data[i] + "</li>";
            }
            $list.html(users);
        }
    });
}

// 페이지 로드 시 자동으로 연결
window.addEventListener('load', connect);

messageForm.addEventListener('submit', sendMessage, true);

// 나가기 버튼 클릭 시
document.querySelector('#leaveButton').addEventListener('click', sendLeaveMessage);
