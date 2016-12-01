package expression;

import java.util.ArrayList;

public class SelectList {
    ArrayList<String> list;

    public SelectList(String a) {
        this.list = new ArrayList<String>();
        list.add(a);
    }

    public SelectList(SelectList list1, SelectList list2) {
        this.list = new ArrayList<String>(list1.list);
        this.list.addAll(list2.list);
    }

    public String toString() {
        return list.toString();
    }
}
