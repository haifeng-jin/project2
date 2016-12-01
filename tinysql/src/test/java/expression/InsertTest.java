package expression;

import org.junit.Test;
import parser.ParseException;
import parser.Parser;

import java.io.StringReader;

import static org.junit.Assert.*;

public class InsertTest {
    @Test
    public void toStringTest() throws ParseException {
        String insertStatement = "INSERT INTO table1 (attr1, attr2) VALUES (\"abc\", 123)";
        Insert insert = new Parser(new StringReader(insertStatement)).insertStatement();
        assertEquals("INSERT INTO table1 ([attr1, attr2]) [\"abc\", 123]", insert.toString());
    }
}