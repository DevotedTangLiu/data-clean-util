package com.gzcb.creditcard.data.clean.config;

import com.gzcb.creditcard.data.clean.quartz.JobManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tangliu
 */
@Configuration
public class InitConfig {

    @Bean
    public String initScheduledJobs() {

        new JobManager().init();

        return "scheduledJobs";
    }

}
