function sendVerificationCode() {
    const email = document.getElementById('email').value;
    console.log(email); // 이메일이 콘솔에 찍히는지 확인
    fetch(`/emails/verification-requests?email=${email}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'  // Content-Type 설정 확인
        }
    }).then(response => {
        if (response.ok) {
            alert('인증 코드가 이메일로 전송되었습니다.');
        } else {
            alert('인증 코드 전송에 실패했습니다.');
        }
    }).catch(error => {
        console.error('Error:', error);  // 에러 발생 시 로그 출력
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