package techit.gongsimchae.global.batch.views;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;
import techit.gongsimchae.domain.portion.subdivision.repository.SubdivisionRepository;
import techit.gongsimchae.global.batch.JobLoggerListener;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SubViewsSyncBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SubdivisionRepository subdivisionRepository;

    /**
     * 레디스에 저장된 조회수를 DB로 옮기기
     */

    @Bean
    public Job syncViewCountsToDBJob(){
        return new JobBuilder("syncViewJob", jobRepository)
                .listener(new JobLoggerListener())
                .start(SyncViewCountsToDBStep())
                .next(SyncViewCountsToRedisStep())
                .build();
    }

    @Bean
    public Step SyncViewCountsToDBStep() {
        return new StepBuilder("syncViewStep",jobRepository)
                .<Map.Entry<String, Double>, ViewCountDto>chunk(10, transactionManager)
                .reader(syncViewReader())
                .processor(syncViewProcessor())
                .writer(syncViewWriter())
                .faultTolerant()
                .retryLimit(3)
                .retry(SQLException.class)
                .build();


    }

    @Bean
    public ItemStreamReader<Map.Entry<String, Double>> syncViewReader() {
        return new SubItemReader(redisTemplate);
    }

    @Bean
    public ItemProcessor<Map.Entry<String, Double>, ViewCountDto> syncViewProcessor() {
        return new ItemProcessor<Map.Entry<String, Double>, ViewCountDto>() {
            @Override
            public ViewCountDto process(Map.Entry<String, Double> item) throws Exception {
                Double value = item.getValue();
                String key = item.getKey();
                log.debug("process key {}", key);
                return new ViewCountDto(key, value.intValue());
            }
        };

    }


    @Bean
    public ItemWriter<ViewCountDto> syncViewWriter() {
        return new ItemWriter<ViewCountDto>() {
            @Override
            public void write(Chunk<? extends ViewCountDto> chunk) throws Exception {
                for (ViewCountDto viewCountDto : chunk) {
                    log.debug("viewCountDto = {}", viewCountDto);
                    subdivisionRepository.updateViewCount(viewCountDto.getViewCount(),viewCountDto.getSubdivisionId());
                }
            }
        };
    }


    /**
     * DB에 저장된 조회수를 레디스로 옮기기
     */

    @Bean
    public Job syncViewCountsToRedisJob(){
        return new JobBuilder("syncViewRedisJob", jobRepository)
                .listener(new JobLoggerListener())
                .start(SyncViewCountsToDBStep())
                .build();
    }

    @Bean
    public Step SyncViewCountsToRedisStep() {
        return new StepBuilder("syncViewRedisStep",jobRepository)
                .<Subdivision, ViewCountDto>chunk(10, transactionManager)
                .reader(syncViewRedisReader())
                .processor(syncViewRedisProcessor())
                .writer(syncViewRedisWriter())
                .faultTolerant()
                .retryLimit(3)
                .retry(SQLException.class)
                .build();


    }

    @Bean
    public RepositoryItemReader<Subdivision> syncViewRedisReader() {
        return new RepositoryItemReaderBuilder<Subdivision>()
                .name("subdivisionReader")
                .pageSize(10)
                .methodName("findAllWithViews")
                .arguments(Collections.singletonList(0))
                .repository(subdivisionRepository)
                .build();
    }

    @Bean
    public ItemProcessor<Subdivision, ViewCountDto> syncViewRedisProcessor() {
        return item -> {
            String uid = item.getUID();
            Integer views = item.getViews();
            return new ViewCountDto(uid, views);
        };
    }


    @Bean
    public ItemWriter<ViewCountDto> syncViewRedisWriter() {
        return new SubItemWriter(redisTemplate);

    }



}
