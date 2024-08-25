package techit.gongsimchae.global.batch.views;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.portion.subdivision.repository.SubdivisionRepository;
import techit.gongsimchae.domain.portion.subdivision.service.ViewCountService;
import techit.gongsimchae.global.batch.JobLoggerListener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class SubViewsSyncBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ViewCountService viewCountService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SubdivisionRepository subdivisionRepository;

    private static final String WEEK = ":7";


    /**
     * 하루 조회수를 일주일 저장으로 바꾸기
     * 6시간 마다 실행
     */
    @Scheduled(cron = "30 59 23 * * *", zone = "Asia/Seoul")
    public void aggregateWeeklyViews() {
        Map<Object, Object> dailyViewCount = viewCountService.getDailyViewCount();
        for (Map.Entry<Object, Object> entry : dailyViewCount.entrySet()) {
            String subdivisionId = entry.getKey().toString().split(":")[0];
            Integer viewCount = (Integer) entry.getValue();
            String key = subdivisionId + WEEK + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            redisTemplate.opsForValue().increment(key, viewCount);
            redisTemplate.expire(key, Duration.ofDays(7));
        }

    }

    /**
     * 레디스에 저장된 조회수를 DB로 옮기기
     */

    @Bean
    public Job syncViewCountsToDBJob(){
        return new JobBuilder("syncViewJob", jobRepository)
                .listener(new JobLoggerListener())
                .start(SyncViewCountsToDBStep())
                .build();
    }

    @Bean
    public Step SyncViewCountsToDBStep() {
        return new StepBuilder("syncViewStop",jobRepository)
                .<Map.Entry<String, Double>, ViewCountDto>chunk(10, transactionManager)
                .reader(syncViewReader())
                .processor(syncViewProcessor())
                .writer(syncViewWriter())
                .build();

    }

    @Bean
    public ItemStreamReader<Map.Entry<String, Double>> syncViewReader() {
        return new SubItemReader(redisTemplate);
    }

    @Bean
    public ItemProcessor<Map.Entry<String, Double>, ViewCountDto> syncViewProcessor() {
        return item -> {
            String subdivisionId = item.getKey();
            Double viewCount = item.getValue();
            if (viewCount < 1.0)
                return null;
            System.out.println("subdivisionId = " + subdivisionId);

            return new ViewCountDto(subdivisionId, viewCount.longValue());
        };

    }


    @Bean
    @Transactional
    public ItemWriter<ViewCountDto> syncViewWriter() {
        return new ItemWriter<ViewCountDto>() {
            @Override
            public void write(Chunk<? extends ViewCountDto> chunk) throws Exception {
                for (ViewCountDto viewCountDto : chunk) {
                    subdivisionRepository.updateViewCount(viewCountDto.getViewCount(),viewCountDto.getSubdivisionId());
                }
            }
        };
    }



}
