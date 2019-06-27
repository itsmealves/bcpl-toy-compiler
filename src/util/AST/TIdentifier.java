package util.AST;

import checker.Visitor;
import scanner.Token;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class TIdentifier extends Terminal {

    public TIdentifier(Token token) {
        super(token);
    }

    @Override
    public String toString() {
        return this.getToken().getSpelling();
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitTIdentifier(this, arg);
    }

    @Override
    public String toCode() {
        if(getToken().getSpelling().equals("main")) {
            return "bcpl_main";
        }

        return getToken().getSpelling().replace('.', '_');
    }
}
