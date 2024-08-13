// 페이지 언로드 직전에 스크롤 위치 저장
window.addEventListener('beforeunload', function() {
    sessionStorage.setItem('scrollPosition', window.pageYOffset);
});

// 페이지 로드 후 스크롤 위치 복원
window.addEventListener('load', function() {
    const scrollPosition = sessionStorage.getItem('scrollPosition');
    if (scrollPosition !== null) {
        // 페이지 로드가 완료되면 스크롤 위치를 복원
        setTimeout(function() {
            window.scrollTo(0, parseInt(scrollPosition, 10));
            sessionStorage.removeItem('scrollPosition'); // 복원 후 위치 삭제
        }, 0); // 페이지 로드 후 즉시 수행
    }
});

document.addEventListener('DOMContentLoaded', function () {
    // 요소 선택
    const joinButton = document.getElementById('joinButton');
    const modal = document.getElementById('modal');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.getElementById('cancelButton');

    // 참여하기 버튼 클릭 시 모달 표시
    joinButton.addEventListener('click', function () {
        modal.style.display = 'flex';
    });

    // 모달에서 확인 버튼 클릭 시 페이지 이동
    confirmButton.addEventListener('click', function () {
        window.location.href = `/portioning/${subdivisionRespDto.UID}/join`;
    });

    // 모달에서 취소 버튼 클릭 시 모달 숨기기
    cancelButton.addEventListener('click', function () {
        modal.style.display = 'none';
    });

});
