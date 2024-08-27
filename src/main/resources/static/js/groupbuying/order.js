document.addEventListener('DOMContentLoaded', function() {
    // 모달 요소
    const deliveryModal = document.getElementById('deliveryModal');

    // 모달 인스턴스 생성
    const modal = new bootstrap.Modal(deliveryModal);

    // 수정 버튼 클릭 시 모달 열기
    window.openDeliveryModal = function() {
        modal.show();
    };

    // 배송 정보 업데이트 함수
    window.updateDeliveryInfo = function() {
        const name = document.getElementById('recipientName').value;
        const phone = document.getElementById('recipientPhone').value;
        const address = document.getElementById('recipientAddress').value;

        // 화면에 표시되는 정보 업데이트
        document.getElementById('displayRecipientName').textContent = name;
        document.getElementById('displayRecipientPhone').textContent = phone;
        document.getElementById('displayRecipientAddress').textContent = address;

        // 모달 닫기
        modal.hide();
    };

    // 모달이 닫힐 때 입력 필드 초기화
    deliveryModal.addEventListener('hidden.bs.modal', function () {
        const originalName = document.getElementById('displayRecipientName').textContent;
        const originalPhone = document.getElementById('displayRecipientPhone').textContent;
        const originalAddress = document.getElementById('displayRecipientAddress').textContent;

        document.getElementById('recipientName').value = originalName;
        document.getElementById('recipientPhone').value = originalPhone;
        document.getElementById('recipientAddress').value = originalAddress;
    });

    // 쿠폰 선택 처리
    const couponSelect = document.getElementById('couponSelect');
    if (couponSelect) {
        couponSelect.addEventListener('change', function() {
            updateTotalAmount();
        });
    }

    // 포인트 입력 처리
    const pointInput = document.getElementById('pointInput');
    const availablePoints = parseInt(document.getElementById('availablePoints').textContent) || 0;

    if (pointInput) {
        pointInput.addEventListener('input', function() {
            let inputPoints = parseInt(this.value);
            if (isNaN(inputPoints) || inputPoints < 0) {
                this.value = 0;
            } else if (inputPoints > availablePoints) {
                this.value = availablePoints;
            }
            updateTotalAmount();
        });

        // 초기값 설정
        pointInput.value = Math.min(availablePoints, parseInt(pointInput.value) || 0);
    }

    // 총 결제 금액 업데이트 함수
    function updateTotalAmount() {
        const originalTotal = parseInt(document.getElementById('originalTotalAmount').textContent.replace(/[^0-9]/g, ''));
        let couponDiscount = 0;
        let pointDiscount = 0;

        // 쿠폰 할인 계산
        const selectedCoupon = couponSelect.options[couponSelect.selectedIndex];
        if (selectedCoupon && selectedCoupon.value !== "") {
            couponDiscount = parseInt(selectedCoupon.textContent.match(/\d+/)[0]);
        }

        // 포인트 사용 계산
        pointDiscount = parseInt(pointInput.value) || 0;

        // 총 할인 금액
        const totalDiscount = couponDiscount + pointDiscount + totalDiscount;

        // 최종 금액 계산
        const finalAmount = Math.max(0, originalTotal - totalDiscount);

        // 화면 업데이트
        document.getElementById('couponDiscountAmount').textContent = couponDiscount.toLocaleString() + '원';
        document.getElementById('pointDiscountAmount').textContent = pointDiscount.toLocaleString() + '원';
        document.getElementById('totalDiscountAmount').textContent = totalDiscount.toLocaleString() + '원';
        document.getElementById('finalTotalAmount').textContent = finalAmount.toLocaleString() + '원';
    }

    // 초기 총 금액 설정
    updateTotalAmount();
});

// 주문 제출 전 최종