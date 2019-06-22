package com.gzcb.creditcard.data.clean.quartz;

import org.apache.commons.io.FileUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wulinzhen
 */
public class JobManager {

    private static Logger logger = LoggerFactory.getLogger(JobManager.class);

    private SchedulerFactory schedulerfactory;

    private Scheduler scheduler;

    public void init() {
        try {
            logger.info("JobManager init...");
            schedulerfactory = new StdSchedulerFactory();
            scheduler = schedulerfactory.getScheduler();
            scheduler.start();
            addAllJob();
        } catch (Exception e) {
            logger.error("初始化定时管理器出错", e);
        }
    }

    public void addAllJob() {

        /**
         * 加载所有的定时任务
         */
        List<JobDescription> jobs = readJobDescriptions();

        for (JobDescription job : jobs) {
            logger.info("添加定时任务:" + job.getId() + ":" + job.getDataBaseName() + ":" + job.getTableName());
            try {
                addJob(job);
            } catch (Exception e) {
                logger.error("添加定时任务({})({})({})失败", job.getId(), job.getDataBaseName(), job.getTableName(), e);
            }
        }
    }

    private List<JobDescription> readJobDescriptions() {

        String base = System.getProperty("worker.base");
        String confPath = base + "/conf/job.list.conf";

        List<JobDescription> result = new ArrayList<>();
        try {
            List<String> lines = FileUtils.readLines(new File(confPath), "UTF-8");
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("#")) {
                    continue;
                } else {
                    String[] data = line.split(",");
                    if (data.length != 9) {
                        logger.error("配置{}不正确，请检查", line);
                        continue;
                    }
                    JobDescription jd = new JobDescription();
                    jd.setId(Integer.valueOf(data[0].trim()));
                    jd.setCron(data[1].trim());
                    jd.setUrl(data[2].trim());
                    jd.setUserName(data[3].trim());
                    jd.setPassword(data[4].trim());
                    jd.setDataBaseName(data[5].trim());
                    jd.setTableName(data[6].trim());
                    jd.setColumnName(data[7].trim());
                    jd.setKeeyDays(Long.valueOf(data[8].trim()));

                    result.add(jd);
                }
            }
        } catch (IOException e) {
            logger.error("读取配置文件异常!", e);
        }
        return result;
    }

    /**
     * 增加任务
     *
     * @param task
     * @throws Exception
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addJob(JobDescription task) throws Exception {

        BaseJob job = new BaseJob(task);
        logger.info("添加定时任务({})({})({}),cron表达式:{}", task.getId(), task.getDataBaseName(), task.getTableName(), task.getCron());

        TriggerKey triggerKey = TriggerKey.triggerKey(job.getName(), job.getGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (null == trigger) {
            JobDetail jobDetail = JobBuilder.newJob(job.getClass()).withIdentity(job.getName(), job.getGroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob", job);
            jobDetail.getJobDataMap().put("description", task);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getDescription().getCron());
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getName(), job.getGroup()).withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            logger.info("任务({})已存在，重新调度:{}", job.getName() + ":" + job.getGroup(), job.getDescription().getCron());

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getDescription().getCron());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            ((CronTriggerImpl) trigger).setStartTime(new Date());
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }
}
