package checker;

import util.AST.*;

/**
 * Created by Gabriel Alves on 29/05/2017.
 */
public interface Visitor {
    Object visitProgram(Program p, Object o) throws SemanticException;

    // Commands
    Object visitAssignmentCommand(AssignmentCommand ac, Object o) throws SemanticException;
    Object visitCallCommand(CallCommand cc, Object o) throws SemanticException;
    Object visitIfCommand(IfCommand ic, Object o) throws SemanticException;
    Object visitTestCommand(TestCommand tc, Object o) throws SemanticException;
    Object visitWhileCommand(WhileCommand wc, Object o) throws SemanticException;
    Object visitVariableDeclarationCommand(VariableDeclarationCommand vdc, Object o) throws SemanticException;
    Object visitBreakCommand(BreakCommand bc, Object o) throws SemanticException;
    Object visitContinueCommand(ContinueCommand cc, Object o) throws SemanticException;
    Object visitWriteCommand(WriteCommand wc, Object o) throws SemanticException;
    Object visitReturnCommand(ReturnCommand rc, Object o) throws SemanticException;

    // Expressions
    Object visitExpression(Expression e, Object o) throws SemanticException;
    Object visitArithmeticExpression(ArithmeticExpression ae, Object o) throws SemanticException;
    Object visitTermExpression(TermExpression te, Object o) throws SemanticException;

    // Factors
    Object visitNumberFactor(NumberFactor nf, Object o);
    Object visitBooleanFactor(BooleanFactor bf, Object o);
    Object visitIdFactor(IdFactor idf, Object o) throws SemanticException;
    Object visitCallFactor(CallFactor cf, Object o) throws SemanticException;

    // Lists
    Object visitParametersList(ParametersList pl, Object o);
    Object visitArgumentList(ArgumentList al, Object o) throws SemanticException;

    // Declarations
    Object visitFunctionDeclaration(FunctionDeclaration fd, Object o) throws SemanticException;
    Object visitGlobalDeclaration(GlobalDeclaration gd, Object o) throws SemanticException;
    Object visitVariableDeclaration(VariableDeclaration vd, Object o) throws SemanticException;

    // Terminals
    Object visitTIdentifier(TIdentifier tid, Object o);
    Object visitTInteger(TInteger tin, Object o);
    Object visitTOperator(TOperator top, Object o);
    Object visitTType(TType tt, Object o);
    Object visitTBoolean(TBoolean tb, Object o);
}
