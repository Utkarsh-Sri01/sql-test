package com.sql.test.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sql.test.model.SqlQuery;
import com.sql.test.model.SqlTestUser;

public interface SqlTestService {

    public void save(SqlTestUser user);
    public JsonNode executeQuery(String query);
    public JsonNode getSqlTestQuestion();
    public Object submitTest(SqlQuery sqlQuery);

}
