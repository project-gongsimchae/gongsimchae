package techit.gongsimchae.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "customTaskExecutor")
    public Executor taskExecutor() {
        return new ThreadPoolExecutor(0, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
    }
}
