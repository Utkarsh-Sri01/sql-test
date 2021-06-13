package com.sql.test.dao;

import com.sql.test.model.SqlQuery;
import com.sql.test.model.SqlTestUser;

public interface SqlTestDao {

    void save(SqlTestUser user);

    Object getUser(String query);

    Object executeQuery(String query);

    Object getSqlQuestion(String query);

    Object getTableMetaData(String query);

    Object getExpectedOutput(String query);

    Object getUSerQueryExecutionTime(String query);

    void submitResponse(SqlQuery sqlQuery, long executionTime, boolean isOutputMatch);

}
