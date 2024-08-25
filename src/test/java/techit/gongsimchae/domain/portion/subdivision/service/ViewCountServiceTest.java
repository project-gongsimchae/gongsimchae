package techit.gongsimchae.domain.portion.subdivision.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.global.util.ViewVO;

import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ViewCountServiceTest {

    @Autowired
    ViewCountService viewCountService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("view count")
    void view_count_test() throws Exception {

        String subdivisionId = "zset test";
        String cookieName = "zset cookie";

        viewCountService.incrementViewCount(subdivisionId, cookieName);
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().rangeWithScores(ViewVO.TOTAL_VIEWS, 0, -1);
        Assertions.assertThat(typedTuples).isNotEmpty();

        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();
        if (iterator.hasNext()) {
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            Assertions.assertThat(next.getScore()).isEqualTo(1.0);
            System.out.println("next = " + next.getValue());
        }


    }

}