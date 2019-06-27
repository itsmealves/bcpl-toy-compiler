package util.AST;

import checker.SemanticException;
import checker.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class ArgumentList extends AST {
    private List<Expression> expressions;

    public ArgumentList(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Argument List\n");

        for(Expression e : expressions)
            s.append(e.toString(level + 1));

        s.append(this.getSpaces(level));
        s.append("Argument List End\n");
        return s.toString();
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitArgumentList(this, arg);

    }

    @Override
    public String toCode() {
        StringBuilder s = new StringBuilder();

        for(int i = 0; i < expressions.size(); i++) {
            s.append(expressions.get(i).toCode());
            s.append(",");
        }

        if(expressions.size() > 0)
            s.deleteCharAt(s.lastIndexOf(","));

        return s.toString();
    }
}
