package techit.gongsimchae.global.batch.views;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static techit.gongsimchae.global.util.ViewVO.*;

@RequiredArgsConstructor
@Slf4j
public class SubItemReader implements ItemStreamReader<Map.Entry<String, Double>> {
    private final RedisTemplate<String, Object> redisTemplate;
    private Set<ZSetOperations.TypedTuple<Object>> typedTuples;
    private Iterator<ZSetOperations.TypedTuple<Object>> iterator;
    private final String CURRENT_KEY = "current_key";
    private int currentNumber = 0;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        typedTuples = redisTemplate.opsForZSet().rangeWithScores(TOTAL_VIEWS, 0, -1);
        iterator = typedTuples.iterator();

        if (executionContext.containsKey(CURRENT_KEY)) {
            currentNumber = executionContext.getInt(CURRENT_KEY);
        }

        for (int i = 0; i < currentNumber && iterator.hasNext(); i++) {
            System.out.println("iterator.hasNext = " + iterator.hasNext());
            iterator.next();
        }

    }

    @Override
    public Map.Entry<String, Double> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (iterator != null && iterator.hasNext()) {
            currentNumber++;
            ZSetOperations.TypedTuple<Object> tuple = iterator.next();
            return new AbstractMap.SimpleEntry<>((String) tuple.getValue(), tuple.getScore());
        }
        return null;
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putInt(CURRENT_KEY, currentNumber);
    }
}
