// 카테고리 상태를 확인하고 경고 메시지를 띄우는 함수
function checkCategoryStatus(button) {
    const form = button.closest('form');
    const categoryStatus = form.getAttribute('data-category-status');

    if (categoryStatus === '1') { // 카테고리가 삭제된 상태라면
        alert("해당 카테고리가 존재하지 않아 복구할 수 없습니다.");
    } else {
        form.submit(); // 카테고리가 삭제되지 않은 경우 폼을 제출하여 복구 처리
    }
}
