package util.AST;

import checker.Visitor;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class BooleanFactor extends FactorExpression {
    private TBoolean value;

    public BooleanFactor(TBoolean tBoolean) {
        value = tBoolean;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Boolean Factor\n");

        s.append(this.getSpaces(level + 1));
        s.append(value.toString() + '\n');

        s.append(this.getSpaces(level));
        s.append("Boolean Factor End\n");
        return s.toString();
    }

    public TBoolean getValue() {
        return value;
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitBooleanFactor(this, arg);

    }

    @Override
    public String toCode() {
        return value.toCode();
    }
}
