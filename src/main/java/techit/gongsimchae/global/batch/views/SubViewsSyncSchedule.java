package techit.gongsimchae.global.batch.views;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import techit.gongsimchae.domain.portion.subdivision.service.ViewCountService;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import static techit.gongsimchae.global.util.ViewVO.WEEK;

@Configuration
public class SubViewsSyncSchedule {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final ViewCountService viewCountService;
    private final RedisTemplate<String, Object> redisTemplate;

    public SubViewsSyncSchedule(JobLauncher jobLauncher, JobRegistry jobRegistry,
                                ViewCountService viewCountService, RedisTemplate<String, Object> redisTemplate) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
        this.viewCountService = viewCountService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 1시간 마다 DB로 조회수 저장하기
     */
    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void runSyncDB() throws Exception {
        System.out.println("syncViewJob start");
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("syncViewJob"), jobParameters);

    }





    /**
     * 하루 조회수를 일주일 저장으로 바꾸기
     * 6시간 마다 실행
     */
    @Scheduled(cron = "30 59 23 * * *", zone = "Asia/Seoul")
    public void aggregateWeeklyViews() {
        Map<Object, Double> dailyViewCount = viewCountService.getDailyViewCount();
        for (Map.Entry<Object, Double> entry : dailyViewCount.entrySet()) {
            String subdivisionId = entry.getKey().toString().split(":")[0];
            Integer viewCount = entry.getValue().intValue();
            String key = subdivisionId + WEEK + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            redisTemplate.opsForValue().increment(key, viewCount);
            redisTemplate.expire(key, Duration.ofDays(7));
        }

    }

}
