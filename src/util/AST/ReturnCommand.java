package util.AST;

import checker.SemanticException;
import checker.Visitor;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class ReturnCommand extends Command {
    private Expression expression;

    public ReturnCommand(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Return Command\n");
        s.append(expression.toString(level + 1));
        s.append(this.getSpaces(level));
        s.append("Return Command End\n");
        return s.toString();
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitReturnCommand(this, arg);
    }

    @Override
    public String toCode() {
        return "return " + expression.toCode() + ";";
    }
}
