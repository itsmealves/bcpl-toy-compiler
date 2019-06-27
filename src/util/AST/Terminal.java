package util.AST;

import scanner.Token;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public abstract class Terminal extends AST {
    private Token token;

    public Terminal(Token token) {
        this.token = token;
    }
    public Token getToken() {
        return token;
    }
    public abstract String toString();

    @Override
    public String toString(int level) {
        return this.toString();
    }
}
