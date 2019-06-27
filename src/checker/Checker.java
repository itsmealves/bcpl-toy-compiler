package checker;

import com.sun.deploy.util.SystemUtils;
import compiler.Properties;
import parser.GrammarSymbols;
import scanner.Token;
import util.AST.*;
import util.symbolsTable.IdentificationTable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriel Alves on 29/05/2017.
 */
public class Checker implements Visitor {

    private IdentificationTable identificationTable;

    public void check(AST ast) throws SemanticException {
        identificationTable = new IdentificationTable();
        ast.visit(this, null);
    }

    @Override
    public Object visitProgram(Program p, Object o) throws SemanticException {
        boolean startFound = false;
        String programException = "Entry point missing:\n\t" +
                "Program does not have an entry point " +
                "(function start returning INT)";

        if(p.getGlobalVars() != null)
            p.getGlobalVars().visit(this, null);

        if(p.getFunctions().size() < 1)
            throw new SemanticException(programException);

        for(FunctionDeclaration f : p.getFunctions()) {
            Token tId = f.getIdentifier().getToken();
            Token tTp = null;

            if(f.getReturnType() != null)
                tTp = f.getReturnType().getToken();

            startFound = startFound ||
                    (tId.getSpelling().equals("start") &&
                            tTp != null &&
                            tTp.getKind().equals(GrammarSymbols.INTEGER));

            f.visit(this, null);
        }

        if(!startFound)
            throw new SemanticException(programException);


        return null;
    }

    @Override
    public Object visitAssignmentCommand(AssignmentCommand ac, Object o) throws SemanticException {
        Token t = ac.getIdentifier().getToken();
        Type variableType;
        Type expressionType = (Type) ac.getExpression().visit(this, null);

        AST idAst = identificationTable.retrieve(t.getSpelling());
        boolean isCorrect = idAst != null && idAst instanceof VariableDeclaration;

        if(!isCorrect)
            throw new SemanticException("Identification error:\n\t" +
                    t.getSpelling() + "is not assignable.\n\t" +
                    "Error at line " + t.getLine()
            );


        VariableDeclaration vd = (VariableDeclaration) idAst;
        Token type = vd.getType().getToken();

        if(type.getKind().equals(GrammarSymbols.INTEGER))
            variableType = Type.Integer;
        else
            variableType = Type.Boolean;

        if(!variableType.equals(expressionType)) {
            throw new SemanticException("Type mismatch:\n\t" +
                    t.getSpelling() + " of type " + variableType.name() +
                    " against an expression of type " + expressionType.name() +
                    "\n\tError at line " + t.getLine()
            );
        }


        return null;
    }

    @Override
    public Object visitCallCommand(CallCommand cc, Object o) throws SemanticException {
        FunctionDeclaration fd;
        FunctionListAttributes argumentsAttributes = null;
        FunctionListAttributes parametersAttributes = null;
        Token t = cc.getIdentifier().getToken();
        AST id = identificationTable.retrieve(t.getSpelling());
        boolean isCorrect = id != null && id instanceof FunctionDeclaration;
        String listsException = "Call error:\n\t" +
                "Parameters list and arguments list doesn't match.\n\t" +
                "It is expected that they have the same size and types.\n\t" +
                "Error at line " + t.getLine();

        if(!isCorrect)
            throw new SemanticException("Call error:\n\t" +
                "Trying to call " + t .getSpelling() + " which is not a function or" +
                " has never been declared.\n\tError at line " + t.getLine()
            );

        fd = (FunctionDeclaration) id;

        if(cc.getArguments() != null)
            argumentsAttributes = (FunctionListAttributes) cc.getArguments().visit(this, null);

        if(fd.getParametersList() != null)
            parametersAttributes = (FunctionListAttributes) fd.getParametersList().visit(this, null);

        if(argumentsAttributes != null && parametersAttributes != null) {
            if(!argumentsAttributes.equals(parametersAttributes))
                throw new SemanticException(listsException);
        }
        else if((argumentsAttributes == null && parametersAttributes != null) ||
                (parametersAttributes == null && argumentsAttributes != null)) {
            throw new SemanticException(listsException);
        }
        return null;
    }

