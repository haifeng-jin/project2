package expression;

import java.math.BigDecimal;

public class Expression {
    String type;
    String term1;
    String op;
    String term2;

    public Expression(String term1, String op, String term2) {
        this.term1 = term1;
        this.op = op;
        this.term2 = term2;
        this.type = "integer";
    }

    public Expression(String term1, String type) {
        this.term1 = term1;
        this.op = null;
        this.term2 = null;
        this.type = type;
    }

    String getValue() throws Exception {
        if (op == null) {
            return term1;
        }
        try {
            BigDecimal a = new BigDecimal(term1);
            BigDecimal b = new BigDecimal(term2);
            if (op.equals("+")) {
                return a.add(b).toString();
            }
            if (op.equals("-")) {
                return a.subtract(b).toString();
            }
            if (op.equals("*")) {
                return a.multiply(b).toString();
            }
        } catch (Exception e) {
            System.err.println("Error in Expression " + this.toString());
            e.printStackTrace();
        }
        throw new Exception("Operator not valid in Expression " + this.toString());
    }

    public String toString() {
        if (op == null) {
            return term1;
        }
        return term1 + " " + op + " " + term2;
    }
}
