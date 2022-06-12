package Model.Interpreter.Errors;

public class NullVar extends Exception{
    protected String msg;

    public NullVar(String var) {
        msg = var + "is null";
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
