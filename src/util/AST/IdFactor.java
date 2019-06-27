package util.AST;

import checker.SemanticException;
import checker.Visitor;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class IdFactor extends FactorExpression {
    private TIdentifier identifier;

    public IdFactor(TIdentifier tIdentifier) {
        identifier = tIdentifier;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Id Factor\n");

        s.append(this.getSpaces(level + 1));
        s.append(identifier.toString() + '\n');

        s.append(this.getSpaces(level));
        s.append("Id Factor End\n");
        return s.toString();
    }

    public TIdentifier getIdentifier() {
        return identifier;
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitIdFactor(this, arg);
    }

    @Override
    public String toCode() {
        return identifier.toCode();
    }
}
