package checker;

/**
 * ${PACKAGE}
 * Created by gabriel on 08/06/17.
 */
public class ReturnControl {
    private final boolean coverabilityTest;
    private final Type expressionType;

    public ReturnControl(boolean coverabilityTest, Type expressionType) {

        this.coverabilityTest = coverabilityTest;
        this.expressionType = expressionType;
    }

    public boolean isCoverabilityTest() {
        return coverabilityTest;
    }

    public Type getExpressionType() {
        return expressionType;
    }
}
