package util.AST;

import checker.SemanticException;
import checker.Visitor;

import java.util.List;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class GlobalDeclaration extends AST {
    private List<VariableDeclaration> variables;

    public GlobalDeclaration(List<VariableDeclaration> globalVars) {
        this.variables = globalVars;
    }

    public List<VariableDeclaration> getVariables() {
        return variables;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Global Start\n");

        for(VariableDeclaration v : variables)
            s.append(v.toString(level + 1));

        s.append(this.getSpaces(level));
        s.append("Global End\n");
        return s.toString();
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitGlobalDeclaration(this, arg);
    }

    @Override
    public String toCode() {
        StringBuilder s = new StringBuilder();

        for(VariableDeclaration d : variables) {
            s.append("static ");
            s.append(d.toCode());
            s.append("\n");
        }

        return s.toString();
    }
}
