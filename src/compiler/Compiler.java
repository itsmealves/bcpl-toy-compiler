package compiler;

import checker.Checker;
import checker.SemanticException;
import encoder.Encoder;
import encoder.EncodingException;
import parser.Parser;
import parser.SyntacticException;
import scanner.LexicalException;
//import util.AST.AST;
import util.AST.AST;
import util.symbolsTable.IdentificationTable;

import java.io.IOException;

/**
 * Compiler driver
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class Compiler {

    /*
	 * Compiler start point
	 * @param args - none
	 */
	public static void main(String[] args) {
		// Initializes the identification table with the reserved words 
        AST ast;
        Parser p;
        Checker c;
        Encoder e;

        System.out.println("\n** Mini-BCPL Compiler **");
        System.out.println("Compiling file " + Properties.sourceCodeLocation + ".");

		try {
			// Parses the source code
			p = new Parser();
			c = new Checker();
			e = new Encoder();

            System.out.println("\t* Parsing syntactic tree");
            ast = p.parse();
            System.out.println("\t\tDONE!");

            System.out.println("\t* Running contextual analysis");
            c.check(ast);
            System.out.println("\t\tDONE!");

            System.out.println("\t* Encoding target bytecode");
            e.encode(ast);
            System.out.println("\t\tDONE!");

            System.out.println("\n" + ast.toString(1));
            System.out.println("No errors found. AST structure printed above for further analysis.");

            if(Properties.runTargetBytecode)
                e.execute();

            System.out.println("** Finished compilation process **");

        } catch(SyntacticException e1) {
            System.err.println("\n" + e1.toString());
            System.err.println("Compilation halted because of a syntactic error found.\n" +
                    "Check message above for error analysis.");
        } catch (LexicalException e2) {
            System.err.println("\n" + e2.toString());
            System.err.println("Compilation halted because of a lexical error found.\n" +
                    "Check message above for error analysis.");
		} catch (SemanticException e3) {
            System.err.println("\n" + e3.toString());
            System.err.println("Compilation halted because of a semantic error found.\n" +
                    "Check message above for error analysis.");
        } catch (IOException e4) {
            System.err.println("\n" + e4.toString());
            System.err.println("Compilation halted because of an IO error found.\n" +
                    "Check message above for error analysis.");
        } catch (EncodingException e5) {
            System.err.println("\n" + e5.toString());
            System.err.println("Compilation halted because of an encoding error found.\n" +
                    "Check message above for error analysis.");
        }
    }
}
