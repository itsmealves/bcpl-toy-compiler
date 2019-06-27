package util.AST;

import checker.SemanticException;
import checker.Visitor;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class BreakCommand extends Command {
    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Break Command\n");
        return s.toString();
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitBreakCommand(this, arg);
    }

    @Override
    public String toCode() {
        return "break;";
    }
}
