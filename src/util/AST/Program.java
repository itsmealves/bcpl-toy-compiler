package util.AST;

import checker.SemanticException;
import checker.Visitor;

import java.util.List;
import java.util.Properties;

/**
 * Created by Gabriel Alves on 13/05/2017.
 */
public class Program extends AST  {
    private GlobalDeclaration globalVars;
    private List<FunctionDeclaration> functions;

    public Program(GlobalDeclaration globalVars, List<FunctionDeclaration> functions) {
        this.globalVars = globalVars;
        this.functions = functions;
    }

    public GlobalDeclaration getGlobalVars() {
        return globalVars;
    }

    public List<FunctionDeclaration> getFunctions() {
        return functions;
    }

    @Override
    public String toString(int level) {
        StringBuilder s = new StringBuilder();
        s.append(this.getSpaces(level));
        s.append("Program Start\n");

        if(globalVars != null)
            s.append(globalVars.toString(level + 1));

        for(FunctionDeclaration f : functions)
            s.append(f.toString(level + 1));

        s.append(this.getSpaces(level));
        s.append("Program End\n");
        return s.toString();
    }

    @Override
    public Object visit(Visitor v, Object arg) throws SemanticException {
        return v.visitProgram(this, arg);
    }

    @Override
    public String toCode() {
        StringBuilder s = new StringBuilder();
        int lastIndex = compiler.Properties.sourceCodeLocation.lastIndexOf(".");
        String programName = compiler.Properties.sourceCodeLocation.substring(0, lastIndex);

        s.append("public class ");
        s.append(programName.toUpperCase());
        s.append(" {\n");

        if(globalVars != null) {
            s.append(globalVars.toCode());
            s.append("\n");
        }

        for(FunctionDeclaration f : functions) {
            s.append(f.toCode());
            s.append("\n");
        }

        s.append("public static void main(String... args) { start(); }\n}");

        return s.toString();
    }
}
