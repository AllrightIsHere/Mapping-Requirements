package mappingrequirements.model;

import mappingrequirements.annotation.Priority;

import java.util.ArrayList;
import java.util.List;

public class CustomType {

    public final String name;
    public final String packageName;
    public final List<String> modifiers;
    public final Priority priority;
    public final String createdBy;
    public final String lastModified;
    public final List<String> tags;
    public final List<CustomVariable> variables;
    public final List<CustomMethod> methods;

    public CustomType(
            String name,
            String packageName,
            List<String> modifiers,
            Priority priority,
            String createdBy,
            String lastModified,
            List<String> tags
    ) {
        this.name = name;
        this.packageName = packageName;
        this.modifiers = modifiers;
        this.priority = priority;
        this.createdBy = createdBy;
        this.lastModified = lastModified;
        this.tags = tags;
        this.variables = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

}
