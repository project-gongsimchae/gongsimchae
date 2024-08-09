// 로그인 성공 시 호출될 함수
// 로그인 후 세션에 저장된 사용자 정보를 활용하여 SSE 구독 설정
const eventSource = new EventSource("http://localhost:8081/subscribe");

eventSource.addEventListener('sse', async function (event) {
    console.log(event.data);

    const data = JSON.parse(event.data);
    console.log(data.content)
    console.log(data.body)
    console.log(data.title)
    console.log(data)

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