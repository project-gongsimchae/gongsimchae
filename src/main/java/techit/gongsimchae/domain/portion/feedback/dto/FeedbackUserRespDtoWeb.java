package techit.gongsimchae.domain.portion.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.user.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackUserRespDtoWeb implements Serializable {
    private Long id;
    private String nickname;

    public static List<FeedbackUserRespDtoWeb> toDto(List<User> users) {
        List<FeedbackUserRespDtoWeb> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(toDto(user));
        }
        return dtos;
    }

    public static FeedbackUserRespDtoWeb toDto(User user) {
        FeedbackUserRespDtoWeb feedbackUserRespDtoWeb = new FeedbackUserRespDtoWeb();
        feedbackUserRespDtoWeb.setId(user.getId());
        feedbackUserRespDtoWeb.setNickname(user.getNickname());
        return feedbackUserRespDtoWeb;
    }

}
