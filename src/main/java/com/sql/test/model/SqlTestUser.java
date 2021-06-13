package com.sql.test.model;

import org.springframework.lang.NonNull;

public class SqlTestUser {

    private String emailId;

    public SqlTestUser() {
    }

    public SqlTestUser(String emailId) {
        this.emailId = emailId;
    }

    @NonNull
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(@NonNull String emailId) {
        this.emailId = emailId;
    }

    @Override
    public String toString() {
        return "SqlTestUser{" +
                "emailId='" + emailId + '\'' +
                '}';
    }
}
