package techit.gongsimchae.domain.groupbuying.item.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemCardResDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemSearchForm;

import java.util.List;

import static techit.gongsimchae.domain.common.imagefile.entity.QImageFile.imageFile;
import static techit.gongsimchae.domain.groupbuying.item.entity.QItem.item;

@RequiredArgsConstructor
public class ItemCustomRepositoryImpl implements ItemCustomRepository {
    private final JPAQueryFactory queryFactory;

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

        Long size = queryFactory.select(item.count())
                .from(item)
                .fetchOne();
        return new PageImpl<>(results, pageable, size.intValue());
    }
}
