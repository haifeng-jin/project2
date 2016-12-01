package expression;

import storageManager.Tuple;

import java.math.BigDecimal;

public class BooleanFactor implements Satisfiable{
    Expression exp1;
    String compOp;
    Expression exp2;

    public BooleanFactor(Expression exp1, String compOp, Expression exp2) {
        this.exp1 = exp1;
        this.compOp = compOp;
        this.exp2 = exp2;
    }

    public boolean satisfy(Tuple tuple) {
        try {
            String value1 = exp1.getValue();
            String value2 = exp2.getValue();
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

    public String toString() {
        return exp1 + " " + compOp + " " + exp2;
    }
}
