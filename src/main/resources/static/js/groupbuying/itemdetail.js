document.addEventListener('DOMContentLoaded', function() {
    const mainImage = document.getElementById('main-image');
    const thumbnails = document.querySelectorAll('.thumbnail');

    thumbnails.forEach(thumbnail => {
        thumbnail.addEventListener('click', function() {
            // 현재 선택된 썸네일을 강조 표시하고, 이전 선택된 썸네일은 강조 해제
            thumbnails.forEach(thumb => thumb.classList.remove('active'));
            this.classList.add('active');

            // 큰 이미지 변경
            const newSrc = this.querySelector('.thumbnail-image').src;
            mainImage.src = newSrc;
        });
    });


    const optionSelect = document.getElementById('product-options');
    const totalPriceEl = document.getElementById('total-price');
    const selectedOptionsEl = document.getElementById('selected-options');
    const addToCartForm = document.getElementById('addToCartForm');

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const updateDetails = () => {
        const selectedOption = optionSelect.options[optionSelect.selectedIndex];
        const optionValue = selectedOption.value;
        const originalPrice  = selectedOption ? parseInt(selectedOption.getAttribute('data-price')) : 0;
        const discountPrice = selectedOption ? parseInt(selectedOption.getAttribute('data-discount-price')) : 0;
        const discountRate = selectedOption ? parseInt(selectedOption.getAttribute('data-discount-rate')) : 0;
        const optionText = selectedOption ? selectedOption.text : '없음';

        if (optionValue) {
            let optionDetail = document.getElementById(`option-${optionValue}`);
            if (!optionDetail) {
                optionDetail = document.createElement('div');
                optionDetail.id = `option-${optionValue}`;
                optionDetail.className = 'option-detail';
                optionDetail.innerHTML = `
                <div class="option">
                    <div class="option-info">
                        <span class="option-name">${optionText}</span>
                        <div class="quantity-controls">
                            <button type="button" class="quantity-decrease">-</button>
                            <span class="quantity-value">1</span>
                            <button type="button" class="quantity-increase">+</button>
                        </div>
                    </div>
                    <div class="price-display">
                        <span class="original-price">${originalPrice.toLocaleString()}원</span>
                        <div class="discount-info">
                            <span class="discount-rate">${discountRate}%</span>
                            <span class="discount-price">${discountPrice.toLocaleString()}원</span>
                        </div>
                    </div>
                </div>
                <button type="button" class="remove-option-btn" aria-label="옵션 삭제">x</button>
            `;
                selectedOptionsEl.appendChild(optionDetail);
                updateTotalPrice();
            }
        }
    };

    const updateTotalPrice = () => {
        const optionDetails = document.querySelectorAll('.option-detail');
        let totalDiscount = 0;
        optionDetails.forEach(detail => {
            const quantity = parseInt(detail.querySelector('.quantity-value').textContent);
            const discountPrice = parseInt(detail.querySelector('.discount-price').textContent.replace(/[^0-9]/g, ''));
            totalDiscount += discountPrice * quantity;
        });
        totalPriceEl.innerHTML = `
            <span class="total-discount-price">${totalDiscount.toLocaleString()}원</span>
        `;
    };

    const changeQuantity = (event) => {
        if (event.target.classList.contains('quantity-increase') || event.target.classList.contains('quantity-decrease')) {
            const optionDetail = event.target.closest('.option-detail');
            const quantityValueEl = optionDetail.querySelector('.quantity-value');
            let quantity = parseInt(quantityValueEl.textContent);

            if (event.target.classList.contains('quantity-increase')) {
                quantity++;
            } else if (event.target.classList.contains('quantity-decrease') && quantity > 1) {
                quantity--;
            }

            quantityValueEl.textContent = quantity;
            updateTotalPrice();
        }
    };

    const removeOption = (event) => {
        if (event.target.classList.contains('remove-option-btn')) {
            const optionDetail = event.target.closest('.option-detail');
            optionDetail.remove();
            updateTotalPrice();
        }
    };

    optionSelect.addEventListener('change', updateDetails);
    document.addEventListener('click', function(event) {
        if (event.target.closest('.quantity-increase') || event.target.closest('.quantity-decrease')) {
            changeQuantity(event);
        } else if (event.target.closest('.remove-option-btn')) {
            removeOption(event);
        }
    });


    const addToCart = (event) => {
        event.preventDefault();

        const selectedOptions = document.querySelectorAll('.option-detail');
        if (selectedOptions.length === 0) {
            alert('최소 하나의 옵션을 선택해주세요.');
            return;
        }

        const cartItems = Array.from(selectedOptions).map(option => {
            const optionId = option.id.replace('option-', '');
            const quantity = parseInt(option.querySelector('.quantity-value').textContent);
            return { itemOptionId: optionId, quantity: quantity };
        });

        fetch(addToCartForm.action, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ cartItems: cartItems })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('장바구니에 상품이 추가되었습니다.');
                } else {
                    alert('장바구니 추가에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('장바구니 추가 중 오류가 발생했습니다.');
            });
    };

    optionSelect.addEventListener('change', updateDetails);
    addToCartForm.addEventListener('submit', addToCart);

    // 모달 관련 코드 추가
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.innerHTML = `
        <span class="modal-close">&times;</span>
        <div class="modal-content">
            <img src="" alt="리뷰 이미지">
        </div>
    `;
    document.body.appendChild(modal);

    const modalImg = modal.querySelector('img');
    const modalClose = modal.querySelector('.modal-close');

    document.querySelectorAll('.review-img').forEach(img => {
        img.addEventListener('click', function() {
            modal.style.display = 'block';
            modalImg.src = this.src;
        });
    });

    modalClose.addEventListener('click', function() {
        modal.style.display = 'none';
    });

    modal.addEventListener('click', function(event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
});
