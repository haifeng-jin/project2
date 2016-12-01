package expression;

import storageManager.Tuple;

import java.util.ArrayList;

public class SearchCondition {
    ArrayList<BooleanTerm> list;
    public boolean satisfy(Tuple tuple) {
        return tuple != null;
    }
}
