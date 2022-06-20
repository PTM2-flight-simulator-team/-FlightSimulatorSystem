package Model.Interpreter;

import CommonClasses.PlaneData;
import CommonClasses.PlaneVar;

public class Variable {//class that represent code var

    public double value;
    public String bindTo;//indicate if the var is bind to FG var

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
