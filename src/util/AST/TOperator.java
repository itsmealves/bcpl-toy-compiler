package util.AST;

import checker.Visitor;
import scanner.Token;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class TOperator extends Terminal {
    public TOperator(Token token) {
        super(token);
    }

    @Override
    public String toString() {
        return this.getToken().getSpelling();
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitTOperator(this, arg);
    }

    @Override
    public String toCode() {
        if(getToken().getSpelling().equals("="))
            return "==";

        return getToken().getSpelling();
    }
}
