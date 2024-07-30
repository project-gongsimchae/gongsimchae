function sendVerificationCode() {
    const email = document.getElementById('email').value;
    fetch(`/emails/verification-requests?email=${email}`, {
        method: 'POST'
    }).then(response => {
        if (response.ok) {
            alert('인증 코드가 이메일로 전송되었습니다.');
        } else {
            alert('인증 코드 전송에 실패했습니다.');
        }
    });
}

function verifyCode() {
    const email = document.getElementById('email').value;
    const code = document.getElementById('verificationCode').value;
    fetch(`/emails/verifications?email=${email}&code=${code}`)
        .then(response => {
            if (response.ok) {
                isEmailVerified = true;
                document.getElementById('verificationMessage').innerText = '인증에 성공했습니다.';
            } else {
                isEmailVerified = false;
                document.getElementById('verificationMessage').innerText = '인증에 실패했습니다.';
            }
        });
}

function validateForm() {
    if (!isEmailVerified) {
        alert('이메일 인증을 완료해주세요.');
        return false;
    }
    return true;
}