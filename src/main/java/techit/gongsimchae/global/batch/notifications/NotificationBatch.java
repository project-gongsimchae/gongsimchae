package techit.gongsimchae.global.batch.notifications;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import techit.gongsimchae.domain.portion.notifications.entity.Notifications;
import techit.gongsimchae.domain.portion.notifications.repository.NotificationRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class NotificationBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final NotificationRepository notificationRepository;

    @Bean
    public Job removeNotificationJob() {
        return new JobBuilder("removeNotificationJob", jobRepository)
                .start(removeNotificationStep())
                .build();
    }

    @Bean
    public Step removeNotificationStep() {
        return new StepBuilder("removeNotificationStep", jobRepository)
                .<Notifications, Notifications>chunk(10, transactionManager)
                .reader(notificationReader())
                .processor(notificationProcessor())
                .writer(notificationWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<Notifications> notificationReader() {
        return new RepositoryItemReaderBuilder<Notifications>()
                .name("notificationsReader")
                .pageSize(10)
                .methodName("findAllUnreadNotifications")
                .arguments(Collections.singletonList(0L))
                .repository(notificationRepository)
                .sorts(Map.of("createDate", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<Notifications, Notifications> notificationProcessor() {
        return (item -> {
            ZonedDateTime oneMonthAgo = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).minusMinutes(1);

            // 알림학인한 게 1분전인지 확인
            if (item.getUpdateDate().isBefore(oneMonthAgo.toLocalDateTime())) {
                return item;
            } else {
                return null;
            }

        });
    }

    @Bean
    public RepositoryItemWriter<Notifications> notificationWriter() {
        return new RepositoryItemWriterBuilder<Notifications>()
                .repository(notificationRepository)
                .methodName("delete")
                .build();
    }

}
