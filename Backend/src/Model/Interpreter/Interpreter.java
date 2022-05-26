package Model.Interpreter;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {

    String code;
    Lexer lexer;
    Parser parser;
    Util maps;

    public Interpreter(String code) {
        this.code = code;
    }

    public void interpret(){}

}
