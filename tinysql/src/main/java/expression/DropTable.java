package expression;


import function.Function;

public class DropTable extends Statement {
    String tableName;

    public DropTable(String tableName) {
        this.tableName = tableName;
    }

    public String toString() {
        return "drop table: " + tableName + "";
    }

    public void execute(Function function) {
        function.dropTable(tableName);
    }
}

