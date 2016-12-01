package expression;

import java.util.ArrayList;

public class ValueList {
    public ArrayList<String> value;

    public ValueList(String value) {
        this.value = new ArrayList<String>();
        this.value.add(value);
    }

    public ValueList(ValueList list1, ValueList list2) {
        this.value = new ArrayList<String>(list1.value);
        this.value.addAll(list2.value);
    }
}
