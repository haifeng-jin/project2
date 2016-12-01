package parser;

import expression.Insert;
import function.Function;
import org.junit.Test;

import java.io.StringReader;

public class ParserTest {

    @Test
    public void testCreateDropTable() throws ParseException {
        Function function = new Function();
        String createStatement = "CREATE TABLE table1 (attr1 INT, attr2 STR20)";
        new Parser(new StringReader(createStatement)).createTableStatement().execute(function);

        String insertStatement = "INSERT INTO table1 (attr1, attr2) VALUES (123, \"abc\")";
        new Parser(new StringReader(insertStatement)).insertStatement().execute(function);

        String dropStatement = "DROP TABLE table1";
        new Parser(new StringReader(dropStatement)).dropTableStatement().execute(function);
    }
}

