package expression;

import function.Function;

import java.util.ArrayList;

public class Insert extends Exp{
    String tableName;
    AttributeList list;
    InsertTuples tuples;

    public Insert(String tableName, AttributeList list, InsertTuples tuples) {
        this.tableName = tableName;
        this.list = list;
        this.tuples = tuples;
    }

    public void execute(Function function) {
        ArrayList<ArrayList<String>> valueList = new ArrayList<ArrayList<String>>();
        valueList.add(tuples.list.value);
        function.insertIntoTableNtimes(tableName, list.nameList, valueList);
    }

    public String toString() {
        return "INSERT INTO " + tableName + " (" + list.nameList + ") " + tuples.list;
    }

}
