package expression;

import function.Function;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Expression {
    String type;
    String term1;
    String op;
    String term2;
    String[] tableArray;

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

    public void setTableArray(String[] tableArray) {
        this.tableArray = tableArray;
    }

    String getValue(ArrayList<String> tuple) throws Exception {
        if (op == null) {
            if (!isInteger(term1))
                return Function.getField(tableArray, term1, tuple);
            return term1;
        }
        try {
            if (!isInteger(term1)) {
                term1 = Function.getField(tableArray, term1, tuple);
            }
            if (!isInteger(term2)) {
                term2 = Function.getField(tableArray, term2, tuple);
            }
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

    private boolean isInteger(String term) {
        for (int i = 0; i < term.length(); i++) {
            if (term.charAt(i) > '9' || term.charAt(i) < '0')
                return false;
        }
        return true;
    }

    public String toString() {
        if (op == null) {
            return term1;
        }
        return term1 + " " + op + " " + term2;
    }
}
