package util.AST;

import checker.SemanticException;
import checker.Visitor;

import java.util.List;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class TestCommand extends Command {
    private final Expression expression;
    private final List<Command> body;
    private final List<Command> elseBody;

    public TestCommand(Expression expression, List<Command> body, List<Command> elseBody) {
        this.expression = expression;
        this.body = body;
        this.elseBody = elseBody;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Test Command\n");

        s.append(expression.toString(level + 1));
        s.append(this.getSpaces(level));
        s.append("Body\n");
        for(Command c : body)
            s.append(c.toString(level + 1));

        s.append(this.getSpaces(level));
        s.append("Else Body\n");
        for(Command c : elseBody)
            s.append(c.toString(level + 1));

        s.append(this.getSpaces(level));
        s.append("Test Command End\n");
        return s.toString();
    }

    public Expression getExpression() {
        return expression;
    }

    public List<Command> getBody() {
        return body;
    }

    public List<Command> getElseBody() {
        return elseBody;
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitTestCommand(this, arg);

    }

    @Override
    public String toCode() {
        StringBuilder s = new StringBuilder();

        s.append("if(");
        s.append(expression.toCode());
        s.append(") {\n");

        for(Command c : body) {
            s.append(c.toCode());
            s.append("\n");
        }

        s.append("}\nelse {\n");

        for(Command c : elseBody) {
            s.append(c.toCode());
            s.append("\n");
        }

        s.append("}\n");

        return s.toString();
    }
}
