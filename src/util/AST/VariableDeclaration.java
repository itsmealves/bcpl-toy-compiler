package util.AST;

import checker.SemanticException;
import checker.Visitor;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class VariableDeclaration extends AST {
    private TType type;
    private TIdentifier identifier;
    private Expression expression;

    public VariableDeclaration(TType type, TIdentifier identifier, Expression expression) {
        this.type = type;
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append(type.toString());
        s.append(" ");
        s.append(identifier.toString());
        s.append(" receives: \n");
        s.append(expression.toString(level + 1));
        s.append(this.getSpaces(level));
        s.append("Variable Declaration End\n");

        return s.toString();
    }

    public TType getType() {
        return type;
    }

    public TIdentifier getIdentifier() {
        return identifier;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitVariableDeclaration(this, arg);
    }

    @Override
    public String toCode() {
        StringBuilder s = new StringBuilder();

        s.append(type.toCode());
        s.append(" ");
        s.append(identifier.toCode());
        s.append(" = ");
        s.append(expression.toCode());
        s.append(";\n");

        return s.toString();
    }
}
