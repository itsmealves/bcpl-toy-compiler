package util.AST;

import checker.Visitor;
import scanner.Token;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class TBoolean extends Terminal {
    public TBoolean(Token token) {
        super(token);
    }

    @Override
    public String toString() {
        return this.getToken().getKind().name();
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitTBoolean(this, arg);
    }

    @Override
    public String toCode() {
        return getToken().getSpelling().toLowerCase();
    }
}
