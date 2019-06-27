package util.AST;

import checker.SemanticException;
import checker.Visitor;

import java.util.List;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class TermExpression extends AST {
    private List<FactorExpression> factors;
    private List<TOperator> operators;

    public TermExpression(List<FactorExpression> factors, List<TOperator> operators) {
        this.factors = factors;
        this.operators = operators;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Term Expression\n");

        s.append(factors.get(0).toString(level + 1));
        for(int i = 0; i < operators.size(); i++) {
            s.append(this.getSpaces(level + 1));
            s.append("Operator ");
            s.append(operators.get(i).toString() + '\n');
            s.append(factors.get(i + 1).toString(level + 1));
        }

        s.append(this.getSpaces(level));
        s.append("Term Expression End\n");
        return s.toString();
    }

    public List<FactorExpression> getFactors() {
        return factors;
    }

    public List<TOperator> getOperators() {
        return operators;
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitTermExpression(this, arg);
    }

    @Override
    public String toCode() {
        if(operators.size() == 0) {
            return factors.get(0).toCode();
        }

        StringBuilder s = new StringBuilder();
        s.append("((int) ");

        for(int i = 0; i < operators.size(); i++) {
            s.append(factors.get(i).toCode());
            s.append(operators.get(i).toCode());
        }
        s.append(factors.get(factors.size() - 1).toCode());
        s.append(")");
        return s.toString();
    }
}
