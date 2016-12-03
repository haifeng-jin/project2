package expression;

import org.junit.Test;
import parser.ParseException;
import parser.Parser;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class CreateTableTest {

    @Test
    public void toStringTest() throws ParseException {
        String createStatement = "CREATE TABLE table1 (attr1 INT, attr2 STR20)";
        CreateTable createTable = new Parser(new StringReader(createStatement)).createTableStatement();
        assertEquals("CREATE TABLE table1 (nameList: [attr1, attr2] typeList: [INT, STR20])", createTable.toString());
    }

}