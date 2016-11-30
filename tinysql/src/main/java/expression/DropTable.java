package expression;


public class DropTable extends Exp{
    String tableName;
    public DropTable(String tableName) {
        this.tableName = tableName;
    }
    public String toString() {return "drop table: " + tableName + "";}
}