    @Override
    public Object visitIfCommand(IfCommand ic, Object o) throws SemanticException {
        Type type = (Type) ic.getExpression().visit(this, null);
        if(!type.equals(Type.Boolean))
            throw new SemanticException("Type mismatch\n\t" +
                    "IF expects a boolean expression to evaluate."
            );

        identificationTable.openScope();
        for(Command c : ic.getBody())
            c.visit(this, o);
        identificationTable.closeScope();

        return null;
    }

    @Override
    public Object visitTestCommand(TestCommand tc, Object o) throws SemanticException {
        Type type = (Type) tc.getExpression().visit(this, null);
        Type returnType = null;
        boolean bodyHasReturnCommand = false;
        boolean elseHasReturnCommand = false;
        boolean commandsCoverReturnCommand = false;
        ReturnControl commandsControl = null;
        String returnMixedException = "Type mismatch:\n\t" +
                "Found different RETURN clauses returning expressions of different types.";

        if(!type.equals(Type.Boolean))
            throw new SemanticException("Type mismatch:\n\t" +
                    "TEST expects a boolean expression to evaluate.");

        identificationTable.openScope();
        for(Command c : tc.getBody()) {
            Object ret = c.visit(this, o);

            if(ret instanceof ReturnControl) {
                commandsControl = (ReturnControl) ret;
            }
            else {
                commandsControl = null;
            }

            if(c instanceof ReturnCommand) {
                bodyHasReturnCommand = true;
                ReturnCommand r = (ReturnCommand) c;

                if(returnType == null) {
                    returnType = (Type) r.getExpression().visit(this, null);
                }
                else {
                    Type newType = (Type) ((ReturnCommand) c).getExpression().visit(this, null);
                    if(!newType.equals(returnType)) {
                        throw new SemanticException(returnMixedException);
                    }
                }
            }

            if(commandsControl != null && commandsControl.isCoverabilityTest() && returnType != null) {
                commandsCoverReturnCommand = commandsCoverReturnCommand || commandsControl.isCoverabilityTest();
                if(!commandsControl.getExpressionType().equals(returnType)) {
                    throw new SemanticException(returnMixedException);
                }
            }
        }
        identificationTable.closeScope();

        identificationTable.openScope();
        for(Command c : tc.getElseBody()) {
            Object ret = c.visit(this, o);

            if(ret instanceof ReturnControl) {
                commandsControl = (ReturnControl) ret;
            }
            else {
                commandsControl = null;
            }

            if(c instanceof ReturnCommand) {
                elseHasReturnCommand = true;
                ReturnCommand r = (ReturnCommand) c;

                if(returnType == null) {
                    returnType = (Type) r.getExpression().visit(this, null);
                }
                else {
                    Type newType = (Type) ((ReturnCommand) c).getExpression().visit(this, null);
                    if(!newType.equals(returnType)) {
                        throw new SemanticException(returnMixedException);
                    }
                }
            }

            if(commandsControl != null && commandsControl.isCoverabilityTest() && returnType != null) {
                commandsCoverReturnCommand = commandsCoverReturnCommand || commandsControl.isCoverabilityTest();
                if(!commandsControl.getExpressionType().equals(returnType)) {
                    throw new SemanticException(returnMixedException);
                }
            }
        }
        identificationTable.closeScope();

        return new ReturnControl((bodyHasReturnCommand && elseHasReturnCommand) ||
                commandsCoverReturnCommand, returnType);
    }

    @Override
    public Object visitWhileCommand(WhileCommand wc, Object o) throws SemanticException {
        Type type = (Type) wc.getExpression().visit(this, null);
        boolean hasReturnCommand = false;
        boolean commandsCoverReturnCommand = false;
        Type returnType = null;
        ReturnControl commandsControl = null;
        String returnMixedException = "Type mismatch:\n\t" +
                "Found different RETURN clauses returning expressions of different types.";


        if(!type.equals(Type.Boolean))
            throw new SemanticException("Type mismatch:\n\t" +
                    "WHILE expects a boolean expression to evaluate.");

        identificationTable.openScope();
        for(Command c : wc.getBody()) {
            Object ret = c.visit(this, wc);

            if(ret instanceof ReturnControl) {
                commandsControl = (ReturnControl) ret;
            }
            else {
                commandsControl = null;
            }

            if(c instanceof ReturnCommand) {
                hasReturnCommand = true;
                ReturnCommand r = (ReturnCommand) c;

                if(returnType == null) {
                    returnType = (Type) r.getExpression().visit(this, null);
                }
                else {
                    Type newType = (Type) ((ReturnCommand) c).getExpression().visit(this, null);
                    if(!newType.equals(returnType)) {
                        throw new SemanticException(returnMixedException);
                    }
                }
            }

            if(commandsControl != null && commandsControl.isCoverabilityTest() && returnType != null) {
                commandsCoverReturnCommand = commandsCoverReturnCommand || commandsControl.isCoverabilityTest();
                if(!commandsControl.getExpressionType().equals(returnType)) {
                    throw new SemanticException(returnMixedException);
                }
            }
        }
        identificationTable.closeScope();

        return new ReturnControl(hasReturnCommand || commandsCoverReturnCommand,
                returnType);
    }

