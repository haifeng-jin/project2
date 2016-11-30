package expression;

import java.util.ArrayList;

public class AttributeTypeList extends Exp{
    public AttributeItem item;
    AttributeTypeList list;

    public AttributeTypeList(AttributeItem item, AttributeTypeList list) {
        this.item = item;
        this.list = list;
    }

    public NameTypeList getNameTypeList() {
        AttributeTypeList list = this;
        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<String> typeList = new ArrayList<String>();
        while (list != null) {
            nameList.add(list.item.attributeName);
            typeList.add(list.item.dataType);
            list = list.list;
        }
        return new NameTypeList(nameList, typeList);
    }

    public String toString() {
        if (list == null)
            return item.toString();
        return list.toString() + ", " + item.toString();
    }
}

