package expression;

import java.util.ArrayList;

public class TableList {
    ArrayList<String> list;

    public TableList(String a) {
        this.list = new ArrayList<String>();
        list.add(a);
    }

    public TableList(TableList list1, TableList list2) {
        this.list = new ArrayList<String>(list1.list);
        this.list.addAll(list2.list);
    }

    public String toString() {
        return list.toString();
    }
}
