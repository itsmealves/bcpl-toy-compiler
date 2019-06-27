package parser;

import scanner.LexicalException;
import scanner.Scanner;
import scanner.Token;
import util.AST.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser class
 * @version 2010-august-29
 * @discipline Projeto de Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class Parser {
	// The current token
	private Token currentToken = null;
	// The scanner
	private Scanner scanner = null;
	
	/**
	 * Parser constructor
	 * @throws LexicalException 
	 */
	public Parser() throws LexicalException {
		// Initializes the scanner object
		this.scanner = new Scanner();
		this.currentToken = this.scanner.getNextToken();
	}
	
	/**
	 * Veririfes if the current token kind is the expected one
	 * @param kind
	 * @throws SyntacticException
	 * @throws LexicalException 
	 */
	private void accept(GrammarSymbols kind) throws SyntacticException, LexicalException {
		if(this.currentToken.getKind() == kind) {
			this.acceptIt();
		}
		else {
			throw new SyntacticException("Syntactic error: expecting " +
					kind + ", but found " + 
					this.currentToken.getKind(), 
					this.currentToken);
		}
	}
	
	/**
	 * Gets next token
	 * @throws LexicalException 
	 */
	private void acceptIt() throws LexicalException {
		this.currentToken = this.scanner.getNextToken();
	}

	/**
	 * Verifies if the source program is syntactically correct
	 * @throws SyntacticException
	 * @throws LexicalException 
	 */
	public AST parse() throws SyntacticException, LexicalException {
		Program ast = parseProgram();
		accept(GrammarSymbols.EOT);
		
		return ast;
	}
	
	private Program parseProgram() throws SyntacticException, LexicalException {
		GlobalDeclaration globalVars = null;
        List<FunctionDeclaration> functions = new ArrayList<>();

	    if(this.currentToken.getKind() == GrammarSymbols.GLOBAL) {
			globalVars = parseGlobalDeclaration();
		}

        while(this.currentToken.getKind() != GrammarSymbols.EOT) {
            functions.add(parseFunctionDeclaration());
        }

        return new Program(globalVars, functions);
	}
	
	private FunctionDeclaration parseFunctionDeclaration() throws SyntacticException, LexicalException{
        TType returnType = null;
        TIdentifier identifier = null;
        List<Command> commands = new ArrayList<>();
	 	ParametersList paramentersList = null;

	    accept(GrammarSymbols.LET);
	    if(this.currentToken.getKind() == GrammarSymbols.IDENTIFIER) {
	        identifier = new TIdentifier(this.currentToken);
            accept(GrammarSymbols.IDENTIFIER);
        }
	 	accept(GrammarSymbols.LP);

	 	if(this.currentToken.getKind() != GrammarSymbols.RP){
	 		paramentersList = parseParametersList();
	 	}
	 		
	 	accept(GrammarSymbols.RP);
	 		
	 	if(this.currentToken.getKind() == GrammarSymbols.BE) {
	 		acceptIt();
	 	}
	 	else {
	 		returnType = parseType();
	 	}
	 		
	 	accept(GrammarSymbols.LK);
	 		
	 	do {
	 		commands.add(parseCommand());
	 	} while(this.currentToken.getKind() != GrammarSymbols.RK);
	 		
	 	accept(GrammarSymbols.RK);

	 	return new FunctionDeclaration(identifier,
                paramentersList, returnType, commands);
	 }

	private GlobalDeclaration parseGlobalDeclaration() throws SyntacticException, LexicalException {
		List<VariableDeclaration> globalVars = new ArrayList<>();

	    accept(GrammarSymbols.GLOBAL);
		accept(GrammarSymbols.LK);
		
		do {
			globalVars.add(parseVariableDeclaration());
			accept(GrammarSymbols.SEMICOLON);
		} while(this.currentToken.getKind() != GrammarSymbols.RK);
		
		accept(GrammarSymbols.RK);

		return new GlobalDeclaration(globalVars);
	}

	private VariableDeclaration parseVariableDeclaration() throws SyntacticException, LexicalException {
		TType type = parseType();
		TIdentifier identifier = new TIdentifier(this.currentToken);

		accept(GrammarSymbols.IDENTIFIER);
		accept(GrammarSymbols.COLON);

		return new VariableDeclaration(type, identifier, parseExpression());
	}

	private ParametersList parseParametersList() throws SyntacticException, LexicalException {
        List<TType> types = new ArrayList<>();
        List<TIdentifier> identifiers = new ArrayList<>();

        types.add(parseType());
        identifiers.add(new TIdentifier(this.currentToken));
        accept(GrammarSymbols.IDENTIFIER);

        while(this.currentToken.getKind() == GrammarSymbols.COMMA){
            accept(GrammarSymbols.COMMA);
            types.add(parseType());
            identifiers.add(new TIdentifier(this.currentToken));
            accept(GrammarSymbols.IDENTIFIER);
        }

        return new ParametersList(types, identifiers);
	}
	
	
	private ArgumentList parseArgumentList() throws SyntacticException, LexicalException {
		List<Expression> expressions = new ArrayList<>();

	    expressions.add(parseExpression());
		 while(this.currentToken.getKind() == GrammarSymbols.COMMA){
		 	accept(GrammarSymbols.COMMA);
		 	expressions.add(parseExpression());
		 }

		 return new ArgumentList(expressions);
	}
	
	private Expression parseExpression() throws LexicalException, SyntacticException {
		ArithmeticExpression left = parseArithmeticExpression();
		ArithmeticExpression right = null;
		TOperator operator = null;
		
		if(this.currentToken.getKind() == GrammarSymbols.EQUALS ||
			this.currentToken.getKind() == GrammarSymbols.NEQUALS ||
			this.currentToken.getKind() == GrammarSymbols.GT ||
			this.currentToken.getKind() == GrammarSymbols.GE ||
			this.currentToken.getKind() == GrammarSymbols.LT ||
			this.currentToken.getKind() == GrammarSymbols.LE) {

		    operator = new TOperator(this.currentToken);
			acceptIt();
			right = parseArithmeticExpression();
		}

		return new Expression(left, right, operator);
	}
	
	private ArithmeticExpression parseArithmeticExpression() throws LexicalException, SyntacticException {
		List<TermExpression> terms = new ArrayList<>();
		List<TOperator> operators = new ArrayList<>();

	    terms.add(parseTermExpression());
		
		while(this.currentToken.getKind() == GrammarSymbols.ADD ||
			this.currentToken.getKind() == GrammarSymbols.SUBTRACT) {

		    operators.add(new TOperator(this.currentToken));
			acceptIt();
			terms.add(parseTermExpression());
		}

        return new ArithmeticExpression(terms, operators);
    }

	private TermExpression parseTermExpression() throws LexicalException, SyntacticException {
		List<FactorExpression> factors = new ArrayList<>();
		List<TOperator> operators = new ArrayList<>();
	    factors.add(parseFactorExpression());
		
		while(this.currentToken.getKind() == GrammarSymbols.MULTIPLY ||
				this.currentToken.getKind() == GrammarSymbols.DIVIDE) {

		    operators.add(new TOperator(this.currentToken));
			acceptIt();
			factors.add(parseFactorExpression());
		}
	    return new TermExpression(factors, operators);
	}

	private FactorExpression parseFactorExpression() throws LexicalException, SyntacticException {
	    FactorExpression factor;

	    if(this.currentToken.getKind() == GrammarSymbols.NUMBER) {
            factor = new NumberFactor(new TInteger(this.currentToken));
            acceptIt();
		}
		else if(this.currentToken.getKind() == GrammarSymbols.IDENTIFIER) {
			factor = new IdFactor(new TIdentifier(this.currentToken));
	        acceptIt();
		}
		else if(this.currentToken.getKind() == GrammarSymbols.LP) {
			acceptIt();
			factor = parseArithmeticExpression();
			accept(GrammarSymbols.RP);
		}
		else if(this.currentToken.getKind() == GrammarSymbols.TRUE) {
			factor = new BooleanFactor(new TBoolean(this.currentToken));
	        acceptIt();
		}
		else if(this.currentToken.getKind() == GrammarSymbols.FALSE) {
			factor = new BooleanFactor(new TBoolean(this.currentToken));
	        acceptIt();
		}
		else {
	        TIdentifier identifier = null;
	        ArgumentList arguments = null;

			accept(GrammarSymbols.VALOF);
			identifier = new TIdentifier(this.currentToken);
			accept(GrammarSymbols.IDENTIFIER);
			accept(GrammarSymbols.LP);
			
			if(this.currentToken.getKind() != GrammarSymbols.RP) {
				arguments = parseArgumentList();
			}
			
			accept(GrammarSymbols.RP);
			factor = new CallFactor(identifier, arguments);
		}

		return factor;
	}

	private TType parseType() throws SyntacticException, LexicalException {
	    TType type = new TType(this.currentToken);

		if(this.currentToken.getKind() == GrammarSymbols.INTEGER) {
			accept(GrammarSymbols.INTEGER);
		}
		else {
			accept(GrammarSymbols.BOOLEAN);
		}
		return type;
	}

	private Command parseCommand() throws SyntacticException, LexicalException {
		Command command;

	    if(this.currentToken.getKind() == GrammarSymbols.WHILE) {
	        Expression expression;
	        List<Command> body = new ArrayList<>();

			accept(GrammarSymbols.WHILE);
			expression = parseExpression();
			accept(GrammarSymbols.DO);
			accept(GrammarSymbols.LK);
			
			do {
				body.add(parseCommand());
			} while(this.currentToken.getKind() != GrammarSymbols.RK);
			
			accept(GrammarSymbols.RK);
			command = new WhileCommand(expression, body);
		}
		else if(this.currentToken.getKind() == GrammarSymbols.IF) {
            Expression expression;
            List<Command> body = new ArrayList<>();

			accept(GrammarSymbols.IF);
			expression = parseExpression();
			accept(GrammarSymbols.THEN);
			accept(GrammarSymbols.LK);
			
			do {
				body.add(parseCommand());
			} while(this.currentToken.getKind() != GrammarSymbols.RK);
			
			accept(GrammarSymbols.RK);
			command = new IfCommand(expression, body);
		}
		else if(this.currentToken.getKind() == GrammarSymbols.TEST) {
            Expression expression;
            List<Command> body = new ArrayList<>();
            List<Command> elseBody = new ArrayList<>();

			accept(GrammarSymbols.TEST);
			expression = parseExpression();
			accept(GrammarSymbols.THEN);
			accept(GrammarSymbols.LK);
			
			do {
				body.add(parseCommand());
			} while(this.currentToken.getKind() != GrammarSymbols.RK);
			
			accept(GrammarSymbols.RK);
			
			accept(GrammarSymbols.ELSE);
			accept(GrammarSymbols.LK);
			
			do {
				elseBody.add(parseCommand());
			} while(this.currentToken.getKind() != GrammarSymbols.RK);
			
			accept(GrammarSymbols.RK);
			command = new TestCommand(expression, body, elseBody);
		}
		else if(this.currentToken.getKind() == GrammarSymbols.WRITE) {
	        accept(GrammarSymbols.WRITE);
			accept(GrammarSymbols.LP);
			
			command = new WriteCommand(parseExpression());
			
			accept(GrammarSymbols.RP);
			accept(GrammarSymbols.SEMICOLON);
	    }
		else if(this.currentToken.getKind() == GrammarSymbols.BREAK) {
			accept(GrammarSymbols.BREAK);
			accept(GrammarSymbols.SEMICOLON);

			command = new BreakCommand();
		}
		else if(this.currentToken.getKind() == GrammarSymbols.CONTINUE) {
			accept(GrammarSymbols.CONTINUE);
			accept(GrammarSymbols.SEMICOLON);

			command = new ContinueCommand();
		}
		else if(this.currentToken.getKind() == GrammarSymbols.RETURN) {
			accept(GrammarSymbols.RETURN);
			command = new ReturnCommand(parseExpression());
			accept(GrammarSymbols.SEMICOLON);
		}
		else if(this.currentToken.getKind() == GrammarSymbols.IDENTIFIER){
		 	TIdentifier identifier = new TIdentifier(this.currentToken);
	        accept(GrammarSymbols.IDENTIFIER);
		 	
		 	if(this.currentToken.getKind() == GrammarSymbols.LP) {
		 		ArgumentList arguments = null;
		 	    accept(GrammarSymbols.LP);
		 		
		 		if(this.currentToken.getKind() != GrammarSymbols.RP) {
		 			arguments = parseArgumentList();
		 		}
			 
			 	accept(GrammarSymbols.RP);
		 		command = new CallCommand(identifier, arguments);
		 	}
		 	else {
		 		accept(GrammarSymbols.ASSIGNMENT);
		 		Expression expression = parseExpression();
		 		command = new AssignmentCommand(identifier, expression);
		 	}
		 	accept(GrammarSymbols.SEMICOLON);
		}
		else {
		    command = new VariableDeclarationCommand(parseVariableDeclaration());
		    accept(GrammarSymbols.SEMICOLON);
		}
	    return command;
	}
}
