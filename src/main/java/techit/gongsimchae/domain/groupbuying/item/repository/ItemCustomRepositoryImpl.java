package techit.gongsimchae.domain.groupbuying.item.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemSearchForm;

import java.util.List;

import static techit.gongsimchae.domain.common.imagefile.entity.QImageFile.imageFile;
import static techit.gongsimchae.domain.groupbuying.item.entity.QItem.item;

@RequiredArgsConstructor
public class ItemCustomRepositoryImpl implements ItemCustomRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<ItemRespDtoWeb> findItemsByKeyword(ItemSearchForm itemSearchForm, Pageable pageable) {
        List<ItemRespDtoWeb> result = queryFactory.select(Projections.fields(ItemRespDtoWeb.class,
                        item.id, item.name, item.intro, item.originalPrice, item.discountRate,
                        item.pointAccumulationRate, item.groupBuyingQuantity, item.groupBuyingLimitTime,
                        item.deleteStatus, item.UID, item.cumulativeSalesVolume, item.reviewCount, item.createDate,
                        item.updateDate, imageFile))
                .from(item)
                .leftJoin(item.imageFiles, imageFile)
                .where(searchCondition(itemSearchForm))
                .orderBy(sortTypes(itemSearchForm))
                .fetch();

        int size = queryFactory.select(item)
                .from(item)
                .leftJoin(item.imageFiles, imageFile)
                .where(searchCondition(itemSearchForm))
                .fetch().size();


        return new PageImpl<>(result, pageable, size);
    }


    private BooleanExpression searchCondition(ItemSearchForm itemSearchForm) {
        if (itemSearchForm != null && itemSearchForm.getKeyword() != null) {
            String keyword = itemSearchForm.getKeyword();
            return item.name.contains(keyword).or(item.intro.contains(keyword));
        }
        return null;
    }

    private OrderSpecifier<?> sortTypes(ItemSearchForm itemSearchForm) {
        Integer sortType = itemSearchForm.getSortType();
        if (sortType != null) {
            switch (sortType) {
                case 4:
                    return item.originalPrice.asc();
                case 5:
                    return item.originalPrice.desc();
                case 1:
                default:
                    return item.createDate.desc();
            }
        }
        return item.createDate.desc(); // 기본값: 생성일 내림차순
    }
}
