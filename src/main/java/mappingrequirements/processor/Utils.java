package mappingrequirements.processor;

import mappingrequirements.model.CustomMethod;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
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

    static List<CustomMethod.Parameter> assembleParameters(ExecutableElement element) {
        List<CustomMethod.Parameter> parameters = new ArrayList<>();

        for(VariableElement variableElement : element.getParameters()) {
            parameters.add(new CustomMethod.Parameter(
                    variableElement.getSimpleName().toString(),
                    variableElement.asType().toString()
            ));
        }

        return parameters;
    }

}
