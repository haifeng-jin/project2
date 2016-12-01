package expression;

import function.Function;
import org.junit.Test;
import parser.Parser;

import java.io.StringReader;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DropTableTest {
    private String createStatement = "DROP TABLE table1";

    @Test
    public void executeTest() throws Exception {
        Function function = mock(Function.class);
        DropTable dropTable = new Parser(new StringReader(createStatement)).dropTableStatement();
        dropTable.execute(function);
        verify(function).dropTable(dropTable.tableName);
    }

    @Test
    public void toStringTest() throws Exception {
        DropTable dropTable = new Parser(new StringReader(createStatement)).dropTableStatement();
        assertEquals("drop table: table1", dropTable.toString());
    }
}