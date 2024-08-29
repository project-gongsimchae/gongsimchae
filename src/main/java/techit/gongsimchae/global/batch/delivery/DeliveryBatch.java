package techit.gongsimchae.global.batch.delivery;

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
import techit.gongsimchae.domain.groupbuying.delivery.entity.Delivery;
import techit.gongsimchae.domain.groupbuying.delivery.entity.DeliveryStatus;
import techit.gongsimchae.domain.groupbuying.delivery.repository.DeliveryRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DeliveryBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DeliveryRepository deliveryRepository;

    @Bean
    public Job updateDeliveryStatusJob() {
        return new JobBuilder("updateDeliveryStatusJob", jobRepository)
                .start(updateDeliveryStatusStep())
                .build();
    }

    @Bean
    public Step updateDeliveryStatusStep() {
        return new StepBuilder("updateDeliveryStatusStep", jobRepository)
                .<Delivery, Delivery>chunk(10, transactionManager)
                .reader(deliveryReader())
                .processor(deliveryProcessor())
                .writer(deliveryWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<Delivery> deliveryReader() {
        return new RepositoryItemReaderBuilder<Delivery>()
                .name("deliveryReader")
                .pageSize(10)
                .methodName("findByDeliveryStatus")
                .arguments(Collections.singletonList(DeliveryStatus.배달준비중))
                .repository(deliveryRepository)
                .sorts(Map.of("createDate", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<Delivery, Delivery> deliveryProcessor() {
        return (delivery -> {
            ZonedDateTime tenMinutesAgo = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).minusMinutes(10);

            if (delivery.getCreateDate().isBefore(tenMinutesAgo.toLocalDateTime())) {
                delivery.updateDeliveryStatus(DeliveryStatus.배달중);
                return delivery;
            } else {
                return null;
            }
        });
    }

    @Bean
    public RepositoryItemWriter<Delivery> deliveryWriter() {
        return new RepositoryItemWriterBuilder<Delivery>()
                .repository(deliveryRepository)
                .methodName("save")
                .build();
    }

    @Bean
    public Job updateDeliveryStatusToCompleteJob() {
        return new JobBuilder("updateDeliveryStatusToCompleteJob", jobRepository)
                .start(updateDeliveryStatusCompleteStep())
                .build();
    }

    @Bean
    public Step updateDeliveryStatusCompleteStep() {
        return new StepBuilder("updateDeliveryStatusCompleteStep", jobRepository)
                .<Delivery, Delivery>chunk(10, transactionManager)
                .reader(deliveryForCompleteReader())
                .processor(deliveryForCompleteProcessor())
                .writer(deliveryWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<Delivery> deliveryForCompleteReader() {
        return new RepositoryItemReaderBuilder<Delivery>()
                .name("deliveryForCompleteReader")
                .pageSize(10)
                .methodName("findByDeliveryStatus")
                .arguments(Collections.singletonList(DeliveryStatus.배달중))
                .repository(deliveryRepository)
                .sorts(Map.of("createDate", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<Delivery, Delivery> deliveryForCompleteProcessor() {
        return (delivery -> {
            ZonedDateTime tenMinutesAgo = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).minusMinutes(20);

            if (delivery.getUpdateDate().isBefore(tenMinutesAgo.toLocalDateTime())) {
                delivery.updateDeliveryStatus(DeliveryStatus.배달완료);
                return delivery;
            } else {
                return null;
            }
        });
    }
}
