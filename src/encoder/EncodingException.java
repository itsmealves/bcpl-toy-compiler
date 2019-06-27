package encoder;

/**
 * ${PACKAGE}
 * Created by gabriel on 10/06/17.
 */
public class EncodingException extends Exception {

    /**
     * Default constructor
     * @param message
     */
    public EncodingException(String message) {
        super(message);
    }

    /**
     * Creates the error report
     */
    public String toString() {
        String errorMessage =
                "----------------------------- ENCODING ERROR REPORT - BEGIN -----------------------------\n" +
                        ">> Message: " + super.getMessage() + "\n" +
                        "------------------------------ ENCODING ERROR REPORT - END ------------------------------\n";

        return errorMessage;
    }
}
