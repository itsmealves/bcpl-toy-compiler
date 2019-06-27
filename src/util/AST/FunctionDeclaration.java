package util.AST;

import checker.SemanticException;
import checker.Visitor;

import java.util.List;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class FunctionDeclaration extends AST {
    private TIdentifier identifier;
    private ParametersList parametersList;
    private TType returnType;
    private List<Command> commands;

    public FunctionDeclaration(TIdentifier identifier, ParametersList paramentersList, TType returnType, List<Command> commands) {
        this.identifier = identifier;
        this.parametersList = paramentersList;
        this.returnType = returnType;
        this.commands = commands;
    }
    public TIdentifier getIdentifier() {
        return identifier;
    }

    public ParametersList getParametersList() {
        return parametersList;
    }

    public TType getReturnType() {
        return returnType;
    }

    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        String type;
        s.append(this.getSpaces(level));

        if(this.getReturnType() == null)
            type = "Procedure";
        else
            type = "Function of " + this.returnType.toString();

        s.append(type);
        s.append(" ");
        s.append(this.getIdentifier().toString());
        s.append('\n');

        for(Command c : commands)
            s.append(c.toString(level + 1));

        s.append(this.getSpaces(level));
        s.append(type + " End\n");
        return s.toString();
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitFunctionDeclaration(this, arg);
    }

    @Override
    public String toCode() {
        StringBuilder s = new StringBuilder();

        s.append("public static ");

        if(returnType != null) {
            s.append(returnType.toCode());
        }
        else {
            s.append("void");
        }

        s.append(" ");
        s.append(identifier.toCode());
        s.append("(");

        if(parametersList != null)
            s.append(parametersList.toCode());

        s.append(") {\n");

        for(Command c : commands) {
            s.append(c.toCode());
            s.append("\n");
        }

        s.append("}\n");

        return s.toString();
    }
}
