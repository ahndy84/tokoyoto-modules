package com.salt;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.test.JobLauncherTestUtils;

@EnableBatchProcessing  //jobBuilder, StepBuilder, JobRepository, JobLauncher 등 다양한 설정이 자동으로 주입합니다.
@Configuration
public class TestJobConfig {

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils(); // Job 실행에 필요한 JobLauncher를 필드값으로 갖는 JobLauncherTestUtils를 빈으로 등록합니다.
    }
}
