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
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;
import techit.gongsimchae.domain.groupbuying.orderitem.repository.OrderItemRepository;
import techit.gongsimchae.domain.groupbuying.reviews.dto.ReviewsReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.reviews.dto.ReviewResDtoWeb;
import techit.gongsimchae.domain.groupbuying.reviews.entity.Review;
import techit.gongsimchae.domain.groupbuying.reviews.entity.SecretStatus;
import techit.gongsimchae.domain.groupbuying.reviews.repository.ReviewRepository;
import techit.gongsimchae.global.dto.AccountDto;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ReviewRepository reviewRepository;
    private final ImageS3Service imageS3Service;
    private final ImageFileRepository imageFileRepository;
    private final OrderItemRepository orderItemRepository;

    public void createReview(AccountDto accountDto, ReviewsReqDtoWeb reviewReqDtoWeb, String uid) {
        User user = userRepository.findByLoginId(accountDto.getLoginId()).orElseThrow(
                () -> new CustomWebException(ErrorMessage.USER_NOT_FOUND)
        );
        Item item = itemRepository.findByUIDAndDeleteStatus(uid, 0).orElseThrow(
                () -> new CustomWebException(ErrorMessage.ITEM_NOT_FOUND)
        );

        OrderItem orderItem = orderItemRepository.findById(reviewReqDtoWeb.getOrderItemId()).orElseThrow(() -> new CustomWebException(ErrorMessage.ORDER_ITEM_NOT_FOUND));

        Review review = reviewRepository.save(new Review(reviewReqDtoWeb, user, item, orderItem));
        imageS3Service.storeFile(reviewReqDtoWeb.getImages(), S3VO.REVIEWS_IMAGE_UPLOAD_DIRECTORY, review);
    }

    public ReviewResDtoWeb getReviews(AccountDto accountDto, String uid, Long orderItemId) {
        Item item = itemRepository.findByUIDAndDeleteStatus(uid, 0).orElseThrow(
                () -> new CustomWebException(ErrorMessage.ITEM_NOT_FOUND)
        );
        Review review = reviewRepository.findByUserIdAndItemAndOrderItemId(accountDto.getId(), item, orderItemId);
        ImageFile imageFile = imageFileRepository.findByReview(review);
        return new ReviewResDtoWeb(review, imageFile.getStoreFilename());
    }

    public void updateReview(AccountDto accountDto, ReviewsReqDtoWeb reviewsReqDtoWeb, String uid, Long orderItemId) {
        Item item = itemRepository.findByUIDAndDeleteStatus(uid, 0).orElseThrow(
                () -> new CustomWebException(ErrorMessage.ITEM_NOT_FOUND)
        );
        Review review = reviewRepository.findByUserIdAndItemAndOrderItemId(accountDto.getId(), item, orderItemId);
        review.update(reviewsReqDtoWeb);
    }

    public List<ReviewResDtoWeb> findReviewListByItemId(Long itemId) {

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new CustomWebException(ErrorMessage.ITEM_NOT_FOUND));

        List<Review> reviewList = reviewRepository.findReviewsByItemAndSecretStatus(item, SecretStatus.NORMAL);
        List<ReviewResDtoWeb> reviewResDtoWebList = new ArrayList<>();

        for (Review review : reviewList) {
            ImageFile imageFile = imageFileRepository.findByReview(review);
            if (imageFile != null) {
                reviewResDtoWebList.add(new ReviewResDtoWeb(review, imageFile.getStoreFilename()));
            } else {
                reviewResDtoWebList.add(new ReviewResDtoWeb(review, null));
            }
        }

        return reviewResDtoWebList;
    }
}
