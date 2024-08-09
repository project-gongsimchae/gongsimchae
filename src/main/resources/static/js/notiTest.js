document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // 기본 폼 제출 막기

    // 폼 데이터를 가져오기
    const formData = new FormData(this);

    // CSRF 토큰 가져오기
    const csrfToken = document.querySelector('input[name="_csrf"]').value;

    try {
        // 로그인 요청 보내기
        const response = await fetch(this.action, {
            method: 'POST',
            body: formData,
            headers: {
                'X-CSRF-TOKEN': csrfToken // CSRF 토큰을 헤더에 추가
            },
            credentials: 'same-origin' // 쿠키를 포함하여 요청
        });

        if (response.ok) {
            // 로그인 성공 시 SSE 설정
            handleLoginSuccess();
            // 원하는 경우 로그인 성공 시 페이지 리다이렉트
            window.location.href = '/main'; // 대시보드로 리다이렉트
        } else {
            console.error('로그인 실패');
            document.getElementById('loginError').classList.remove('d-none');
        }
    } catch (error) {
        console.error('로그인 요청 중 오류 발생:', error);
    }
});

function handleLoginSuccess() {
    const eventSource = new EventSource("http://localhost:8081/subscribe");

    eventSource.addEventListener('sse', async function (event) {
        console.log(event.data);

        const data = JSON.parse(event.data);
        console.log(data.content);
        console.log(data.body);
        console.log(data.title);
        console.log(data);

        // 알림을 표시하는 함수
        const showNotification = () => {
            const notification = new Notification('알림', {
                body: data
            });

            setTimeout(() => {
                notification.close();
            }, 10 * 1000); // 10초 후에 알림 닫기

            notification.addEventListener('click', () => {
                window.open(data.url, '_blank');
            });
        };

        // 알림 권한 확인 및 요청
        if (Notification.permission === 'granted') {
            showNotification();
        } else if (Notification.permission === 'default') {
            const permission = await Notification.requestPermission();
            if (permission === 'granted') {
                showNotification();
            }
        }
    });

    // SSE 연결 오류 처리
    eventSource.addEventListener('error', function (event) {
        console.error('SSE 연결 오류:', event);
    });
}