package checker;

import java.util.List;

/**
 * ${PACKAGE}
 * Created by gabriel on 08/06/17.
 */
public class FunctionListAttributes {
    private final int size;
    private final List<Type> typesList;

    public FunctionListAttributes(int size, List<Type> typesList) {
        this.size = size;
        this.typesList = typesList;
    }

    public int getSize() {
        return size;
    }

    public List<Type> getTypesList() {
        return typesList;
    }

    @Override
    public boolean equals(Object fla) {
        FunctionListAttributes f = (FunctionListAttributes) fla;
        List<Type> fList = f.getTypesList();

        if(this.getSize() != f.getSize())
            return false;

        for(int i = 0; i < this.getSize(); i++) {
            if(!typesList.get(i).equals(fList.get(i)))
                return false;
        }

        return true;
    }
}
