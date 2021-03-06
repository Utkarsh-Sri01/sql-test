package com.sql.test.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sql.test.Constants;
import com.sql.test.controller.SqlTestController;
import com.sql.test.customexception.QueryExecutionException;
import com.sql.test.customexception.SubmitTestException;
import com.sql.test.dao.SqlTestDao;
import com.sql.test.model.SqlQuery;
import com.sql.test.model.SqlTestUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SqlTestServiceImpl implements SqlTestService {

    private final Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    SqlTestDao sqlTestDao;

    @Value("${my.list.of.tables}")
    private List<String> tableList;

    @Value("${my.database.name}")
    private String databaseName;

    @Value("${query.Output}")
    private String outputQuery;

    @Value("${query.questions}")
    private String questions;

    @Value("${query.getUser}")
    private String user;


    private static final String IS_OUTPUT_MATCH = "isOutPutMatches";
    private static final String QUESTION = "questions";
    private static final String TABLE_NAME = "tableName";
    private static final String TABLES = "tables";
    private static final String SUCCESS_MESSAGE = "Test Submitted successfully";

    @Override
    public void save(SqlTestUser user) {
        logger.info("Start creating user");
        boolean isUserExists = checkIfUserExists(user.getEmailId());
        if (!isUserExists) {
            sqlTestDao.save(user);
        }
        logger.info("User created successfully");
    }

    @Override
    public JsonNode executeQuery(String query) {
        try {
            ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
            logger.info("start executing user query");
            Object queryResult = sqlTestDao.executeQuery(query);
            jsonNode.putPOJO(Constants.DATA, queryResult);
            boolean isMatch = checkIfOutputMatches((List<Map<String, Object>>) queryResult);
            jsonNode.put(IS_OUTPUT_MATCH, isMatch);
            logger.info("user query executed successfully");
            return jsonNode;

        } catch (Exception e) {
            throw new QueryExecutionException(NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        }
    }

    @Override
    public JsonNode getSqlTestQuestion() {
        ObjectNode result = JsonNodeFactory.instance.objectNode();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode questionJsonNode = mapper.convertValue(sqlTestDao.getSqlQuestion(questions), JsonNode.class);
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        logger.info("fetch sql question details");
        for (String tName : tableList) {
            ObjectNode objectNodeTab = JsonNodeFactory.instance.objectNode();

            ArrayNode metaDataNode = mapper.convertValue(sqlTestDao.getTableMetaData("select column_name as name, data_type as type,is_nullable as nullable from INFORMATION_SCHEMA.COLUMNS\n" +
                    "WHERE table_catalog='" + databaseName + "' AND table_name = '" + tName + "'"), ArrayNode.class);
            objectNodeTab.put(TABLE_NAME, tName);
            objectNodeTab.putPOJO(Constants.DATA, metaDataNode);
            arrayNode.add(objectNodeTab);
        }
        result.putPOJO(QUESTION, questionJsonNode);
        result.putPOJO(TABLES, arrayNode);

        logger.info("sql question details fetched successfully");
        return result;
    }

    @Override
    public Object submitTest(SqlQuery sqlQuery) {

        try {
            logger.info("submit test");
            boolean isMatch = checkIfOutputMatches((List<Map<String, Object>>) sqlTestDao.executeQuery(sqlQuery.getQuery()));
            long execTime = getQueryExecutionTime(sqlQuery.getQuery());
            sqlTestDao.submitResponse(sqlQuery, execTime, isMatch);
            logger.info("Test submitted successfully");
        } catch (Exception e) {
            throw new SubmitTestException(NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        }
        return SUCCESS_MESSAGE;
    }

    private long getQueryExecutionTime(String query) {
        StopWatch watch = new StopWatch();
        watch.start();
        sqlTestDao.getUSerQueryExecutionTime(query);
        watch.stop();
        return watch.getTotalTimeMillis();
    }

    private boolean checkIfOutputMatches(List<Map<String, Object>> queryResult) {

        boolean isMatch = false;
        List<Map<String, Object>> ex = (List<Map<String, Object>>) sqlTestDao.getExpectedOutput(outputQuery);
        List<Object> values = new ArrayList<>();
        List<Object> queryValues = new ArrayList<>();

        for (Map<String, Object> k : ex) {
            values.addAll(k.values());
        }
        for (Map<String, Object> k : queryResult) {
            queryValues.addAll(k.values());
        }
        logger.info("check if user query output matches with expected output");
        isMatch = values.equals(queryValues);
        return isMatch;
    }


    private boolean checkIfUserExists(String emailId) {
        boolean isUserExist = false;
        List<Object> users = new ArrayList<>();
        List<Map<String, Object>> userList = (List<Map<String, Object>>) sqlTestDao.getUser(user);
        for (Map<String, Object> k : userList) {
            users.addAll(k.values());
        }
        logger.info("checking if user exists");
        if (users.contains(emailId)) {
            isUserExist = true;
        }
        return isUserExist;
    }


}
