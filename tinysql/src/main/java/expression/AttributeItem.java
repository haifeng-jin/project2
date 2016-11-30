package expression;

public class AttributeItem extends Exp{
	String attributeName;
	String dataType;

	public AttributeItem(String attributeName, String dataType) {
		this.attributeName = attributeName;
		this.dataType = dataType;
	}

	public String toString() {
		return attributeName + " " + dataType;
	}
}

