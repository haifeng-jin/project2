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
            "SELECT * FROM course",
            "SELECT * FROM course WHERE exam = 100",
            "SELECT * FROM course WHERE (exam + homework) = 200"
    };
    final static String[] dataNameArray = {"sid", "homework", "project", "exam", "grade"};
    final static String[] dataTypeArray = {"INT", "INT", "INT", "INT", "STR20"};
    final static String[] dataValueArray1 = {"1", "99", "100", "100", "\"A\""};
    final static String[] dataValueArray2 = {"2", "99", "100", "100", "\"A\""};
    final static String tableName = "course";
    final static String searchConditionStatement = "sid = 2";
    static ArrayList<String> dataTypes = null;
    static ArrayList<String> dataNames = null;
    static ArrayList<String> dataValues1 = null;
    static ArrayList<String> dataValues2 = null;
    static SearchCondition searchCondition = null;
    static ArrayList<String> fieldList = null;
    static ArrayList<String> fieldListStar = null;


    @Before
    public void setUp() {
        dataNames = new ArrayList<String>();
        dataTypes = new ArrayList<String>();
        dataValues1 = new ArrayList<String>();
        dataValues2= new ArrayList<String>();
        fieldListStar = new ArrayList<String>();
        fieldList = new ArrayList<String>();
        Collections.addAll(dataNames, dataNameArray);
        Collections.addAll(dataTypes, dataTypeArray);
        Collections.addAll(dataValues1, dataValueArray1);
        Collections.addAll(dataValues2, dataValueArray2);
        fieldList.add("homework");
        fieldList.add("exam");
        fieldListStar.add("*");
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
        String selection = function.getTuplesFromRelation(relation, 0, 1, dataNames, new SearchCondition());
        assertEquals("1\t99\t100\t100\t\"A\"\t\n", selection);
    }

    @Test
    public void testInsertNtimes() {
        Function function = new Function();
        function.createTable(tableName, dataNames, dataTypes);
        function.insertIntoTableNtimes(tableName, dataNames, dataValues1);
        Relation relation = function.insertIntoTableNtimes(tableName, dataNames, dataValues2);
        assertEquals(2, relation.getNumOfBlocks());
        assertEquals("******RELATION DUMP BEGIN******\n" +
                "sid\thomework\tproject\texam\tgrade\t\n" +
                "0: 1\t99\t100\t100\t\"A\"\t\n" +
                "1: 2\t99\t100\t100\t\"A\"\t\n" +
                "******RELATION DUMP END******", relation.toString());
        function.insertIntoTableNtimes(tableName, dataNames, dataValues2);
    }

    @Test
    public void testSelectWhere() {
        Function function = new Function();
        function.createTable(tableName, dataNames, dataTypes);
        function.insertIntoTableNtimes(tableName, dataNames, dataValues1);
        function.insertIntoTableNtimes(tableName, dataNames, dataValues2);
        searchCondition.setTableArray(new String[]{tableName});
        String selection = function.selectFromTable(tableName, fieldListStar, searchCondition);
        assertEquals("2\t99\t100\t100\t\"A\"\t\n", selection);
    }

    @Test
    public void testSelectWhere2() {
        Function function = new Function();
        function.createTable(tableName, dataNames, dataTypes);
        function.insertIntoTableNtimes(tableName, dataNames, dataValues1);
        function.insertIntoTableNtimes(tableName, dataNames, dataValues2);
        searchCondition.setTableArray(new String[]{tableName});
        String selection = function.selectFromTable(tableName, fieldList, searchCondition);
        assertEquals("99\t100\t\n", selection);
    }

    @Test
    public void testDelete1() {
        Function function = new Function();
        function.createTable(tableName, dataNames, dataTypes);
        function.insertIntoTableNtimes(tableName, dataNames, dataValues1);
        function.insertIntoTableNtimes(tableName, dataNames, dataValues2);
        searchCondition.setTableArray(new String[]{tableName});
        function.deleteFromTable(tableName, new SearchCondition());
        String selection = function.selectFromTable(tableName, fieldListStar, searchCondition);
        assertEquals("", selection);
    }

    @Test
    public void testDelete2() {
        Function function = new Function();
        function.createTable(tableName, dataNames, dataTypes);
        function.insertIntoTableNtimes(tableName, dataNames, dataValues1);
        function.insertIntoTableNtimes(tableName, dataNames, dataValues2);
        searchCondition.setTableArray(new String[]{tableName});
        function.deleteFromTable(tableName, searchCondition);
        String selection = function.selectFromTable(tableName, fieldListStar, new SearchCondition());
        assertEquals("1\t99\t100\t100\t\"A\"\t\n", selection);
    }
}