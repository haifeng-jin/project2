package expression;

import function.Function;

public class CreateTable extends Statement {
    String tableName;
    public AttributeTypeList list;

    public CreateTable(String tableName, AttributeTypeList list) {
        this.tableName = tableName;
        this.list = list;
    }

    public void execute(Function function) {
        function.createTable(tableName, list.nameList, list.typeList);
    }

    public String toString() {
        return "CREATE TABLE " + tableName + " (" + list + ")";
    }
}
