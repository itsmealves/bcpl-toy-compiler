package util.AST;

import checker.Visitor;
import scanner.Token;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class TInteger extends Terminal {
    public TInteger(Token token) {
        super(token);
    }

    @Override
    public String toString() {
        return this.getToken().getSpelling();
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitTInteger(this, arg);
    }

    @Override
    public String toCode() {
        return getToken().getSpelling();
    }
}
