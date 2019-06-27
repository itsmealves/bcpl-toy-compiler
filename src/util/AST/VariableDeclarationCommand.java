package util.AST;

import checker.SemanticException;
import checker.Visitor;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class VariableDeclarationCommand extends Command {
    private VariableDeclaration variableDeclaration;

    public VariableDeclarationCommand(VariableDeclaration variableDeclaration) {
        this.variableDeclaration = variableDeclaration;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Variable Declaration Command\n");
        s.append(variableDeclaration.toString(level + 1));
        s.append(this.getSpaces(level));
        s.append("Variable Declaration Command End\n");
        return s.toString();
    }

    public VariableDeclaration getVariableDeclaration() {
        return variableDeclaration;
    }

    @Override

    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitVariableDeclarationCommand(this, arg);
    }

    @Override
    public String toCode() {
        return variableDeclaration.toCode();
    }
}
