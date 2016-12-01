package expression;

import org.junit.Test;
import parser.ParseException;
import parser.Parser;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class AttributeTypeListTest {

    @Test
    public void testGetNameTypeList() throws ParseException {
        String statement = "attr1 INT, attr2 STR20";
        AttributeTypeList list = new Parser(new StringReader(statement)).attributeTypeList();
        assertEquals("attr1", list.nameList.get(0));
        assertEquals("attr2", list.nameList.get(1));
        assertEquals("INT", list.typeList.get(0));
        assertEquals("STR20", list.typeList.get(1));
    }
}
