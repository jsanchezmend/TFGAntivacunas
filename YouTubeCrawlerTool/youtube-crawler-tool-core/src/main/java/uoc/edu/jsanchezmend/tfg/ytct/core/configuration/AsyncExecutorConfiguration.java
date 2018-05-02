package uoc.edu.jsanchezmend.tfg.ytct.core.configuration;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configure @Executor to handler @Async processes
 * (used for asynchronous execution of the crawler processes)
 * (example: https://spring.io/guides/gs/async-method/)
 * 
 * @author jsanchezmend
 *
 */
@EnableAsync
@Configuration
public class AsyncExecutorConfiguration {
	
	/*
	 * https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/integration.html#scheduling-task-executor
	 */
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("crawler-");
        executor.initialize();
        return executor;
    }

}
