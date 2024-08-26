package techit.gongsimchae.domain.portion.subdivision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubSearchDto {

    private Boolean onSale;
    private String sort;
}
