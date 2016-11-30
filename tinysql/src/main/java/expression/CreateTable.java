package expression;

public class CreateTable extends Exp{
    String tableName;
    AttributeTypeList attributeTypeList;

    public CreateTable(String tableName, AttributeTypeList attributeTypeList) {
        this.tableName = tableName;
        this.attributeTypeList = attributeTypeList;
    }

    public void execute() {
        NameTypeList list = attributeTypeList.getNameTypeList();

    }

    public String toString() {
        return "create table: " + tableName + " (" + attributeTypeList + ")";
    }
}
