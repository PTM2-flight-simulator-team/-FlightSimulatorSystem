package Model.Interpreter.Errors;

public class VarIsAlreadyDefined extends Exception{
    protected String msg;

    public VarIsAlreadyDefined(String var) {
        msg = var + "is already defined";
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
