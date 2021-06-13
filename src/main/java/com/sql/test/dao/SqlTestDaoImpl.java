package com.sql.test.dao;

import com.sql.test.customexception.SaveUserException;
import com.sql.test.model.SqlQuery;
import com.sql.test.model.SqlTestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class SqlTestDaoImpl implements SqlTestDao {

    @Qualifier("readJdbcTemplate")
    @Autowired
    private JdbcTemplate readJdbcTemplate;

    @Qualifier("writeJdbcTemplate")
    @Autowired
    private JdbcTemplate writeJdbcTemplate;

    @Value("${query.createUser}")
    private String createUser;

    @Value("${query.submitUserRes}")
    private String submitRes;

    public void save(SqlTestUser user) {
        try {
            writeJdbcTemplate.execute(createUser, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps)
                        throws SQLException, DataAccessException {
                    ps.setString(1, user.getEmailId());
                    return ps.execute();
                }
            });
        } catch (Exception ex) {
            throw new SaveUserException(ex.getMessage());
        }
    }

    @Override
    public Object getUser(String query) {
        return writeJdbcTemplate.queryForList(query);
    }

    @Override
    public Object executeQuery(String query) {
        return readJdbcTemplate.queryForList(query);
    }

    @Override
    public Object getSqlQuestion(String query) {
        return writeJdbcTemplate.queryForList(query);
    }

    @Override
    public Object getTableMetaData(String query) {
        return writeJdbcTemplate.queryForList(query);
    }

    @Override
    public Object getExpectedOutput(String query) {
        return writeJdbcTemplate.queryForList(query);
    }

    @Override
    public Object getUSerQueryExecutionTime(String query) {
        return writeJdbcTemplate.queryForList(query);
    }

    @Override
    public void submitResponse(SqlQuery sqlQuery, long executionTime, boolean isOutputMatch) {
        writeJdbcTemplate.execute(submitRes, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                ps.setString(1, sqlQuery.getUserEmailId());
                ps.setString(2, sqlQuery.getQuery());
                ps.setBoolean(3, isOutputMatch);
                ps.setLong(4, executionTime);
                return ps.execute();
            }
        });
    }

}
