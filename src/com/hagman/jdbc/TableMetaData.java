package com.hagman.jdbc;

/**
 * Created by tom on 9/24/14.
 */
public class TableMetaData {
    private String tableName;
    private String routeName;
    private String packageName;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getRouteName() {
        return tableName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
