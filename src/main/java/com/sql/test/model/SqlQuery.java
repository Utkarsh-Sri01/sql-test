package com.sql.test.model;

public class SqlQuery {
    private String query;
    private String userEmailId;

    public SqlQuery(){}
    public SqlQuery(String query,String emailId){
        this.query=query;
        this.userEmailId=emailId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }
}
