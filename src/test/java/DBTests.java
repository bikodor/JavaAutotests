import dataBase.DBConnection;
import dataBase.DBSteps;
import dataBase.HelperDBMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.io.IOException;

public class DBTests {

    static JdbcTemplate template;

    @BeforeAll
    static void before() throws IOException {
        template = DBConnection.getTemplate();
        DBSteps.addTestDataForStaffTable(template);
        DBSteps.addTestDataForPersonTable(template);
        DBSteps.addTestDataForDocumentsTable(template);
        DBSteps.addTestDataForEmployeeTable(template);
    }

    @Test
    @Tag("SQL")
    void checkSQL() throws InterruptedException {
        String sql = "SELECT * FROM staff";
        SqlRowSet resultSet = HelperDBMethods.queryWithWait(sql, 30, template);
        Assertions.assertEquals("Иван", resultSet.getString("name"), "Имя не соответствует");
    }

    @Test
    @Tag("SQL")
    void checkSQLJoin() {
        String selectDefaultFields =
                "SELECT person.create_date,documents.original_flg,employee.license_flg\n" +
                        "FROM person\n" +
                        "JOIN documents ON documents.doc_id = person.doc_id\n" +
                        "JOIN employee ON employee.employee_id = documents.employee_id " +
                        "WHERE person.name='Иван'";
        SqlRowSet resultSet = template.queryForRowSet(selectDefaultFields);
        Assertions.assertTrue(resultSet.next(), "Пустой результат");
        Assertions.assertEquals("1995-01-01", resultSet.getString("create_date"), "Дефолтная дата create_date не соответствует ожидаемой");
        Assertions.assertFalse(resultSet.getBoolean("original_flg"), "Дефолтное значение original_flg не соответствует ожидаемому");
        Assertions.assertFalse(resultSet.getBoolean("license_flg"), "Дефолтное значение license_flg не соответствует ожидаемому");

    }

    @Test
    @Tag("SQL")
    void deleteSQL() {
        String countPerson = "SELECT COUNT(*) as count FROM person WHERE name = 'Виктор'";
        SqlRowSet resultSet = template.queryForRowSet(countPerson);
        Assertions.assertTrue(resultSet.next(), "Пустой результат");
        Assertions.assertEquals(1, resultSet.getInt("count"), "Неверное количество записей");

        String deletePerson =
                "DELETE FROM person WHERE name ='Виктор'";
        template.execute(deletePerson);
        countPerson = "SELECT COUNT(*) as count FROM person WHERE name = 'Виктор'";
        SqlRowSet resultSet2 = template.queryForRowSet(countPerson);
        Assertions.assertTrue(resultSet2.next(), "Пустой результат");
        Assertions.assertEquals(0, resultSet2.getInt("count"), "Неверное количество записей");
    }

    @Test
    @Tag("SQL")
    void updateSQL() {
        String oldName = "Владимир";
        String newName = "Всеволод";
        String updateperson =
                String.format("UPDATE person SET name = '%s' WHERE name='%s'", newName, oldName);
        template.execute(updateperson);
        String selectNameperson = "SELECT name FROM person WHERE doc_id = 9999";
        var resultSet = template.queryForRowSet(selectNameperson);
        Assertions.assertTrue(resultSet.next(), "Пустой результат");
        Assertions.assertEquals(newName, resultSet.getString("name"), "Имя не соответствует ожидаемому");
    }


}

