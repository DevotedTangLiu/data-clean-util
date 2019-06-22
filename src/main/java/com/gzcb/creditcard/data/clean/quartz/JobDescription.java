package com.gzcb.creditcard.data.clean.quartz;

/**
 * @author tangliu
 */
public class JobDescription {

    private Integer id;

    private String cron;

    private String url;

    private String userName;

    private String password;

    private String dataBaseName;

    private String tableName;

    private String columnName;

    private Long keeyDays;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Long getKeeyDays() {
        return keeyDays;
    }

    public void setKeeyDays(Long keeyDays) {
        this.keeyDays = keeyDays;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
