package expression;


import java.util.ArrayList;

public class AttributeList {
    public ArrayList<String> nameList;

    public AttributeList(String nameList) {
        this.nameList = new ArrayList<String>();
        this.nameList.add(nameList);
    }

    public AttributeList(AttributeList list1, AttributeList list2) {
        this.nameList = new ArrayList<String>(list1.nameList);
        this.nameList.addAll(list2.nameList);
    }

}
