package expression;

import org.junit.Test;
import parser.ParseException;
import parser.Parser;

import java.io.StringReader;

import static org.junit.Assert.*;

public class DeleteTest {
    @Test
    public void toStringTest() throws ParseException {
        String deleteStatement = "DELETE FROM table1 WHERE att1=16";
        Delete delete = new Parser(new StringReader(deleteStatement)).delete();
        assertEquals("DELETE FROM table1WHERE [[att1 = 16]]", delete.toString());
    }
}