package expression;

import org.junit.Test;
import parser.ParseException;
import parser.Parser;

import java.io.StringReader;

import static org.junit.Assert.*;

public class SearchConditionTest {

    @Test
    public void toStringTest() throws ParseException {
        String searchConditionStatement = "abcd < b AND 1 = 1 OR asdf > 3 OR (1 + 1) = 2";
        SearchCondition searchCondition = new Parser(new StringReader(searchConditionStatement)).searchCondition();
        assertEquals("[[abcd < b, 1 = 1], [asdf > 3], [1 + 1 = 2]]", searchCondition.toString());
    }
}