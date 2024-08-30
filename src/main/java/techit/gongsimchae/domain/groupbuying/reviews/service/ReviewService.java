package techit.gongsimchae.domain.groupbuying.reviews.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.imagefile.entity.S3VO;
import techit.gongsimchae.domain.common.imagefile.repository.ImageFileRepository;
import techit.gongsimchae.domain.common.imagefile.service.ImageS3Service;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.repository.ItemRepository;
import techit.gongsimchae.domain.groupbuying.reviews.dto.ReviewsReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.reviews.dto.ReviewResDtoWeb;
import techit.gongsimchae.domain.groupbuying.reviews.entity.Review;
import techit.gongsimchae.domain.groupbuying.reviews.repository.ReviewRepository;
import techit.gongsimchae.global.dto.AccountDto;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ReviewRepository reviewRepository;
    private final ImageS3Service imageS3Service;
    private final ImageFileRepository imageFileRepository;

    public void createReview(AccountDto accountDto, ReviewsReqDtoWeb reviewReqDtoWeb, String uid) {
        User user = userRepository.findByLoginId(accountDto.getLoginId()).orElseThrow(
                () -> new CustomWebException(ErrorMessage.USER_NOT_FOUND)
        );
        Item item = itemRepository.findByUIDAndDeleteStatus(uid, 0).orElseThrow(
                () -> new CustomWebException(ErrorMessage.ITEM_NOT_FOUND)
        );
        Review review = reviewRepository.save(new Review(reviewReqDtoWeb, user, item));
        imageS3Service.storeFile(reviewReqDtoWeb.getImages(), S3VO.REVIEWS_IMAGE_UPLOAD_DIRECTORY, review);
    }

    public ReviewResDtoWeb getReviews(AccountDto accountDto, String uid) {
        Item item = itemRepository.findByUIDAndDeleteStatus(uid, 0).orElseThrow(
                () -> new CustomWebException(ErrorMessage.ITEM_NOT_FOUND)
        );
        Review review = reviewRepository.findByUserIdAndItem(accountDto.getId(), item);
        ImageFile imageFile = imageFileRepository.findByReview(review);
        return new ReviewResDtoWeb(review, imageFile.getStoreFilename());
    }

    public void updateReview(AccountDto accountDto, ReviewsReqDtoWeb reviewsReqDtoWeb, String uid) {
        Item item = itemRepository.findByUIDAndDeleteStatus(uid, 0).orElseThrow(
                () -> new CustomWebException(ErrorMessage.ITEM_NOT_FOUND)
        );
        Review review = reviewRepository.findByUserIdAndItem(accountDto.getId(), item);
        review.update(reviewsReqDtoWeb);
    }
}
