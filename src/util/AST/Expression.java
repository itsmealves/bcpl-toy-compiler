package util.AST;

import checker.SemanticException;
import checker.Visitor;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class Expression extends AST {

    private final ArithmeticExpression left;
    private final ArithmeticExpression right;
    private final TOperator operator;

    public Expression(ArithmeticExpression left, ArithmeticExpression right, TOperator operator) {
        super();
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public TOperator getOperator() {
        return operator;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Expression\n");

        if(operator != null) {
            s.append(this.getSpaces(level + 1));
            s.append("Operator: ");
            s.append(operator.toString() + '\n');
        }

        s.append(left.toString(level + 1));

        if(right != null) {
            s.append(right.toString(level + 1));
        }

        s.append(this.getSpaces(level));
        s.append("Expression End\n");
        return s.toString();
    }

    public ArithmeticExpression getLeft() {
        return left;
    }

    public ArithmeticExpression getRight() {
        return right;
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitExpression(this, arg);

    }

    @Override
    public String toCode() {
        StringBuilder s = new StringBuilder();

        s.append(left.toCode());

        if(operator != null) {
            s.append(operator.toCode());
            s.append(right.toCode());
        }

        return s.toString();
    }
}
