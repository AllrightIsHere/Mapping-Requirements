package mappingrequirements.model;

import mappingrequirements.annotation.Priority;

import java.util.ArrayList;
import java.util.List;

public class CustomMethod {

    public final Priority priority;
    public final String createdBy;
    public final String lastModified;
    public final List<String> tags;
    public final String name;
    public final List<String> modifiers;
    public final String returnType;
    public final List<Parameter> parameters;

    public CustomMethod(
            Priority priority,
            String createdBy,
            String lastModified,
            List<String> tags,
            String name,
            List<String> modifiers,
            List<Parameter> parameters,
            String returnType
    ) {
        this.priority = priority;
        this.createdBy = createdBy;
        this.lastModified = lastModified;
        this.tags = tags;
        this.name = name;
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public static class Parameter {

        public final String name;
        public final String type;

        public Parameter(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

}
