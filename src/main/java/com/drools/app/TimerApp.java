package com.drools.app;

import com.drools.BaseTest;
import com.drools.job.HelloJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author cWww
 * @Title TimerApp
 * @Description 任务调度
 * @date: 2019/2/27  13:57
 */
@Slf4j
public class TimerApp extends BaseTest {

    public void quartz(){

        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(3)
                            .repeatForever())
                    .build();

            //定义Job类为HelloQuartz类，这是真正的执行逻辑所在
            JobDetail job = newJob(HelloJob.class)
                    //定义name/group
                    .withIdentity("job1", "group1")
                    //定义属性
                    .usingJobData("name", "quartz")
                    .build();

            scheduler.scheduleJob(job, trigger);

            // and start it off
            scheduler.start();

            //运行一段时间后关闭
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        }

    }

}
