package expression;

import org.junit.Test;
import parser.ParseException;
import parser.Parser;

import java.io.StringReader;

import static org.junit.Assert.*;

public class AttributeListTest {
    @Test
    public void toStringTest() throws ParseException {
        String attributeListStatement = "attr1, attr2";
        AttributeList attributeList = new Parser(new StringReader(attributeListStatement)).attributeList();
        assertEquals("[attr1, attr2]", attributeList.toString());
    }

}