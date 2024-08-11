document.getElementById("notificationButton")?.addEventListener("click", function(event) {
    event.preventDefault(); // 기본 클릭 동작 방지

    // AJAX 요청을 통해 알림 데이터 가져오기
    fetch('/mypage/notifications')
        .then(response => response.text())
        .then(html => {
            document.getElementById("notificationContent").innerHTML = html; // 서버에서 받아온 HTML을 모달 내용에 삽입
            var notificationModal = new bootstrap.Modal(document.getElementById('notificationModal'));
            notificationModal.show(); // 모달 표시
        })
        .catch(error => console.error('Error:', error));
});

// 페이지 로드 시 main 함수 실행
window.onload = function() {
    console.log('Page loaded');
    // 여기에 main 함수에 대한 코드를 추가할 수 있습니다.
};

// 모달이 열린 후 AJAX 요청을 통해 알림을 읽은 상태로 업데이트
$('#notificationModal').on('shown.bs.modal', function (e) {
    console.log('Modal is shown');
    $.ajax({
        url: '/mypage/read/notifications',
        type: 'GET',
        success: function (response) {
            console.log('Notifications marked as read');
        },
        error: function (xhr, status, error) {
            console.error('Error marking notifications as read:', error);
        }
    });
});

// 모달이 닫힐 때 페이지 새로고침
$('#notificationModal').on('hidden.bs.modal', function (e) {
    console.log('Modal is hidden');
    window.location.reload(); // 페이지 새로고침
});