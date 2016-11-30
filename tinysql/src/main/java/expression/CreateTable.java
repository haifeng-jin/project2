package expression;

import function.Function;

public class CreateTable extends Exp{
    String tableName;
    AttributeTypeList attributeTypeList;

    public CreateTable(String tableName, AttributeTypeList attributeTypeList) {
        this.tableName = tableName;
        this.attributeTypeList = attributeTypeList;
    }

    public void execute() {
        NameTypeList list = attributeTypeList.getNameTypeList();
        Function function = new Function();
        function.createTable(tableName, list.nameList, list.typeList);
    }

    public String toString() {
        return "create table: " + tableName + " (" + attributeTypeList + ")";
    }
}
