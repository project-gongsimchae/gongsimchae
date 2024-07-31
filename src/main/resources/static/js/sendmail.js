document.addEventListener('DOMContentLoaded', (event) => {
    document.getElementById('showFullIdButton').addEventListener('click', sendIdToEmail);

    function sendIdToEmail() {
        const email = document.getElementById('userEmail').textContent.trim();
        fetch(`/find/id/send`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: email })
        }).then(response => {
            if (response.ok) {
                alert('전체 아이디가 이메일로 전송되었습니다.');
            } else {
                alert('아이디 전송 중 오류가 발생했습니다.');
            }
        }).catch(error => {
            console.error('Error:', error);
            alert('아이디 전송 중 오류가 발생했습니다.');
        });
    }
});