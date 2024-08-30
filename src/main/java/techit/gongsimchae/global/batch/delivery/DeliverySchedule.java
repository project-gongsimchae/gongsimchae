package techit.gongsimchae.global.batch.delivery;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class DeliverySchedule {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    public DeliverySchedule(JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void runDeliveryStatusJob() throws Exception {
        System.out.println("first delivery schedule start");
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("updateDeliveryStatusJob"), jobParameters);
    }

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void runDeliveryStatusCompletionJob() throws Exception {
        System.out.println("second delivery schedule start");
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("updateDeliveryStatusToCompleteJob"), jobParameters);
    }
}
