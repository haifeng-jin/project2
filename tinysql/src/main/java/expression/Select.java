package expression;

import com.sun.deploy.util.StringUtils;
import function.Function;
import storageManager.Tuple;

import java.util.*;

public class Select extends Statement{
    SelectList columnList;
    TableList tableList;
    boolean distinct;
    String orderBy;
    SearchCondition condition;
    static int order;
    boolean out;
    ArrayList<ArrayList<String>> table;

    public Select(SelectList columnList, TableList tableList, boolean distinct, String orderBy, SearchCondition condition) {
        this.columnList = columnList;
        this.tableList = tableList;
        this.distinct = distinct;
        this.orderBy = orderBy;
        this.condition = condition;
        condition.setTableArray(tableList.list.toArray(new String[]{}));
        out = true;
    }

    public void execute(Function function) {
        if (!distinct && orderBy == null && tableList.list.size() == 1 && out) {
            function.selectFromTable(tableList.list.get(0), columnList.list, condition);
            return;
        }
        ArrayList<ArrayList<Tuple>> allTables = getAllTables(function);
        ArrayList<ArrayList<String>> jointTable = joinAllTables(allTables, 0);
        ArrayList<ArrayList<String>> filteredTable = filter(jointTable);
        if (orderBy != null) {
            order = Function.getFieldIndex(tableList.list.toArray(new String[0]), orderBy);
            Collections.sort(filteredTable,new Comparator<ArrayList<String>>() {
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    return o1.get(order).compareTo(o2.get(order));
                }
            });
        }
        ArrayList<ArrayList<String>> projectedTable = projection(filteredTable);
        if (distinct && orderBy == null) {
            makeDistinct(projectedTable);
        }
        if (distinct && orderBy != null) {
            for (int i = 0; i < projectedTable.size(); i++) {
                while (i + 1 < projectedTable.size()) {
                    if (tupleEqual(projectedTable.get(i), projectedTable.get(i + 1))) {
                        projectedTable.remove(i);
                    }
                }
            }
        }
        if (out)
            output(projectedTable);
        table = projectedTable;
    }

    private boolean tupleEqual(ArrayList<String> strings, ArrayList<String> strings1) {
        for (int i = 0; i < strings.size(); i++) {
            if (!strings.get(i).equals(strings1.get(i))) {
                return false;
            }
        }
        return true;
    }

    private void makeDistinct(ArrayList<ArrayList<String>> table) {
        ArrayList<String> outputList = new ArrayList<String>();
        for (ArrayList<String> tuple : table) {
            outputList.add(StringUtils.join(tuple, "\t"));
        }
        outputList = new ArrayList<String>(new HashSet<String>(outputList));
        table.clear();
        for (String tuple : outputList)
            table.add(new ArrayList<String>(Arrays.asList(tuple.split("\t"))));
    }

    private ArrayList<ArrayList<String>> projection(ArrayList<ArrayList<String>> filteredTable) {
        if (columnList.list.get(0).equals("*")) {
            return filteredTable;
        }
        ArrayList<Integer> columns = new ArrayList<Integer>();
        for (String column : columnList.list) {
            columns.add(Function.getFieldIndex(tableList.list.toArray(new String[0]), column));
        }
        ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
        for (ArrayList<String> tuple : filteredTable) {
            ret.add(projection(tuple, columns));
        }
        return ret;
    }

    private ArrayList<String> projection(ArrayList<String> tuple, ArrayList<Integer> columns) {
        ArrayList<String> ret = new ArrayList<String>();
        for (Integer i : columns) {
            ret.add(tuple.get(i));
        }
        return ret;
    }

    private void output(ArrayList<ArrayList<String>> filteredTable) {
        for (ArrayList<String> tuple : filteredTable) {
            for (String field: tuple)
                System.out.print(field + "\t");
            System.out.println();
        }
    }

    private ArrayList<ArrayList<String>> filter(ArrayList<ArrayList<String>> jointTable) {
        if (condition.isEmpty) {
            return jointTable;
        }
        ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
        for (ArrayList<String> tuple : jointTable) {
            if (condition.satisfy(tuple)) {
                ret.add(tuple);
            }
        }
        return ret;
    }

    private ArrayList<ArrayList<Tuple>> getAllTables(Function function) {
        ArrayList<ArrayList<Tuple>> ret = new ArrayList<ArrayList<Tuple>>();
        for (String tableName : tableList.list) {
            ret.add(function.getAllTuplesFromTable(tableName));
        }
        return ret;
    }


    private ArrayList<ArrayList<String>> joinAllTables(ArrayList<ArrayList<Tuple>> allTuples, int start) {
        ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
        if (start == allTuples.size()) {
            ret.add(new ArrayList<String>());
            return ret;
        }
        ArrayList<Tuple> currentTable = allTuples.get(start);
        ArrayList<ArrayList<String>> restTables = joinAllTables(allTuples, start + 1);
        for (Tuple tuple : currentTable) {
            ArrayList<String> tupleString = Function.tupleToString(tuple);
            for (ArrayList<String> restString : restTables) {
                ArrayList<String> newTuple = new ArrayList<String>();
                newTuple.addAll(tupleString);
                newTuple.addAll(restString);
                ret.add(newTuple);
            }
        }
        return ret;
    }

    public String toString() {
        String ret = "SELECT ";
        if (distinct)
            ret += "DISTINCT ";
        ret += columnList.toString() + " FROM " + tableList.toString() + " ";
        if (condition != null)
            ret += condition.toString() + " ";
        if (orderBy != null)
            ret += "ORDER BY " + orderBy;
        return ret;
    }
}
