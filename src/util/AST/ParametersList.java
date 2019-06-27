package util.AST;

import checker.Visitor;

import java.util.List;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class ParametersList extends AST {
    private List<TType> types;
    private final List<TIdentifier> identifiers;

    public ParametersList(List<TType> types, List<TIdentifier> identifiers) {
        this.types = types;
        this.identifiers = identifiers;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Parameters List\n");

        for(int i = 0; i < types.size(); i++) {
            s.append(types.get(i).toString());
            s.append(' ');
            s.append(identifiers.get(i).toString() + '\n');
        }

        s.append(this.getSpaces(level));
        s.append("Paramenters List End\n");
        return s.toString();
    }

    public List<TType> getTypes() {
        return types;
    }

    public List<TIdentifier> getIdentifiers() {
        return identifiers;
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitParametersList(this, arg);

    }

    @Override
    public String toCode() {
        StringBuilder s = new StringBuilder();

        for(int i = 0; i < types.size(); i++) {
            s.append(types.get(i).toCode());
            s.append(" ");
            s.append(identifiers.get(i).toCode());
            s.append(",");
        }

        if(types.size() > 0)
            s.deleteCharAt(s.lastIndexOf(","));

        return s.toString();
    }
}
