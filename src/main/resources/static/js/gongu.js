document.addEventListener('DOMContentLoaded', function() {
    // 장바구니 추가 기능
    const addToCartButtons = document.querySelectorAll('.add-to-cart');
    addToCartButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            const itemId = e.target.dataset.itemId;
            // AJAX 요청을 통해 서버에 장바구니 추가 요청을 보냅니다.
            fetch(`/api/cart/add/${itemId}`, { method: 'POST' })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('장바구니에 추가되었습니다.');
                    } else {
                        alert('장바구니 추가에 실패했습니다.');
                    }
                });
        });
    });

    /** 무한 스크롤 **/
    let page = 1;
    window.addEventListener('scroll', function() {
        if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 500) {
            loadMoreItems();
        }
    });

    function loadMoreItems() {
        fetch(`/api/items?page=${page}`)
            .then(response => response.json())
            .then(data => {
                // 새로운 아이템을 페이지에 추가하는 로직
                page++;
            });
    }
});