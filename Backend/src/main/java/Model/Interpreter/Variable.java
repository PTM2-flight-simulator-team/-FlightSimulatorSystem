package Model.Interpreter;

public class Variable {

    public double value;
    public String bindTo;

    public Variable(double value) {
        this.value = value;
        this.bindTo = null;
    }
    public Variable(String bind, double value) {
        this.value = value;
        this.bindTo = bind;
    }

    public double getValue() {
        return value;
    }

    public String getBindTo() {
        return bindTo;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
