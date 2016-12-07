package expression;

import function.Function;

import java.util.ArrayList;

public class Insert extends Statement {
    String tableName;
    AttributeList list;
    InsertTuples tuples;
    Select select = null;

    public Insert(String tableName, AttributeList list, InsertTuples tuples) {
        this.tableName = tableName;
        this.list = list;
        this.tuples = tuples;
    }

    public void setSelect(Select select) {
        this.select = select;
    }

    public void execute(Function function) {
        if (select == null) {
            function.insertIntoTableNtimes(tableName, list.nameList, tuples.list.value);
            return;
        }
        select.out = false;
        select.execute(function);
        for (ArrayList<String> tuple: select.table) {
            function.insertIntoTableNtimes(tableName, list.nameList, tuple);
        }
    }

    public String toString() {
        return "INSERT INTO " + tableName + " (" + list.nameList + ") " + tuples.list;
    }

}
