package techit.gongsimchae.domain.portion.subdivision.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;
import techit.gongsimchae.global.util.CalculateTime;

import java.time.Duration;
import java.util.*;

import static techit.gongsimchae.global.util.ViewVO.*;

@Service
@RequiredArgsConstructor
public class ViewCountService {

    private final RedisTemplate<String, Object> redisTemplate;


    private boolean isSubdivisionViewedByUser(String subdivisionId, String viewCookie) {
        return redisTemplate.opsForValue()
                .setIfAbsent(generateKey(subdivisionId, viewCookie), "view-count", Duration.ofSeconds(CalculateTime.getSecondsUntilEndOfDay()));

    }

    /**
     * 한명의 회원은 하루에 한번씩만 (자정이 지난 후를 하루라고 친다)
     * 조회수를 올릴 수 있다
     */

    public void incrementViewCount(String subdivisionId, String viewCookie) {

        if(isSubdivisionViewedByUser(subdivisionId, viewCookie)) {
            redisTemplate.opsForHash().increment(SUBDIVISION_NAME + DAY, subdivisionId + TODAY, 1);
            redisTemplate.opsForZSet().incrementScore(TOTAL_VIEWS,subdivisionId,1);
        }
        redisTemplate.expire(SUBDIVISION_NAME + DAY, Duration.ofSeconds(CalculateTime.getSecondsUntilEndOfDay() + 60));
    }

    /**
     * 오늘 하루 조회수를 조회
     */

    public Integer getDailySubViewCount(String subdivisionId) {
        return (Integer) redisTemplate.opsForHash().get(SUBDIVISION_NAME + DAY, subdivisionId + TODAY);
    }

    /**
     * 모든 글 하루 조회수를 조회
     */

    public Map<Object, Object> getDailyViewCount() {
        return redisTemplate.opsForHash().entries(SUBDIVISION_NAME + DAY);
    }



    /**
     * 일주일 조회수를 조회
     */

    public Integer getWeeklyViewCount(String subdivisionId) {
        String pattern = subdivisionId + ":*";
        Set<String> keys = new HashSet<>();
        Cursor<String> scan = redisTemplate.scan(ScanOptions.scanOptions().match(pattern).build());
        scan.forEachRemaining(keys::add);

        Integer totalViews = 0;
        for (String key : keys) {
            Integer viewCount = (Integer) redisTemplate.opsForValue().get(key);
            if (viewCount != null) {
                totalViews += viewCount;
            }
        }
        return totalViews;
    }

    /**
     * 특정글 전체 조회수 조회하기
     */
    public Integer getSubdivisionViewCount(String subdivisionId) {

        Double score = redisTemplate.opsForZSet().score(TOTAL_VIEWS, subdivisionId);
        return score != null ? score.intValue() : 0;
    }


    /**
     * 전체 글 조회수 조회하기
     */
    public Map<String, Integer> getTotalViewsCount() {
        Map<String, Integer> viewsCount = new HashMap<>();
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet().rangeWithScores(TOTAL_VIEWS, 0, -1);
        if (tuples != null) {
            for (ZSetOperations.TypedTuple<Object> tuple : tuples) {
                viewsCount.put((String) tuple.getValue(), tuple.getScore().intValue());
            }
        }
        return viewsCount;
    }

    /**
     * 조회수 기준으로 Ranking된 subdivision 찾기
     */

    /**
     * 하루 기준으로 조회수가 높은 상위 50 subdivision 찾기
     */
    public Set<Object> getViewRankForDay(String subdivisionId) {
        Integer dailyViewCount = getDailySubViewCount(subdivisionId);
        redisTemplate.opsForZSet().incrementScore(SUBDIVISION_NAME + DAY, subdivisionId, dailyViewCount);
        Set<Object> result = redisTemplate.opsForZSet().reverseRange(SUBDIVISION_NAME + DAY, 0, 49);
        redisTemplate.opsForZSet().remove(SUBDIVISION_NAME+DAY);
        return result;
    }

    /**
     * 일주일 기준으로 조회수가 높은 상위 50 subdivision 찾기
     */

    public Set<Object> getViewRankForWeek(String subdivisionId) {
        Integer WeekViewCount = getWeeklyViewCount(subdivisionId);
        redisTemplate.opsForZSet().incrementScore(SUBDIVISION_NAME + WEEK, subdivisionId, WeekViewCount);
        Set<Object> result = redisTemplate.opsForZSet().reverseRange(SUBDIVISION_NAME + WEEK, 0, 49);
        redisTemplate.opsForZSet().remove(SUBDIVISION_NAME+WEEK);
        return result;
    }


    private String generateKey(String subdivisionId, String viewCookie) {
        return subdivisionId + ":" + viewCookie;
    }

    public void setSubdivisionViews(List<SubdivisionRespDto> content) {

        for (SubdivisionRespDto subdivisionRespDto : content) {
            Integer viewCount = getSubdivisionViewCount(subdivisionRespDto.getUID());
            subdivisionRespDto.setViews(viewCount);
        }
    }
}
