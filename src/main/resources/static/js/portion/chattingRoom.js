document.addEventListener('DOMContentLoaded', function () {
    const chatInput = document.getElementById('chatInput');
    const sendButton = document.getElementById('sendButton');
    const chatWindow = document.getElementById('chatWindow');

    function addMessage(message, isUser = false) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('message', 'd-flex');
        if (isUser) {
            messageElement.classList.add('user');
        }

        const messageBubble = document.createElement('div');
        messageBubble.classList.add('message-bubble', 'p-2', 'rounded');

        const username = document.createElement('span');
        username.classList.add('username', 'fw-bold', 'd-block');
        username.innerText = isUser ? '나' : '상대';

        const text = document.createElement('span');
        text.classList.add('text');
        text.innerText = message;

        messageBubble.appendChild(username);
        messageBubble.appendChild(text);
        messageElement.appendChild(messageBubble);
        chatWindow.appendChild(messageElement);

        chatWindow.scrollTop = chatWindow.scrollHeight;
    }

    sendButton.addEventListener('click', function () {
        const message = chatInput.value.trim();
        if (message) {
            addMessage(message, true);
            chatInput.value = '';
            // 여기서 실제 메시지 전송 로직을 추가해야 함 (예: 서버로 전송)
        }
    });

    chatInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            sendButton.click();
        }
    });

    // 샘플 메시지 추가 (참가자 메시지)
    addMessage('안녕하세요!', false);
});
