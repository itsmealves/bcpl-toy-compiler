package util.AST;

import checker.SemanticException;
import checker.Visitor;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class CallFactor extends FactorExpression {
    private TIdentifier identifier;
    private ArgumentList arguments;

    public CallFactor(TIdentifier identifier, ArgumentList arguments) {
        this.identifier = identifier;
        this.arguments = arguments;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Call Factor\n");

        s.append(this.getSpaces(level + 1));
        s.append(identifier.toString() + '\n');

        if(arguments != null)
            s.append(arguments.toString(level + 1));

        s.append(this.getSpaces(level));
        s.append("Call Factor End\n");
        return s.toString();
    }

    public TIdentifier getIdentifier() {
        return identifier;
    }

    public ArgumentList getArguments() {
        return arguments;
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitCallFactor(this, arg);

    }

    @Override
    public String toCode() {
        StringBuilder s = new StringBuilder();

        s.append(identifier.toCode());
        s.append("(");

        if(arguments != null)
            s.append(arguments.toCode());
        s.append(")\n");

        return s.toString();
    }
}
