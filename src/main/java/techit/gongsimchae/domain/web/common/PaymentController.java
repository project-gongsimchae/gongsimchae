package techit.gongsimchae.web;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import techit.gongsimchae.domain.groupbuying.orders.dto.TempOrderItemDto;
import techit.gongsimchae.domain.groupbuying.payment.dto.PaymentVerificationRequest;
import techit.gongsimchae.domain.groupbuying.payment.dto.PaymentVerificationResponse;
import techit.gongsimchae.domain.groupbuying.payment.service.PaymentService;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;


    @PostMapping("/verifyIamport/{imp_uid}")
    public ResponseEntity<?> verifyIamportPost(
            @AuthenticationPrincipal PrincipalDetails userDetails,
            @PathVariable(value = "imp_uid") String impUid,
            @RequestBody PaymentVerificationRequest request,
            HttpSession session) {
        try {
            List<TempOrderItemDto> tempOrderItems = (List<TempOrderItemDto>) session.getAttribute("tempOrderItems");
            IamportResponse<Payment> verificationResult = paymentService.verifyAndProcessPayment(impUid, request.getMerchantUid(), request.getAmount(), tempOrderItems,userDetails.getAccountDto().getId());
            if (verificationResult.getResponse().getStatus().equals("paid")) {
                return ResponseEntity.ok(new PaymentVerificationResponse("success", "결제가 성공적으로 완료되었습니다."));
            } else {
                return ResponseEntity.badRequest().body(new PaymentVerificationResponse("fail", "결제 상태가 올바르지 않습니다."));
            }
        } catch (IamportResponseException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentVerificationResponse("error", "결제 검증 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
}
