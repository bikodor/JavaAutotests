package dataBase;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class DBConnection {
    public static JdbcTemplate getTemplate(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("root");
        hikariDataSource.setJdbcUrl("jdbc:h2:mem:");
       return new JdbcTemplate(hikariDataSource);
    }
}
