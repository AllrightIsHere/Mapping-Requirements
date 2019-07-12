package mappingrequirements.model;

import java.util.ArrayList;
import java.util.List;

public class CustomVariable {

    public final String name;
    public final String type;
    public final String description;
    public final List<String> modifiers;

    public CustomVariable(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.modifiers = new ArrayList<>();
    }
}
