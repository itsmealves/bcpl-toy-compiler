package util.AST;

import checker.SemanticException;
import checker.Visitor;

import java.util.List;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class WhileCommand extends Command {

    private final Expression expression;
    private final List<Command> body;

    public WhileCommand(Expression expression, List<Command> body) {
        this.expression = expression;
        this.body = body;
    }

    public Expression getExpression() {
        return expression;
    }

    public List<Command> getBody() {
        return body;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("While Command\n");

        s.append(expression.toString(level + 1));
        for(Command c : body)
            s.append(c.toString(level + 1));

        s.append(this.getSpaces(level));
        s.append("While Command End\n");
        return s.toString();
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitWhileCommand(this, arg);
    }

    @Override
    public String toCode() {
        StringBuilder s = new StringBuilder();

        s.append("while(");
        s.append(expression.toCode());
        s.append(") {\n");

        for(Command c : body) {
            s.append(c.toCode());
            s.append("\n");
        }

        s.append("}\n");

        return s.toString();
    }
}
