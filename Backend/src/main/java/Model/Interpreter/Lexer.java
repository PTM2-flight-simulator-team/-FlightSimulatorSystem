package Model.Interpreter;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Lexer {

    public List<String> lexer(String code){

        List<String> tokens = new LinkedList<>();
        Scanner sc = new Scanner(code);

        while (sc.hasNext()) tokens.add(sc.next());
        sc.close();

        return tokens;
    }
}
