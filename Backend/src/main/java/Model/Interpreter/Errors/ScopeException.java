package Model.Interpreter.Errors;

public class ScopeException extends Exception{
        protected String msg;

    public ScopeException() {
        msg = "missing '}' closing in scope";
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