    @Override
    public Object visitVariableDeclarationCommand(VariableDeclarationCommand vdc, Object o) throws SemanticException {
        VariableDeclaration vd = vdc.getVariableDeclaration();
        vd.visit(this, null);
        return null;
    }

    @Override
    public Object visitBreakCommand(BreakCommand bc, Object o) throws SemanticException {
        if(!(o instanceof WhileCommand))
            throw new SemanticException("Command confusion:\n\t" +
                "Found a BREAK command outside a WHILE block. It doesn't make sense at all.");

        return null;
    }

    @Override
    public Object visitContinueCommand(ContinueCommand cc, Object o) throws SemanticException {
        if(!(o instanceof WhileCommand))
            throw new SemanticException("Command confusion:\n\t" +
                    "Found a CONTINUE command outside a WHILE block. It doesn't make sense at all.");

        return null;
    }

    @Override
    public Object visitWriteCommand(WriteCommand wc, Object o) throws SemanticException {
        wc.getExpression().visit(this, null);
        return null;
    }

    @Override
    public Object visitReturnCommand(ReturnCommand rc, Object o) throws SemanticException {
        return rc.getExpression().visit(this, null);
    }

    @Override
    public Object visitExpression(Expression e, Object o) throws SemanticException {
        Type expressionType;
        Type rightOperandType;
        Type leftOperandType = (Type) e.getLeft().visit(this, null);

        if(e.getRight() != null) {
            rightOperandType = (Type) e.getRight().visit(this, null);

            if(!leftOperandType.equals(rightOperandType))
                throw new SemanticException("Type mismatch\n\t" +
                    "Found a comparative expression comparing subexpressions of different types."
                );

            expressionType = Type.Boolean;
        }
        else {
            expressionType = leftOperandType;
        }

        return expressionType;
    }

    @Override
    public Object visitArithmeticExpression(ArithmeticExpression ae, Object o) throws SemanticException {
        if(ae.getOperators().size() == 0) {
            return ae.getTerms().get(0).visit(this, null);
        }
        else {
            for(TermExpression te : ae.getTerms()) {
                Type t = (Type) te.visit(this, null);
                if(!t.equals(Type.Integer))
                    throw new SemanticException("Type mismatch\n\t" +
                        "Found a composite arithmetic expression with BOOLEAN operands."
                    );
            }
            return Type.Integer;
        }
    }

    @Override
    public Object visitTermExpression(TermExpression te, Object o) throws SemanticException {
        if(te.getOperators().size() == 0) {
            return te.getFactors().get(0).visit(this, null);
        }
        else {
            for(FactorExpression fe : te.getFactors()) {
                Type t = (Type) fe.visit(this, null);
                if(!t.equals(Type.Integer))
                    throw new SemanticException("Type mismatch\n\t" +
                            "Found a composite arithmetic expression with BOOLEAN operands."
                    );
            }
            return Type.Integer;
        }
    }

    @Override
    public Object visitNumberFactor(NumberFactor nf, Object o) {
        return Type.Integer;
    }

    @Override
    public Object visitBooleanFactor(BooleanFactor bf, Object o) {
        return Type.Boolean;
    }

