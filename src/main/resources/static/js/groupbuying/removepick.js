$(document).ready(function() {
    $('.custom-picklist-delete-button').on('click', function() {
        var itemId = $(this).data('item-id');

        // CSRF 토큰이 필요하다면, 아래와 같이 추가
        var csrfToken = $('meta[name="csrf-token"]').attr('content');

        $.ajax({
            url: '/mypage/pick/' + itemId,
            type: 'POST',  // 또는 'DELETE' (서버 설정에 따라 다름)
            headers: {
                'X-CSRF-TOKEN': csrfToken // CSRF 토큰 포함
            },
            success: function(response) {
                // 삭제 성공 후 페이지 새로 고침
                location.reload();
            },
            error: function(xhr, status, error) {
                // 오류 처리
                alert('삭제 중 오류가 발생했습니다.');
            }
        });
    });
});