import expression.Statement;
import function.Function;
import parser.ParseException;
import parser.Parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

public class TinySql {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Please input a file name with path: ");
        Scanner scanner = new Scanner(System.in);
        String filename = scanner.nextLine();
        Scanner fileScanner = new Scanner(new FileInputStream("src/main/resources/test.txt"));
        Function function = new Function();
        while (fileScanner.hasNext()) {
            String statement = fileScanner.nextLine();
            System.out.println(statement);
            try {
                new Parser(new StringReader(statement)).statement().execute(function);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
