package uk.gov.hmcts.probate.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.probate.core.service.migration.FormDataMigrator;
import uk.gov.hmcts.probate.core.service.migration.InviteDataMigrator;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Configuration
@EnableScheduling
public class ScheduledConfiguration implements SchedulingConfigurer {
    TaskScheduler taskScheduler;

    @Autowired
    FormDataMigrator formDataMigrator;
    @Autowired
    InviteDataMigrator inviteDataMigrator;

    @Autowired
    SecurityUtils securityUtils;

    private ScheduledFuture<?> migrationJob;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler =new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);// Set the pool of threads
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-thread");
        threadPoolTaskScheduler.initialize();
        migrationJob(threadPoolTaskScheduler);// Assign the job1 to the scheduler
        this.taskScheduler=threadPoolTaskScheduler;// this will be used in later part of the article during refreshing the cron expression dynamically
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
    private void migrationJob(TaskScheduler scheduler) {
        migrationJob = scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                securityUtils.setSecurityContextUserAsCaseworker();
                log.info(Thread.currentThread().getName() + " The migrationJob executed at " + new Date());
                try {
                    Thread.sleep(10000);
                    formDataMigrator.migrateFormData();
                    inviteDataMigrator.migrateInviteData();
                } catch (RuntimeException e) {
                    // TODO Auto-generated catch block
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
                catch (InterruptedException e){
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Date());
    }
}