    @Override
    public Object visitIdFactor(IdFactor idf, Object o) throws SemanticException {
        Token token = idf.getIdentifier().getToken();
        AST attribute = identificationTable.retrieve(token.getSpelling());

        if(attribute == null)
            throw new SemanticException("Identification error:\n\t" +
                "Identifier \"" + token.getSpelling() + "\" used at line " + token.getLine() +
                    " but this identifier could not be identified.\n\t" +
                    "Perhaps it wasn't declared."
            );
        if(!(attribute instanceof VariableDeclaration))
            throw new SemanticException("Identification error:\n\t" +
                    "Identifier \"" + token.getSpelling() + "\" used at line " + token.getLine() +
                    " was successfully identified but it points to a function declaration.\n\t" +
                    "Perhaps you might want to use VALOF keyword."
            );

        VariableDeclaration v = (VariableDeclaration) attribute;
        Token idType = v.getType().getToken();

        if(idType.getKind().equals(GrammarSymbols.INTEGER))
            return Type.Integer;

        return Type.Boolean;
    }

    @Override
    public Object visitCallFactor(CallFactor cf, Object o) throws SemanticException {
        FunctionListAttributes argumentsAttributes = null;
        FunctionListAttributes parametersAttributes = null;
        Token t = cf.getIdentifier().getToken();
        AST id = identificationTable.retrieve(t.getSpelling());
        boolean isCorrect = id != null && id instanceof FunctionDeclaration;

        if(!isCorrect)
            throw new SemanticException("Call error:\n\t" +
                    "Trying to call " + t .getSpelling() + " which is not a function or" +
                    " has never been declared." +
                    "Error at line " + t.getLine()
            );

        FunctionDeclaration fd = (FunctionDeclaration) id;
        if(cf.getArguments() != null)
            argumentsAttributes = (FunctionListAttributes) cf.getArguments().visit(this, null);

        if(fd.getParametersList() != null)
            parametersAttributes = (FunctionListAttributes) fd.getParametersList().visit(this, null);

        if(argumentsAttributes != null && parametersAttributes != null) {
            if(!argumentsAttributes.equals(parametersAttributes))
                throw new SemanticException("Call error:\n\t" +
                        "Parameters list and arguments list doesn't match.\n\t" +
                        "It is expected that they have the same size and types.\n\t" +
                        "Error at line " + t.getLine() + "."
                );
        }
        else if((argumentsAttributes == null && parametersAttributes != null) ||
                (parametersAttributes == null && argumentsAttributes != null)) {
            throw new SemanticException("Call error:\n\t" +
                    "Parameters list and arguments list doesn't match.\n\t" +
                    "It is expected that they have the same size and types.\n\t" +
                    "Error at line " + t.getLine() + "."
            );
        }


        if(fd.getReturnType() != null) {
            Token idType = fd.getReturnType().getToken();
            if(idType.getKind().equals(GrammarSymbols.INTEGER))
                return Type.Integer;
            else
                return Type.Boolean;
        }
        else {
            return Type.None;
        }
    }

    @Override
    public Object visitParametersList(ParametersList pl, Object o) {
        int size = pl.getIdentifiers().size();
        ArrayList<Type> typesList = new ArrayList<>();

        for(TType t : pl.getTypes()) {
            Token typeToken = t.getToken();

            if(typeToken.getKind().equals(GrammarSymbols.INTEGER))
                typesList.add(Type.Integer);
            else
                typesList.add(Type.Boolean);
        }

        return new FunctionListAttributes(size, typesList);
    }

    @Override
    public Object visitArgumentList(ArgumentList al, Object o) throws SemanticException {
        int size = al.getExpressions().size();
        ArrayList<Type> typesList = new ArrayList<>();

        for(Expression e : al.getExpressions()) {
            Type type = (Type) e.visit(this, null);
            typesList.add(type);
        }

        return new FunctionListAttributes(size, typesList);
    }

