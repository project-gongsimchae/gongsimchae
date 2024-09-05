document.addEventListener('DOMContentLoaded', function() {
    const deliveryModal = document.getElementById('deliveryModal');
    const modal = new bootstrap.Modal(deliveryModal);

    const IMP = window.IMP;
    IMP.init("imp63604178");

    const orderInfo = document.getElementById('order-info').dataset;

    const orderButton = document.getElementById('orderButton');
    orderButton.addEventListener('click', requestPay);

    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

    window.openDeliveryModal = function() {
        modal.show();
    };

    window.updateDeliveryInfo = function() {
        const name = document.getElementById('recipientName').value;
        const phone = document.getElementById('recipientPhone').value;
        const address = document.getElementById('recipientAddress').value;

        document.getElementById('displayRecipientName').textContent = name;
        document.getElementById('displayRecipientPhone').textContent = phone;
        document.getElementById('displayRecipientAddress').textContent = address;

        modal.hide();
    };

    deliveryModal.addEventListener('hidden.bs.modal', function () {
        const originalName = document.getElementById('displayRecipientName').textContent;
        const originalPhone = document.getElementById('displayRecipientPhone').textContent;
        const originalAddress = document.getElementById('displayRecipientAddress').textContent;

        document.getElementById('recipientName').value = originalName;
        document.getElementById('recipientPhone').value = originalPhone;
        document.getElementById('recipientAddress').value = originalAddress;
    });

    const couponSelect = document.getElementById('couponSelect');
    if (couponSelect) {
        couponSelect.addEventListener('change', function() {
            updateTotalAmount();
        });
    }

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

        pointInput.value = Math.min(availablePoints, parseInt(pointInput.value) || 0);
    }

    function updateTotalAmount() {
        const originalTotal = parseInt(document.getElementById('originalTotalAmount').textContent.replace(/[^0-9]/g, ''));
        const productDiscount = parseInt(document.getElementById('totalDiscountAmount').textContent.replace(/[^0-9]/g, ''));
        let couponDiscount = 0;

        const selectedCoupon = couponSelect.options[couponSelect.selectedIndex];
        if (selectedCoupon && selectedCoupon.value !== "") {
            const discountRate = parseFloat(selectedCoupon.dataset.discountRate);
            const maxDiscount = parseInt(selectedCoupon.dataset.maxDiscount);

            couponDiscount = Math.min((originalTotal - productDiscount) * (discountRate / 100), maxDiscount);
        }

        const finalAmount = Math.max(0, originalTotal - productDiscount - couponDiscount);

        document.getElementById('couponDiscountAmount').textContent = couponDiscount.toLocaleString() + '원';
        document.getElementById('finalTotalAmount').textContent = finalAmount.toLocaleString() + '원';
    }

    function requestPay() {
        const selectedPaymentMethod = document.querySelector('input[name="paymentMethod"]:checked');
        if (!selectedPaymentMethod) {
            alert("결제 수단을 선택해주세요.");
            return;
        }

        const pgProvider = selectedPaymentMethod.dataset.pgProvider;
        const finalAmount = parseInt(document.getElementById('finalTotalAmount').textContent.replace(/[^0-9]/g, ''));
        const selectedCouponId = document.getElementById('couponSelect').value;

        $.ajax({
            url: '/order/create',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                couponId: selectedCouponId,
                finalAmount: finalAmount
            }),
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(merchantUid) {
                const paymentData = {
                    pg: pgProvider,
                    pay_method: "card",
                    merchant_uid: merchantUid,
                    name: "공심채 주문",
                    amount: finalAmount,
                    buyer_email: orderInfo.buyerEmail,
                    buyer_name: orderInfo.buyerName,
                    buyer_tel: orderInfo.buyerTel,
                    buyer_addr: orderInfo.buyerAddr,
                    buyer_postcode: orderInfo.buyerPostcode,
                    m_redirect_url: "http://localhost:8081/order/complete",
                };

                IMP.request_pay(paymentData, function(rsp) {
                    if (rsp.success) {
                        verifyPayment(rsp.imp_uid, rsp.merchant_uid, rsp.paid_amount);
                    } else {
                        cancelOrder(rsp.imp_uid,merchantUid)
                        alert("결제에 실패하였습니다. " + rsp.error_msg);
                    }
                });
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert("주문 생성에 실패했습니다. 다시 시도해주세요.");
            }
        });
    }

    function verifyPayment(impUid, merchantUid, amount) {
        $.ajax({
            url: `/verifyIamport/${impUid}`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                merchantUid: merchantUid,
                amount: amount
            }),
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(data) {
                if (data.status === "success") {
                    alert("결제가 완료되었습니다.");
                    window.location.href = "/main";
                } else {
                    cancelOrder(impUid,merchantUid)
                    alert("결제 검증에 실패했습니다. 관리자에게 문의해주세요.");
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                cancelOrder(impUid,merchantUid)
                alert("결제 검증 중 오류가 발생했습니다. 관리자에게 문의해주세요. 오류: " + errorThrown);
            }
        });
    }

    function cancelOrder(impUid,merchantUid) {
        return $.ajax({
            url: "/order/cancel",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                impUid: impUid,
                merchantUid: merchantUid
            }),
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        });
    }

    updateTotalAmount();
});