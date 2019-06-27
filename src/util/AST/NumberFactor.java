package util.AST;

import checker.Visitor;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class NumberFactor extends FactorExpression {
    private TInteger number;

    public NumberFactor(TInteger tInteger) {
        this.number = tInteger;
    }

    public TInteger getNumber() {
        return number;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Number Factor\n");

        s.append(this.getSpaces(level + 1));
        s.append(number.toString() + '\n');

        s.append(this.getSpaces(level));
        s.append("Number Factor End\n");
        return s.toString();
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitNumberFactor(this, arg);
    }

    @Override
    public String toCode() {
        return number.toCode();
    }
}