    @Override
    public Object visitFunctionDeclaration(FunctionDeclaration fd, Object o) throws SemanticException {
        Token t = fd.getIdentifier().getToken();
        Token returnToken;
        Type type = Type.None;
        boolean hasOwnReturnCommand = false;
        boolean commandsCoverReturnCommand = false;


        if (fd.getReturnType() != null) {
            returnToken = fd.getReturnType().getToken();
            if (returnToken.getKind().equals(GrammarSymbols.INTEGER))
                type = Type.Integer;
            else if (returnToken.getKind().equals(GrammarSymbols.BOOLEAN))
                type = Type.Boolean;
        }

        identificationTable.enter(t.getSpelling(), fd);
        identificationTable.openScope();

        if(fd.getParametersList() != null) {
            for (int i = 0; i < fd.getParametersList().getIdentifiers().size(); i++) {
                TType parameterType = fd.getParametersList().getTypes().get(i);
                TIdentifier parameterId = fd.getParametersList().getIdentifiers().get(i);
                Expression parameterExpression = null;

                List<TermExpression> terms = new ArrayList<>();
                List<TOperator> arithmeticOperators = new ArrayList<>();
                List<FactorExpression> factors = new ArrayList<>();
                List<TOperator> termOperators = new ArrayList<>();

                if (parameterType.getToken().getKind().equals(GrammarSymbols.BOOLEAN)) {
                    factors.add(new BooleanFactor(new TBoolean(
                            new Token(GrammarSymbols.BOOLEAN, "TRUE", 0, 0))));

                } else {
                    factors.add(new NumberFactor(new TInteger(
                            new Token(GrammarSymbols.INTEGER, "0", 0, 0))));
                }

                terms.add(new TermExpression(factors, termOperators));
                parameterExpression = new Expression(
                        new ArithmeticExpression(terms, arithmeticOperators), null, null);

                identificationTable.enter(parameterId.getToken().getSpelling(),
                        new VariableDeclaration(parameterType, parameterId, parameterExpression));
            }
        }

        for(Command c : fd.getCommands()) {
            if(c instanceof ReturnCommand) {
                hasOwnReturnCommand = true;
                if(type.equals(Type.None))
                    throw new SemanticException("Type mismatch:\n\t" +
                        "Trying to RETURN something on a BE function (procedure)\n\t" +
                            "Error at the declaration of " + t.getSpelling() +
                            "on line " + t.getLine() + "."
                    );

                Type returnType = (Type) c.visit(this, null);
                if(!returnType.equals(type)) {
                    throw new SemanticException("Type mismatch:\n\t" +
                            "RETURN expression has a different type of the function return type\n\t" +
                            "Error at the declaration of " + t.getSpelling() +
                            "on line " + t.getLine() + ".");
                }
            }
            else {
                Object ret = c.visit(this, null);

                if(ret != null && ret instanceof ReturnControl) {
                    commandsCoverReturnCommand = commandsCoverReturnCommand ||
                            ((ReturnControl) ret).isCoverabilityTest();

                    if(((ReturnControl) ret).isCoverabilityTest() &&
                            !type.equals(((ReturnControl) ret).getExpressionType())) {
                        throw new SemanticException("Type mismatch:\n\t" +
                                "Found different RETURN clauses returning expressions of different types.");
                    }
                }
            }
        }

        if(!type.equals(Type.None) && !(hasOwnReturnCommand || commandsCoverReturnCommand)) {
            throw new SemanticException("Type mismatch:\n\t" +
                    "Function " + t.getSpelling() +
                    " needs a RETURN clause but it's not fully covered by its commands.");
        }

        identificationTable.closeScope();
        return null;
    }

    @Override
    public Object visitGlobalDeclaration(GlobalDeclaration gd, Object o) throws SemanticException {

        for(VariableDeclaration vd : gd.getVariables()) {
            vd.visit(this, null);
        }

        return null;
    }

    @Override
    public Object visitVariableDeclaration(VariableDeclaration vd, Object o) throws SemanticException {
        Type type;
        Type expressionType = (Type) vd.getExpression().visit(this, null);
        Token typeToken = vd.getType().getToken();
        Token declarationToken = vd.getIdentifier().getToken();

        if(typeToken.getKind().equals(GrammarSymbols.BOOLEAN))
            type = Type.Boolean;
        else
            type = Type.Integer;

        if(!expressionType.equals(type))
            throw new SemanticException("Type mismatch:\n\t" +
                    "Declaring variable "+ declarationToken.getSpelling() +
                " at line " + declarationToken.getLine() + " and receiving a expression of different type."
            );

        vd.getIdentifier().visit(this, null);
        identificationTable.enter(declarationToken.getSpelling(), vd);

        return null;
    }

    @Override
    public Object visitTIdentifier(TIdentifier tid, Object o) {
        return null;
    }

    @Override
    public Object visitTInteger(TInteger tin, Object o) {
        return null;
    }

    @Override
    public Object visitTOperator(TOperator top, Object o) {
        return null;
    }

    @Override
    public Object visitTType(TType tt, Object o) {
        return null;
    }

    @Override
    public Object visitTBoolean(TBoolean tb, Object o) {
        return null;
    }
}
