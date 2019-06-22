package com.gzcb.creditcard.data.clean.quartz;

import com.alibaba.druid.pool.DruidDataSource;
import com.gzcb.creditcard.data.clean.uitls.DateUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;

/**
 * @author tangliu
 */
public class BaseJob implements Job {

    static Logger logger = LoggerFactory.getLogger(BaseJob.class);

    private JobDescription description;
    /**
     * 名称
     */
    private String name;
    /**
     * 组
     */
    private String group;

    public BaseJob() {

    }

    public BaseJob(JobDescription description) {
        this.description = description;
        this.name = description.getId() + ":" + description.getDataBaseName() + ":" + description.getTableName();
        this.group = this.getClass().getSimpleName();
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        JobDescription description = (JobDescription) jobDataMap.get("description");
        logger.info("数据清理开始执行任务({})({})", description.getId(), description.getDataBaseName() + ":" + description.getTableName());

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(description.getUrl());
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername(description.getUserName());
        dataSource.setPassword(description.getPassword());
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(5);


        QueryRunner runner = new QueryRunner(dataSource);
        String dateStr = DateUtils.getTimeByOffset("yyyy-MM-dd", description.getKeeyDays(), ChronoUnit.DAYS);
        String deleteSql = "delete from " + description.getDataBaseName() + "." + description.getTableName() + " where "
                + description.getColumnName() + " &lt;= '" + dateStr + " 24:00:00'";

        try {
            runner.execute(deleteSql);
        } catch (SQLException e) {
            logger.error("数据清理执行异常！", e);
        } finally {
            dataSource.close();
        }

        logger.info("数据清理任务({})({})执行完成", description.getId(), description.getDataBaseName() + ":" + description.getTableName());
    }

    /**
     * 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 组
     */
    public String getGroup() {
        return group;
    }

    /**
     * 组
     */
    public void setGroup(String group) {
        this.group = group;
    }


    public JobDescription getDescription() {
        return description;
    }
}
