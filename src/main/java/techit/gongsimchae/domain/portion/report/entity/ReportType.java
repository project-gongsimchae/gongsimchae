package techit.gongsimchae.domain.portion.report.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportType {
    PROMOTION("홍보"),
    PROHIBITED_ITEMS("거래금지 물품"),
    PROFESSIONAL_SELLER("전문 판매업자 같아요"),
    DISPUTE("거래 중 분쟁이 발생했어요"),
    SCAM("사기인 것 같아요"),
    OTHER("기타 부적절한 행위");

    String description;
}