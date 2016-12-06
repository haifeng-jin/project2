package expression;

import function.Function;

import java.util.ArrayList;

public class Insert extends Statement {
    String tableName;
    AttributeList list;
    InsertTuples tuples;

    public Insert(String tableName, AttributeList list, InsertTuples tuples) {
        this.tableName = tableName;
        this.list = list;
        this.tuples = tuples;
    }

    public void execute(Function function) {
        function.insertIntoTableNtimes(tableName, list.nameList, tuples.list.value);
    }

    public String toString() {
        return "INSERT INTO " + tableName + " (" + list.nameList + ") " + tuples.list;
    }

}
