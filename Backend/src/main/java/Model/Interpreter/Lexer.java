package Model.Interpreter;

import java.util.*;
import java.util.stream.Collectors;

public class Lexer {

    public List<String> lexer(String code){

        List<String> tokens = new LinkedList<>();
        Scanner sc = new Scanner(code);

        while (sc.hasNextLine()) {//creating tokens list
            String line = sc.nextLine() + " \n";
            String[] arr = line.split(" ");
            tokens.addAll(Arrays.stream(arr).toList());;
        }
        sc.close();

        return tokens;
    }
}
