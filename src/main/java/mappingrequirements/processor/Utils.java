package mappingrequirements.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

final class Utils {

    private Utils() {  } // Cannot be instantiated!

    static List<String> getModifiers(Element element) {
        final List<String> modifiers = new ArrayList<>();

        for(Modifier modifier : element.getModifiers()) {
            modifiers.add(modifier.name().toLowerCase());
        }

        return modifiers;
    }

}
