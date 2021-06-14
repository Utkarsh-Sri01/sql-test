package com.sql.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sql.test.model.SqlQuery;
import com.sql.test.model.SqlTestUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SqlTestApplication.class)
@AutoConfigureMockMvc
@EnableWebMvc
class SqlTestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateUserThenStatus201() throws Exception {

        mockMvc.perform(post("/createUser")
                .content(asJsonString(new SqlTestUser("utty@gmail.com")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateUserException500() throws Exception {

        mockMvc.perform(post("/createUser")
                .content(asJsonString(new SqlTestUser(null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("message", is("Cannot start the test, Please contact Alooba Helpdesk")));
    }

    @Test
    public void testStartTest200() throws Exception {

        mockMvc.perform(get("/startTest")
                .content(asJsonString(new SqlQuery("select * from google_users", "utty@gmail.com")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"questions\":[{\"question_id\":1,\"question_desc\":\"Write a query to get the number of unique Google users whose last login was in July, 2019, broken down by device type. Show the most used device in that period first.\"}],\"tables\":[{\"tableName\":\"google_users\",\"data\":[{\"name\":\"id\",\"type\":\"integer\",\"nullable\":\"NO\"},{\"name\":\"user_id\",\"type\":\"integer\",\"nullable\":\"NO\"},{\"name\":\"browser_language_code\",\"type\":\"text\",\"nullable\":\"NO\"},{\"name\":\"created_on\",\"type\":\"timestamp without time zone\",\"nullable\":\"NO\"},{\"name\":\"device_cat\",\"type\":\"text\",\"nullable\":\"NO\"}]},{\"tableName\":\"users\",\"data\":[{\"name\":\"user_id\",\"type\":\"integer\",\"nullable\":\"NO\"},{\"name\":\"is_activated\",\"type\":\"boolean\",\"nullable\":\"NO\"},{\"name\":\"signed_up_on\",\"type\":\"timestamp without time zone\",\"nullable\":\"NO\"},{\"name\":\"last_login\",\"type\":\"timestamp without time zone\",\"nullable\":\"YES\"},{\"name\":\"sign_up_source\",\"type\":\"text\",\"nullable\":\"YES\"},{\"name\":\"unsubscribed\",\"type\":\"integer\",\"nullable\":\"NO\"},{\"name\":\"user_type\",\"type\":\"integer\",\"nullable\":\"NO\"}]}]}"));
    }

    @Test
    public void testExecuteQueryOutputMatches() throws Exception {

        mockMvc.perform(post("/executeQuery")
                .content(asJsonString(new SqlQuery("select gu.device_cat as device_type, count (*) as user_count from google_users gu, users u where u.user_id = gu.user_id and u.last_login >= TO_DATE ('01-JUL-2019', 'DD-MON-YYYY') and u.last_login < TO_DATE ('01-AUG-2019', 'DD-MON-YYYY') Group by gu.device_cat order by user_count desc", "utty@gmail.com")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(jsonPath("isOutPutMatches", is(true)));
    }

    @Test
    public void testExecuteQueryOutputNotMatches() throws Exception {

        mockMvc.perform(post("/executeQuery").content(asJsonString(new SqlQuery("Select * from google_users", "utty@gmail.com")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(jsonPath("isOutPutMatches", is(false)));
    }

    @Test
    public void executeQueryTestException400() throws Exception {

        mockMvc.perform(post("/executeQuery")
                .content(asJsonString(new SqlQuery("elect gu.device_cat as device_type, count (*) as user_count from google_users gu, users u where u.user_id = gu.user_id and u.last_login >= TO_DATE ('01-JUL-2019', 'DD-MON-YYYY') and u.last_login < TO_DATE ('01-AUG-2019', 'DD-MON-YYYY') Group by gu.device_cat order by user_count desc", "utty@gmail.com")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("message", is("ERROR: syntax error at or near \"elect\"\n  Position: 1")));
    }

    @Test
    public void testSubmitSqlTest200() throws Exception {

        mockMvc.perform(post("/submitTest")
                .content(asJsonString(new SqlQuery("select gu.device_cat as device_type, count (*) as user_count from google_users gu, users u where u.user_id = gu.user_id and u.last_login >= TO_DATE ('01-JUL-2019', 'DD-MON-YYYY') and u.last_login < TO_DATE ('01-AUG-2019', 'DD-MON-YYYY') Group by gu.device_cat order by user_count desc", "utty@gmail.com")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(jsonPath("data", is("Test Submitted successfully")));
    }

    @Test
    public void testSubmitSqlTestException500() throws Exception {

        mockMvc.perform(post("/submitTest").content(asJsonString(new SqlQuery(null, "utty@gmail.com")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("message", is("Something went wrong !! Please contact Alooba Helpdesk")));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
