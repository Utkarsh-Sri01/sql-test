package com.sql.test.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ApplicationConfig {

    @Value("${spring.datasource.username.admin}")
    private String adminUserName;

    @Value("${spring.datasource.password.admin}")
    private String adminPwd;

    @Value("${spring.datasource.username.sqltestuser}")
    private String sqlUserName;

    @Value("${spring.datasource.password.sqltestuser}")
    private String sqlUserPwd;

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Bean
    public DataSource getDataSourceForAdmin() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(databaseUrl);
        dataSourceBuilder.username(adminUserName);
        dataSourceBuilder.password(adminPwd);
        return dataSourceBuilder.build();
    }

    @Bean
    public DataSource getDataSourceForSqlTestUser() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(databaseUrl);
        dataSourceBuilder.username(sqlUserName);
        dataSourceBuilder.password(sqlUserPwd);
        return dataSourceBuilder.build();
    }

    @Bean
    @Qualifier("readJdbcTemplate")
    public JdbcTemplate readJdbcTemplate(){
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
       jdbcTemplate.setDataSource(getDataSourceForSqlTestUser());
       return  jdbcTemplate;

    }

    @Bean
    @Qualifier("writeJdbcTemplate")
    public JdbcTemplate writeJdbcTemplate(){
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        jdbcTemplate.setDataSource(getDataSourceForAdmin());
        return  jdbcTemplate;
    }

}
