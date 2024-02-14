package dataBase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class HelperDBMethods {
    public static SqlRowSet queryWithWait(String sql, long wait, JdbcTemplate template) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime <= wait * 1000) {
            SqlRowSet resultSet = template.queryForRowSet(sql);
            if (resultSet.next()) {
                return resultSet;
            }

            Thread.sleep(5000);
        }
        throw new AssertionError(String.format("Истекло время ожидания получения значения ячейки SQL запрос: %s", sql));
    }
}
