package io.jeongjaeeom.concurrent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class SpringBootConcurrentApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootConcurrentApplication.class, args);
  }

  @Bean
  public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(30);
    executor.setMaxPoolSize(30);
    executor.setQueueCapacity(10);
    executor.setThreadNamePrefix("Executor-");
    executor.initialize();
    return executor;
  }

}
