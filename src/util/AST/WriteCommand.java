package util.AST;

import checker.SemanticException;
import checker.Visitor;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class WriteCommand extends Command {
    private Expression expression;

    public WriteCommand(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Write Command\n");

        s.append(expression.toString(level + 1));

        s.append(this.getSpaces(level));
        s.append("Write Command End\n");
        return s.toString();
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitWriteCommand(this, arg);
    }

    @Override
    public String toCode() {
        return "System.out.println(" + expression.toCode() + ");";
    }
}
