package expression;

import function.Function;
import org.junit.Test;
import parser.ParseException;
import parser.Parser;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateTableTest {

    private String createStatement = "CREATE TABLE table1 (attr1 INT, attr2 STR20)";

    @Test
    public void executeTest() throws Exception {
        Function function = mock(Function.class);
        CreateTable createTable = new Parser(new StringReader(createStatement)).createTableStatement();
        createTable.execute(function);
        AttributeTypeList list = createTable.list;
        verify(function).createTable(createTable.tableName, list.nameList, list.typeList);
    }

    @Test
    public void toStringTest() throws ParseException {
        CreateTable createTable = new Parser(new StringReader(createStatement)).createTableStatement();
        assertEquals("CREATE TABLE table1 (nameList: [attr1, attr2] typeList: [INT, STR20])", createTable.toString());
    }

}