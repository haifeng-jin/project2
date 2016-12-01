package expression;

import java.util.ArrayList;

public class AttributeTypeList {
    public ArrayList<String> nameList;
    public ArrayList<String> typeList;

    public AttributeTypeList(String nameList, String typeList) {
        this.nameList = new ArrayList<String>();
        this.typeList = new ArrayList<String>();
        this.nameList.add(nameList);
        this.typeList.add(typeList);
    }

    public AttributeTypeList(AttributeTypeList list1, AttributeTypeList list2) {
        this.nameList = new ArrayList<String>(list1.nameList);
        this.typeList = new ArrayList<String>(list1.typeList);
        this.nameList.addAll(list2.nameList);
        this.typeList.addAll(list2.typeList);
    }

    public String toString() {
        return  "nameList: " + nameList + " typeList: " + typeList;
    }
}

