package expression;

import org.junit.Test;
import parser.ParseException;
import parser.Parser;

import java.io.StringReader;

import static org.junit.Assert.*;

public class SelectTest {
    @Test
    public void toStringTest() throws ParseException {
        String selectStatement = "SELECT DISTINCT abc, bcd, cde FROM table1, table2" +
                "WHERE id1 > id2 AND a < b" +
                "ORDER BY id";
        Select select = new Parser(new StringReader(selectStatement)).select();
        assertEquals("SELECT DISTINCT [abc, bcd, cde] FROM [table1, table2] [[id1 > id2, a < b]] ORDER BY id", select.toString());
    }
}