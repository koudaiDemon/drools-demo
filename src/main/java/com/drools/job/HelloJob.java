package com.drools.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author cWww
 * @Title
 * @Description
 * @date: 2019/2/27  14:05
 */
@Slf4j
public class HelloJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("now Time:{}",System.currentTimeMillis());
    }

}
