// CSRF 토큰과 헤더를 메타 태그에서 가져옵니다.
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

// 모든 사용자 피드백 컨테이너에 대해 반복합니다.
document.querySelectorAll('.custom-unique-user-feedback').forEach(userFeedback => {
    const form = userFeedback.querySelector('form');
    const radioButtons = form.querySelectorAll('input[type="radio"]');
    const confirmation = userFeedback.querySelector('.custom-unique-confirmation');

    // 모든 라디오 버튼에 이벤트 리스너를 추가합니다.
    radioButtons.forEach(radio => {
        radio.addEventListener('change', () => {
            const formData = new FormData(form);

            fetch(form.action, {
                method: 'POST',
                body: formData,
                headers: {
                    [csrfHeader]: csrfToken
                }
            })
                .then(response => {
                    if (response.ok) {
                        form.style.display = 'none';
                        confirmation.style.display = 'block';
                    } else {
                        throw new Error('제출 중 오류가 발생했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert(error.message);
                    // 오류 발생 시 선택 취소
                    radio.checked = false;
                });
        });
    });
});
