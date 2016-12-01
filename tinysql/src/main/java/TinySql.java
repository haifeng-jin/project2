import expression.Exp;
import function.Function;
import parser.ParseException;
import parser.Parser;

public class TinySql {
    public static void main(String[] args) {
        while (true) {
            try {
                Exp result = new Parser(System.in).statement();
                System.out.println(result);
                result.execute(new Function());
            } catch (ParseException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
