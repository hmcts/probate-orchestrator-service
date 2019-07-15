package uk.gov.hmcts.probate.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.probate.core.service.migration.FormDataMigrator;
import uk.gov.hmcts.probate.core.service.migration.InviteDataMigrator;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Value("${migration.job.startDateTime}")
    private String migrationJobStartDateTime;
    @Value("${migration.job.schedule}")
    private Boolean migrationJobSchedule;

    @Autowired
    SecurityUtils securityUtils;

    private ScheduledFuture<?> migrationJob;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler =new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-thread");
        threadPoolTaskScheduler.initialize();
        if(migrationJobSchedule) {
            migrationJob(threadPoolTaskScheduler);
        }
        this.taskScheduler=threadPoolTaskScheduler;
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
    private void migrationJob(TaskScheduler scheduler) {
        migrationJob = scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                log.info(Thread.currentThread().getName() + " The migrationJob executed at " + new Date());
                securityUtils.setSecurityContextUserAsCaseworker();
                try {
                    Thread.sleep(10000);
                    formDataMigrator.migrateFormData();
                    inviteDataMigrator.migrateInviteData();
                    log.info(Thread.currentThread().getName() + "MigrationJob complete at " + new Date());
                } catch (RuntimeException e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
                catch (InterruptedException e){
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }, getStartTime());
    }

    private Date getStartTime(){
        LocalDateTime dateTime = LocalDateTime.parse(migrationJobStartDateTime);
        Date date = Date.from( dateTime.atZone( ZoneId.systemDefault()).toInstant());
        return date;
    }
}
