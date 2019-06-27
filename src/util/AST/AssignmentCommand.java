package util.AST;

import checker.SemanticException;
import checker.Visitor;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class AssignmentCommand extends Command {
    private final TIdentifier identifier;
    private final Expression expression;

    public AssignmentCommand(TIdentifier identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Assigment Command\n");

        s.append(this.getSpaces(level + 1));
        s.append(identifier.toString() + '\n');
        s.append(expression.toString(level + 1));

        s.append(this.getSpaces(level));
        s.append("Assigment Command End\n");
        return s.toString();
    }

    public TIdentifier getIdentifier() {
        return identifier;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitAssignmentCommand(this, arg);

    }

    @Override
    public String toCode() {
        StringBuilder s = new StringBuilder();
        s.append(identifier.toCode());
        s.append(" = ");
        s.append(expression.toCode());
        s.append(";\n");

        return s.toString();
    }
}
