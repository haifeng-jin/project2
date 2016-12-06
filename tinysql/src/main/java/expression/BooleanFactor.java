package expression;

import function.Function;
import storageManager.Tuple;

import java.math.BigDecimal;

public class BooleanFactor implements Satisfiable{
    Expression exp1;
    String compOp;
    Expression exp2;
    String[] tableArray;

    public BooleanFactor(Expression exp1, String compOp, Expression exp2) {
        this.exp1 = exp1;
        this.compOp = compOp;
        this.exp2 = exp2;
    }

    public boolean satisfy(Tuple tuple) {
        try {
            String value1 = getValue(exp1, tuple);
            String value2 = getValue(exp2, tuple);
            if (compOp.equals("=")) {
                return value1.equals(value2);
            }
            BigDecimal a = new BigDecimal(value1);
            BigDecimal b = new BigDecimal(value2);
            if (compOp.equals(">")) {
                return a.compareTo(b) > 0;
            }
            if (compOp.equals("<")) {
                return a.compareTo(b) < 0;
            }
            throw new Exception("Comp not valid in BooleanFactor " + this.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getValue(Expression exp, Tuple tuple) throws Exception {
        if (exp.type.equals("integer")) {
            return exp.getValue(tuple);
        }
        if (exp.type.equals("literal")) {
            return exp.getValue(tuple);
        }
        String tableName;
        String columnName;
        if (exp.type.equals("column_name")) {
            tableName = exp.getValue(tuple).split(".")[0];
            columnName = exp.getValue(tuple).split(".")[1];
        } else {
            columnName = exp.getValue(tuple);
            for (String name : tableArray) {
                if (Function.schema_manager.getRelation(name).getSchema().getFieldNames().contains(columnName)) {
                    tableName = name;
                    break;
                }
            }
        }
        return tuple.getField(columnName).toString();
    }

    public void setTableArray(String[] tableArray) {
        this.tableArray = tableArray;
        exp1.setTableArray(tableArray);
        exp2.setTableArray(tableArray);
    }

    public String toString() {
        return exp1 + " " + compOp + " " + exp2;
    }
}
