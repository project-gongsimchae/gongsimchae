document.addEventListener('DOMContentLoaded', function() {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    // 수량 조절 기능
    const quantityContainers = document.querySelectorAll('.quantity');
    quantityContainers.forEach(container => {
        const minusBtn = container.querySelector('.minus-btn');
        const plusBtn = container.querySelector('.plus-btn');
        const input = container.querySelector('input');

        if (minusBtn && plusBtn && input) {
            minusBtn.addEventListener('click', function() {
                if (input.value > 1) {
                    input.value = parseInt(input.value) - 1;
                    updateCartItem(container);
                }
            });

            plusBtn.addEventListener('click', function() {
                input.value = parseInt(input.value) + 1;
                updateCartItem(container);
            });

            input.addEventListener('change', function() {
                if (this.value < 1) {
                    this.value = 1;
                }
                updateCartItem(container);
            });
        }
    });

    // 장바구니 아이템 업데이트
    function updateCartItem(container) {
        const cartItem = container.closest('.cart-item');
        if (!cartItem) {
            console.error('Cart item not found');
            return;
        }
        const itemOptionId = cartItem.dataset.itemOptionId;
        const quantity = container.querySelector('input').value;

        fetch(`/product/cart/update/${itemOptionId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ quantity: quantity })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    const priceElement = cartItem.querySelector('.price');
                    if (priceElement) {
                        priceElement.textContent = `${data.totalPrice.toLocaleString()}원`;
                    }
                    updateCartSummary();
                } else {
                    alert('수량 업데이트에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('수량 업데이트 중 오류가 발생했습니다.');
            });
    }

    // 전체 선택 기능
    const selectAllCheckbox = document.getElementById('select-all-checkbox');
    const itemCheckboxes = document.querySelectorAll('.item-checkbox');

    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', function() {
            itemCheckboxes.forEach(checkbox => {
                checkbox.checked = this.checked;
            });
            updateSelectAllText();
            updateCartSummary();
        });
    }

    itemCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            updateSelectAllText();
            updateCartSummary();
        });
    });

    function updateSelectAllText() {
        const checkedCount = document.querySelectorAll('.item-checkbox:checked').length;
        const totalCount = itemCheckboxes.length;
        const selectAllSpan = document.getElementById('select-all-text');
        if (selectAllSpan) {
            selectAllSpan.textContent = `전체선택 (${checkedCount}/${totalCount})`;
        }
        if (selectAllCheckbox) {
            selectAllCheckbox.checked = checkedCount === totalCount;
        }
    }

    // 장바구니 요약 정보 업데이트
    function updateCartSummary() {
        const checkedItems = document.querySelectorAll('.item-checkbox:checked');
        const itemOptionIds = Array.from(checkedItems).map(checkbox => checkbox.closest('.cart-item').dataset.itemOptionId);

        fetch('/product/cart/summary', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ itemOptionIds: itemOptionIds })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                document.getElementById('total-price').textContent = `${data.totalPrice.toLocaleString()}원`;
                document.getElementById('total-discount').textContent = `-${data.totalDiscount.toLocaleString()}원`;
                document.getElementById('total-payment-price').textContent = `${data.totalPaymentPrice.toLocaleString()}원`;
            })
            .catch(error => {
                console.error('Error updating cart summary:', error);
                alert('장바구니 정보 업데이트 중 오류가 발생했습니다.');
            });
    }

    // 삭제 버튼 이벤트 리스너
    document.querySelectorAll('.delete-item').forEach(button => {
        button.addEventListener('click', function() {
            const cartItem = this.closest('.cart-item');
            const itemOptionId = cartItem.dataset.itemOptionId;

            fetch(`/product/cart/remove/${itemOptionId}`, {
                method: 'POST',
                headers: {
                    [csrfHeader]: csrfToken
                }
            })
                .then(response => {
                    if (response.ok) {
                        cartItem.remove();
                        updateSelectAllText();
                        updateCartSummary();
                    } else {
                        alert('상품 삭제에 실패했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('상품 삭제 중 오류가 발생했습니다.');
                });
        });
    });

    // 주문 버튼 클릭 이벤트 핸들러
    document.getElementById('order-button').addEventListener('click', function(e) {
        e.preventDefault();
        const checkedBoxes = document.querySelectorAll('input[name="selectedItemOptionId"]:checked');
        if (checkedBoxes.length === 0) {
            alert('주문할 상품을 선택해주세요.');
            return;
        }
        document.getElementById('orderForm').submit();
    });

    // 초기 장바구니 요약 정보 업데이트
    updateCartSummary();
});