package com.sql.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sql.test.Constants;
import com.sql.test.model.SqlQuery;
import com.sql.test.model.SqlTestUser;
import com.sql.test.service.SqlTestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@Api(description = "New Sql Type Test" , tags="SQL test")
@RestController
public class SqlTestController {

    @Autowired
    SqlTestService sqlTestService;

    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody SqlTestUser user) {

        sqlTestService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/executeQuery")
    public JsonNode executeQuery(@RequestBody SqlQuery query) {

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
        return sqlTestService.executeQuery(query.getQuery());

    }

    @GetMapping("/startTest")
    public JsonNode startTest() {

        JsonNode sqlTestQuestion = sqlTestService.getSqlTestQuestion();
        return sqlTestQuestion;

    }

    @PostMapping("/submitTest")
    public JsonNode submitTest(@RequestBody SqlQuery query) {

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
        return jsonNode.putPOJO(Constants.DATA, sqlTestService.submitTest(query));

    }
}
