package expression;

import org.junit.Test;
import parser.ParseException;
import parser.Parser;

import java.io.StringReader;

import static org.junit.Assert.*;

public class ValueListTest {
    @Test
    public void toStringTest() throws ParseException {
        String valueListStatement = "\"asdfb\", 12, 3";
        ValueList valueList = new Parser(new StringReader(valueListStatement)).valueList();
        assertEquals("[asdfb, 12, 3]", valueList.toString());
    }

}