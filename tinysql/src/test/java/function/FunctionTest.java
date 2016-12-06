package function;

import expression.SearchCondition;
import org.junit.Before;
import org.junit.Test;
import parser.ParseException;
import parser.Parser;
import storageManager.Relation;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

public class FunctionTest {

    final static String[] statements = {"CREATE TABLE course (sid INT, homework INT, project INT, exam INT, grade STR20)",
            "INSERT INTO course (sid, homework, project, exam, grade) VALUES (1, 99, 100, 100, \"A\")",
            "SELECT * FROM course"
    };
    final static String[] dataNameArray = {"sid", "homework", "project", "exam", "grade"};
    final static String[] dataTypeArray = {"INT", "INT", "INT", "INT", "STR20"};
    final static String[] dataValueArray1 = {"1", "99", "100", "100", "\"A\""};
    final static String[] dataValueArray2 = {"2", "99", "100", "100", "\"A\""};
    final static String tableName = "course";
    final static String searchConditionStatement = "sid = 2";
    final static ArrayList<String> dataTypes = new ArrayList<String>();
    final static ArrayList<String> dataNames = new ArrayList<String>();
    final static ArrayList<String> dataValues1 = new ArrayList<String>();
    final static ArrayList<String> dataValues2 = new ArrayList<String>();
    static SearchCondition searchCondition = null;

    @Before
    public void setUp() {
        Collections.addAll(dataNames, dataNameArray);
        Collections.addAll(dataTypes, dataTypeArray);
        Collections.addAll(dataValues1, dataValueArray1);
        Collections.addAll(dataValues2, dataValueArray2);
        try {
            searchCondition = new Parser(new StringReader(searchConditionStatement)).searchCondition();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateDropTable() throws ParseException {
        Function function = new Function();
        for (String statement : statements) {
            new Parser(new StringReader(statement)).statement().execute(function);
        }
        assertEquals(true, true);
    }

    @Test
    public void testGetTuplesFromRelation() {
        Function function = new Function();
        Relation relation = function.createTable(tableName, dataNames, dataTypes);
        function.insertIntoTableNtimes(tableName, dataNames, dataValues1);
        function.insertIntoTableNtimes(tableName, dataNames, dataValues2);
        String selection = function.getTuplesFromRelation(relation, 0, 1, new SearchCondition());
        assertEquals("1\t99\t100\t100\t\"A\"\t\n", selection);
    }

    @Test
    public void testInsertNtimes() {
        Function function = new Function();
        function.createTable(tableName, dataNames, dataTypes);
        Relation relation = function.insertIntoTableNtimes(tableName, dataNames, dataValues1);
        assertEquals("******RELATION DUMP BEGIN******\n" +
                "sid\thomework\tproject\texam\tgrade\t\n" +
                "0: 1\t99\t100\t100\t\"A\"\t\n" +
                "******RELATION DUMP END******", relation.toString());
        function.insertIntoTableNtimes(tableName, dataNames, dataValues2);
    }

    @Test
    public void testSelectWhere() {
        Function function = new Function();
        Relation relation = function.createTable(tableName, dataNames, dataTypes);
        function.insertIntoTableNtimes(tableName, dataNames, dataValues1);
        function.insertIntoTableNtimes(tableName, dataNames, dataValues2);
        searchCondition.setTableArray(new String[]{tableName});
        String selection = function.selectFromTable(tableName, searchCondition);
        assertEquals("2\t99\t100\t100\t\"A\"\t\n", selection);
    }

    @Test
    public void testDelete() {

    }
}