package mappingrequirements.model;

import java.util.ArrayList;
import java.util.List;

public class CustomMethod {

    public final String name;
    public final String description;
    public final List<String> modifiers;
    public final String returnType;
    public final List<Parameter> parameters;

    public CustomMethod(String name, String description, String returnType) {
        this.name = name;
        this.description = description;
        this.modifiers = new ArrayList<>();
        this.returnType = returnType;
        this.parameters = new ArrayList<>();
    }

    static class Parameter {

        public final String name;
        public final String type;

        public Parameter(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

}
