package expression;

import function.Function;

import java.math.BigDecimal;
import java.util.ArrayList;

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

    public boolean satisfy(ArrayList<String> tuple) {
        try {
            String value1 = exp1.getValue(tuple);
            String value2 = exp2.getValue(tuple);
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

    public void setTableArray(String[] tableArray) {
        this.tableArray = tableArray;
        exp1.setTableArray(tableArray);
        exp2.setTableArray(tableArray);
    }

    public String toString() {
        return exp1 + " " + compOp + " " + exp2;
    }
}
