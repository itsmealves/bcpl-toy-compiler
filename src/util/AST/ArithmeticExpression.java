package util.AST;

import checker.SemanticException;
import checker.Visitor;

import java.util.List;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class ArithmeticExpression extends FactorExpression {
    private final List<TermExpression> terms;
    private final List<TOperator> operators;

    public ArithmeticExpression(List<TermExpression> terms, List<TOperator> operators) {
        this.terms = terms;
        this.operators = operators;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Arithmetic Expression\n");

        s.append(terms.get(0).toString(level + 1));
        for(int i = 0; i < operators.size(); i++) {
            s.append(this.getSpaces(level + 1));
            s.append("Operator ");
            s.append(operators.get(i).toString() + '\n');
            s.append(terms.get(i + 1).toString(level + 1));
        }

        s.append(this.getSpaces(level));
        s.append("Arithmetic Expression End\n");
        return s.toString();
    }

    public List<TermExpression> getTerms() {
        return terms;
    }

    public List<TOperator> getOperators() {
        return operators;
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitArithmeticExpression(this, arg);
    }

    @Override
    public String toCode() {
        if(operators.size() == 0) {
            return terms.get(0).toCode();
        }

        StringBuilder s = new StringBuilder();
        s.append("((int) ");

        for(int i = 0; i < operators.size(); i++) {
            s.append(terms.get(i).toCode());
            s.append(operators.get(i).toCode());
        }
        s.append(terms.get(terms.size() - 1).toCode());
        s.append(")");
        return s.toString();
    }
}
