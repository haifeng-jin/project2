import function.Function;
import parser.ParseException;
import parser.Parser;

import java.io.StringReader;

public class Experiment {
    static void createTable(Function function) {
        try {
            new Parser(new StringReader("CREATE TABLE t1 (a1 INT)")).statement().execute(function);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    static void start() {
        Function.disk.resetDiskIOs();
        Function.disk.resetDiskTimer();

    }

    static void print() {
        System.out.println(Function.disk.getDiskTimer());
    }

    public static void main(String[] args) {
        insert();
        delete();
    }

    private static void insert() {
        Function f = new Function();
        createTable(f);
        String statements = "INSERT INTO t1 (a1) VALUES (1)";
        start();
        for (int i = 0; i < 10; i++) {
            try {
                new Parser(new StringReader(statements)).statement().execute(f);
                print();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private static void delete() {
        Function f = new Function();
        createTable(f);
        String statements = "INSERT INTO t1 (a1) VALUES (1)";
        String d = "DELETE FROM t1";
        start();
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < j; i++) {
                try {
                    new Parser(new StringReader(statements)).statement().execute(f);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            try {
                new Parser(new StringReader(d)).statement().execute(f);
                print();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
}
