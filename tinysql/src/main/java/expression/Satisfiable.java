package expression;

import java.util.ArrayList;

interface Satisfiable {
    boolean satisfy(ArrayList<String> tuple);
    void setTableArray(String[] tableArray);
}
