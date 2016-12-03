package expression;

import storageManager.Tuple;

import java.util.ArrayList;

public class SearchCondition implements Satisfiable{
    ArrayList<BooleanTerm> list;
    public boolean isEmpty;
    private String[] tableArray;

    public SearchCondition() {
        isEmpty = true;
    }

    public SearchCondition(BooleanTerm term) {
        this.list = new ArrayList<BooleanTerm>();
        this.list.add(term);
        isEmpty = false;
    }

    public SearchCondition(SearchCondition condition1, SearchCondition condition2) {
        this.list = new ArrayList<BooleanTerm>(condition1.list);
        this.list.addAll(condition2.list);
        isEmpty = false;
    }

    public boolean satisfy(Tuple tuple) {
        for (BooleanTerm term : list) {
            if (!term.satisfy(tuple)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return list.toString();
    }

    public void setTableArray(String[] tableArray) {
        this.tableArray = tableArray;
    }
}
