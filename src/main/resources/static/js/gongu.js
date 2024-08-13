document.addEventListener('DOMContentLoaded', function() {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const addToCartButtons = document.querySelectorAll('.add-to-cart');
    addToCartButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const itemId = this.getAttribute('data-item-id');
            addToCart(itemId);
        });
    });

    function addToCart(itemId) {
        fetch(`/product/cart/add/${itemId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ count: 1 })
        })
            .then(response => {
                if (response.ok) {
                    return response.text();
                }
                throw new Error('Network response was not ok.');
            })
            .then(data => {
                alert('상품이 장바구니에 추가되었습니다.');
            })
            .catch(error => {
                console.error('Error:', error);
                alert('장바구니 추가에 실패했습니다.' + error.message);
            });
    }

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