package scanner;

import compiler.Properties;
import java.util.HashMap;
import java.util.Map;
import parser.GrammarSymbols;
import util.Arquivo;

/**
 * Scanner class
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class Scanner {

	// The file object that will be used to read the source code
	private Arquivo file;
	// The last char read from the source code
	private char currentChar;
	// The kind of the current token
	private GrammarSymbols currentKind;
	// Buffer to append characters read from file
	private StringBuffer currentSpelling;
	// Current line and column in the source file
	private int line, column;
	// Keywords
	private Map<String,GrammarSymbols> keywords;
	
	/**
	 * Default constructor
	 */
	public Scanner() {
		this.file = new Arquivo(Properties.sourceCodeLocation);		
		this.line = 0;
		this.column = 0;
		this.currentChar = this.file.readChar();
		this.currentSpelling = new StringBuffer();
		this.keywords = new HashMap<String, GrammarSymbols>();
		
		keywords.put("INT", GrammarSymbols.INTEGER);
		keywords.put("BOOLEAN", GrammarSymbols.BOOLEAN);
		keywords.put("GLOBAL", GrammarSymbols.GLOBAL);
		keywords.put("LET", GrammarSymbols.LET);
		keywords.put("BE", GrammarSymbols.BE);
		keywords.put("WHILE", GrammarSymbols.WHILE);
		keywords.put("DO", GrammarSymbols.DO);
		keywords.put("IF", GrammarSymbols.IF);
		keywords.put("THEN", GrammarSymbols.THEN);
		keywords.put("TEST", GrammarSymbols.TEST);
		keywords.put("ELSE", GrammarSymbols.ELSE);
		keywords.put("TRUE", GrammarSymbols.TRUE);
		keywords.put("FALSE", GrammarSymbols.FALSE);
		keywords.put("BREAK", GrammarSymbols.BREAK);
		keywords.put("CONTINUE", GrammarSymbols.CONTINUE);
		keywords.put("WRITE", GrammarSymbols.WRITE);
		keywords.put("RETURN", GrammarSymbols.RETURN);
		keywords.put("VALOF", GrammarSymbols.VALOF);
	}
	
	/**
	 * Returns the next token
	 * @return
	 * @throws LexicalException 
	 */
	public Token getNextToken() throws LexicalException {
		while(this.isSeparator(this.currentChar)) {
			this.scanSeparator();
		}
		
		currentSpelling.delete(0, currentSpelling.length());
		this.currentKind = this.scanToken();
		
		return new Token(this.currentKind,
				this.currentSpelling.toString(),
				this.line,
				this.column);
	}
	
	/**
	 * Returns if a character is a separator
	 * @param c
	 * @return
	 */
	private boolean isSeparator(char c) {
		if ( c == '#' || c == ' ' || c == '\n' || c == '\t' ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Reads (and ignores) a separator
	 * @throws LexicalException
	 */
	private void scanSeparator() throws LexicalException {
		if(this.currentChar == '#') {
			while(this.currentChar != '\n') {
				this.getNextChar();
			}
		}
		else {
			this.getNextChar();
		}
	}
	
	/**
	 * Gets the next char
	 */
	private void getNextChar() {
		// Appends the current char to the string buffer
		this.currentSpelling.append(this.currentChar);
		// Reads the next one
		this.currentChar = this.file.readChar();
		// Increments the line and column
		this.incrementLineColumn();
	}
	
	/**
	 * Increments line and column
	 */
	private void incrementLineColumn() {
		// If the char read is a '\n', increments the line variable and assigns 0 to the column
		if ( this.currentChar == '\n' ) {
			this.line++;
			this.column = 0;
		// If the char read is not a '\n' 
		} else {
			// If it is a '\t', increments the column by 4
			if ( this.currentChar == '\t' ) {
				this.column = this.column + 4;
			// If it is not a '\t', increments the column by 1
			} else {
				this.column++;
			}
		}
	}
	
	/**
	 * Returns if a char is a digit (between 0 and 9)
	 * @param c
	 * @return
	 */
	private boolean isDigit(char c) {
		if ( c >= '0' && c <= '9' ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns if a char is a letter (between a and z or between A and Z)
	 * @param c
	 * @return
	 */
	private boolean isLetter(char c) {
		if ( (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Scans the next token
	 * Simulates the DFA that recognizes the language described by the lexical grammar
	 * @return
	 * @throws LexicalException
	 */
	private GrammarSymbols scanToken() throws LexicalException {
		int state = 0;
		
		while(true) {
			switch(state) {
			case 0:
				if(this.currentChar == '\000') {
					state = 1;
				}
				else if(this.isLetter(this.currentChar)) {
					state = 2;
				}
				else if(this.currentChar == '$') {
					state = 3;
				}
				else if(this.currentChar == ';') {
					state = 6;
				}
				else if(this.currentChar == ',') {
					state = 7;
				}
				else if(this.currentChar == '!') {
					state = 8;
				}
				else if(this.currentChar == '>') {
					state = 10;
				}
				else if(this.currentChar == '<') {
					state = 12;
				}
				else if(this.currentChar == ':') {
					state = 14;
				}
				else if(this.currentChar == '=') {
					state = 16;
				}
				else if(this.currentChar == '+') {
					state = 17;
				}
				else if(this.currentChar == '-') {
					state = 18;
				}
				else if(this.currentChar == '*') {
					state = 19;
				}
				else if(this.currentChar == '/') {
					state = 20;
				}
				else if(this.currentChar == '(') {
					state = 21;
				}
				else if(this.currentChar == ')') {
					state = 22;
				}
				else if(this.isDigit(this.currentChar)) {
					state = 23;
				}
				else {
					state = 24;
				}
				
				this.getNextChar();
				break;
			case 1:
				return GrammarSymbols.EOT;
			case 2:
				while(this.isLetter(this.currentChar) ||
					  this.isDigit(this.currentChar) ||
					  this.currentChar == '.') {
					this.getNextChar();
				}
				
				if(this.keywords.containsKey(this.currentSpelling.toString())) {
					return this.keywords.get(this.currentSpelling.toString());
				}
				else {
					return GrammarSymbols.IDENTIFIER;
				}
			case 3:
				if(this.currentChar == '(') {
					state = 4;
					this.getNextChar();
				}
				else if(this.currentChar == ')') {
					state = 5;
					this.getNextChar();
				}
				else {
					state = 24;
				}
				break;
			case 4:
				return GrammarSymbols.LK;
			case 5:
				return GrammarSymbols.RK;
			case 6:
				return GrammarSymbols.SEMICOLON;
			case 7:
				return GrammarSymbols.COMMA;
			case 8:
				if(this.currentChar == '=') {
					state = 9;
					this.getNextChar();
				}
				else {
					state = 24;
				}
				break;
			case 9:
				return GrammarSymbols.NEQUALS;
			case 10:
				if(this.currentChar == '=') {
					state = 11;
					this.getNextChar();
				}
				else {
					return GrammarSymbols.GT;
				}
				break;
			case 11:
				return GrammarSymbols.GE;
			case 12:
				if(this.currentChar == '=') {
					state = 13;
					this.getNextChar();
				}
				else {
					return GrammarSymbols.LT;
				}
				break;
			case 13:
				return GrammarSymbols.LE;
			case 14:
				if(this.currentChar == '=') {
					state = 15;
					this.getNextChar();
				}
				else {
					return GrammarSymbols.COLON;
				}
				break;
			case 15:
				return GrammarSymbols.ASSIGNMENT;
			case 16:
				return GrammarSymbols.EQUALS;
			case 17:
				return GrammarSymbols.ADD;
			case 18:
				return GrammarSymbols.SUBTRACT;
			case 19:
				return GrammarSymbols.MULTIPLY;
			case 20:
				return GrammarSymbols.DIVIDE;
			case 21:
				return GrammarSymbols.LP;
			case 22:
				return GrammarSymbols.RP;
			case 23:
				while(this.isDigit(this.currentChar)) {
					this.getNextChar();
				}
				
				return GrammarSymbols.NUMBER;
			case 24:
				throw new LexicalException(
						"I found a lexical error!",
						this.currentChar, this.line, this.column);
			}
		}
	}
}
