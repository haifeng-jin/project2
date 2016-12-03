package expression;

import function.Function;

public class Delete extends Statement {
    String tableName;
    SearchCondition condition;

    public Delete(String tableName, SearchCondition condition) {
        this.tableName = tableName;
        this.condition = condition;
        condition.setTableArray(new String[]{tableName});
    }

    public String toString() {
        String ret = "DELETE FROM " + tableName;
        if (condition != null) {
            ret += "WHERE " + condition.toString();
        }
        return ret;
    }

    public void execute(Function function) {
        function.deleteFromTable(tableName, condition);
    }
}
