package techit.gongsimchae.domain.groupbuying.item.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.participate.entity.QParticipate;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemAdminSearchForm;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemCardResDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemRespDtoWeb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static techit.gongsimchae.domain.common.imagefile.entity.QImageFile.imageFile;
import static techit.gongsimchae.domain.common.participate.entity.QParticipate.*;
import static techit.gongsimchae.domain.groupbuying.category.entity.QCategory.*;
import static techit.gongsimchae.domain.groupbuying.item.entity.QItem.item;

@RequiredArgsConstructor
public class ItemCustomRepositoryImpl implements ItemCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ItemRespDtoWeb> findItemsWithCriteria(ItemAdminSearchForm form, Pageable pageable) {
        List<ItemRespDtoWeb> results = queryFactory.select(Projections.fields(ItemRespDtoWeb.class,
                        item.id, item.name, item.intro, item.originalPrice, item.discountRate,
                        item.pointAccumulationRate, item.groupBuyingQuantity, item.groupBuyingLimitTime,
                        item.createDate, category.name.as("categoryName"), item.deleteStatus,
                        category.categoryStatus))
                .from(item)
                .join(item.category, category)
                .where(adminSearch(form))
                .orderBy(adminOrder(form))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (ItemRespDtoWeb result : results) {
            Long count = queryFactory.select(participate.count())
                    .from(participate)
                    .join(participate.item, item)
                    .where(item.id.eq(result.getId()))
                    .fetchOne();
            result.setParticipateCount(count);
        }

        Long size = queryFactory.select(item.count())
                .from(item)
                .where(adminSearch(form))
                .fetchOne();
        return new PageImpl<>(results, pageable, size);
    }

    private BooleanExpression adminSearch(ItemAdminSearchForm form) {
        List<BooleanExpression> expressions = new ArrayList<>();

        if (form.getItemIsDeleted() != null) {
            expressions.add(item.deleteStatus.eq(form.getItemIsDeleted()));
        }
        if (form.getItemIsClosed() != null) {
            if (form.getItemIsClosed().equals(1)) {
                expressions.add(item.groupBuyingLimitTime.before(LocalDateTime.now()));
            } else{
                expressions.add(item.groupBuyingLimitTime.after(LocalDateTime.now()));
            }
        }
        if (form.getCategories() != null && !form.getCategories().isEmpty()) {
            expressions.add(category.id.in(form.getCategories()));
        }
        return  expressions.stream()
                .reduce(BooleanExpression::and)
                .orElse(Expressions.TRUE);
    }

    private OrderSpecifier<?> adminOrder(ItemAdminSearchForm form) {
        if(form.getRemainingTime() != null) {
            if (form.getRemainingTime()) {
                return item.groupBuyingLimitTime.desc();
            }
        }
        return item.createDate.desc();
    }



    @Override
    public Page<ItemCardResDtoWeb> findRecentItems(Pageable pageable) {
        List<ItemCardResDtoWeb> results = queryFactory.select(Projections.fields(ItemCardResDtoWeb.class,
                        item.id, item.name, item.intro, item.originalPrice, item.discountRate,
                        item.pointAccumulationRate, item.groupBuyingLimitTime, item.groupBuyingQuantity,
                        item.UID, item.cumulativeSalesVolume, item.reviewCount))
                .from(item)
                .orderBy(item.createDate.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        for (ItemCardResDtoWeb result : results) {
            List<ImageFile> imageFiles = queryFactory.select(imageFile)
                    .from(imageFile)
                    .join(imageFile.item, item)
                    .where(item.id.eq(result.getId()))
                    .fetch();

            if (imageFiles != null) {
                result.setItemBannerImage(imageFiles.get(0).getStoreFilename());
            }
        }

        for (ItemCardResDtoWeb result : results) {
            Long count = queryFactory.select(participate.count())
                    .from(participate)
                    .join(participate.item, item)
                    .where(item.id.eq(result.getId()))
                    .fetchOne();
            result.setParticipateCount(count);
        }

        Long size = queryFactory.select(item.count())
                .from(item)
                .fetchOne();
        return new PageImpl<>(results, pageable, size.intValue());
    }
}
