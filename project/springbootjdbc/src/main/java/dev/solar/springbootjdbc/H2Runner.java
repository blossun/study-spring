package dev.solar.springbootjdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class H2Runner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(H2Runner.class);

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            // 접속할 DB에 대한 정보 확인
            log.debug("URL : {}, UserName : {}",
                    connection.getMetaData().getURL(),
                    connection.getMetaData().getUserName());
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE user (id INTEGER NOT NULL, name VARCHAR(255), PRIMARY KEY (id));\n" +
                    "INSERT INTO user VALUES (1, 'solar')";
            statement.executeUpdate(sql);
        }

    }
}
