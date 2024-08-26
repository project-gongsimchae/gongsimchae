package techit.gongsimchae.global.batch.views;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import static techit.gongsimchae.global.util.ViewVO.TOTAL_VIEWS;

@RequiredArgsConstructor
public class SubItemWriter implements ItemWriter<ViewCountDto> {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void write(Chunk<? extends ViewCountDto> chunk) throws Exception {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

            for (ViewCountDto viewCountDto : chunk) {
                int viewCount = viewCountDto.getViewCount();
                String subdivisionId = viewCountDto.getSubdivisionId();
                zSetOps.add(TOTAL_VIEWS, subdivisionId, viewCount);
            }
            return null;
        });
    }
}
