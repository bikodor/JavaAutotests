package dataBase;

import org.springframework.jdbc.core.JdbcTemplate;
import util.FileUtil;

import java.io.IOException;

public class DBSteps {
    public static void addTestDataForStaffTable(JdbcTemplate template) throws IOException {
        String query = FileUtil.getTextFileContent("sql/table.sql");
        template.execute(query);
        template.execute("INSERT INTO staff (id, name) VALUES (1, 'Иван')");
    }

    public static void addTestDataForPersonTable(JdbcTemplate template) throws IOException {
        String queryperson = FileUtil.getTextFileContent("sql/tablePerson.sql");
        template.execute(queryperson);
        template.execute("INSERT INTO person (name,surname,doc_id) VALUES ('Иван','Иванов',777)");
        template.execute("INSERT INTO person (name,surname,doc_id) VALUES ('Владимир','Иванов',9999)");
        template.execute("INSERT INTO person (name,surname,doc_id) VALUES ('Виктор','Иванов',777)");
    }

    public static void addTestDataForDocumentsTable(JdbcTemplate template) throws IOException {
        String queryDocuments = FileUtil.getTextFileContent("sql/tableDocuments.sql");
        template.execute(queryDocuments);
        template.execute("INSERT INTO documents (doc_id, doc_series,doc_num,employee_id) VALUES (777,4444,123456,12345)");
    }

    public static void addTestDataForEmployeeTable(JdbcTemplate template) throws IOException {
        String queryEmployee = FileUtil.getTextFileContent("sql/tableEmployee.sql");
        template.execute(queryEmployee);
        template.execute("INSERT INTO employee (fullname,employee_id) VALUES ('Миханюк Лариса Аромовна',12345)");
    }

}
