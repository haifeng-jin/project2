package expression;

import function.Function;

public class Select extends Statement{
    SelectList columnList;
    TableList tableList;
    boolean distinct;
    String orderBy;
    SearchCondition condition;

    public Select(SelectList columnList, TableList tableList, boolean distinct, String orderBy, SearchCondition condition) {
        this.columnList = columnList;
        this.tableList = tableList;
        this.distinct = distinct;
        this.orderBy = orderBy;
        this.condition = condition;
        condition.setTableArray(tableList.list.toArray(new String[]{}));
    }

    public void execute(Function function) {
        function.selectFromTable(tableList.list.get(0), condition);
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
