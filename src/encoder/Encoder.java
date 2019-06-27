package encoder;

import checker.SemanticException;
import compiler.Properties;
import util.AST.AST;

import java.io.*;
import java.util.logging.StreamHandler;

/**
 * ${PACKAGE}
 * Created by gabriel on 10/06/17.
 */
public class Encoder {
    private String fileName;
    private String className;
    private String targetCode;
    private FileWriter fileWriter;
    private boolean didEncode = false;

    public void encode(AST ast) throws IOException, EncodingException {
        Process compilationProcess;
        int lastIndex = Properties.sourceCodeLocation.lastIndexOf(".");

        className = Properties.sourceCodeLocation.substring(0, lastIndex).toUpperCase();
        fileName = className.toUpperCase() + ".java";
        fileWriter = new FileWriter(fileName);

        targetCode = ast.toCode();
        fileWriter.write(targetCode);
        fileWriter.close();

        compilationProcess = Runtime.getRuntime().exec("javac " + fileName);
        while(compilationProcess.isAlive());

        if(compilationProcess.exitValue() != 0)
            throw new EncodingException("Encoding error:\n\t" +
                    "Found an error on your source code while encoding."
            );

        if(!Properties.shouldKeedObjectFile) {
            new File(fileName).delete();
        }

        didEncode = true;
    }

    public void execute() throws IOException {
        if(didEncode) {
            System.out.println("\n*** EXECUTING " + className + " ***\n");

            String s = null;
            Process executionProcess = Runtime.getRuntime().exec("java " + className);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(executionProcess.getInputStream()));

            while ((s = br.readLine()) != null) {
                System.out.println(s);
            }

            System.out.println("\n*** DONE ***\n");
        }
    }
}
