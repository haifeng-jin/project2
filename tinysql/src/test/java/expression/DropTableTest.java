package expression;

import function.Function;
import org.junit.Test;
import parser.Parser;

import java.io.StringReader;

import static org.junit.Assert.*;

public class DropTableTest {

    @Test
    public void toStringTest() throws Exception {
        String createStatement = "DROP TABLE table1";
        DropTable dropTable = new Parser(new StringReader(createStatement)).dropTableStatement();
        assertEquals("drop table: table1", dropTable.toString());
    }
}