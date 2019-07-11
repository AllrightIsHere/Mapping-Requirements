package mappingrequirements.processor;

import mappingrequirements.annotation.CustomMethodAnnotation;
import mappingrequirements.annotation.CustomTypeAnnotation;
import mappingrequirements.annotation.CustomVariableAnnotation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class MappingRequirementsProcessor extends AbstractProcessor {

    private Messager messager;

    @Override public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        this.messager = processingEnvironment.getMessager();
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        Set<Class<? extends Annotation>> annotations = supportedAnnotations();
        Set<String> names = new LinkedHashSet<>();

        for(Class<? extends Annotation> annotation : annotations) {
            names.add(annotation.getCanonicalName());
        }

        return names;
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // find elements annotatec with @CustomTypeAnnotation
        for(Element element : roundEnvironment.getElementsAnnotatedWith(CustomTypeAnnotation.class)) {
            messager.printMessage(
                    Kind.NOTE,
                    String.format(
                            "Type %s annotated with @%s",
                            element.getSimpleName(),
                            CustomTypeAnnotation.class.getSimpleName()
                    )
            );
        }

        // find elements annotatec with @CustomVariableAnnotation
        for(Element element : roundEnvironment.getElementsAnnotatedWith(CustomVariableAnnotation.class)) {
            messager.printMessage(
                    Kind.NOTE,
                    String.format(
                            "Variable %s annotated with @%s",
                            element.getSimpleName(),
                            CustomVariableAnnotation.class.getSimpleName()
                    )
            );
        }

        // find elements annotatec with @CustomMethodAnnotation
        for(Element element : roundEnvironment.getElementsAnnotatedWith(CustomMethodAnnotation.class)) {
            messager.printMessage(
                    Kind.NOTE,
                    String.format(
                            "Method %s annotated with @%s",
                            element.getSimpleName(),
                            CustomMethodAnnotation.class.getSimpleName()
                    )
            );
        }

        return false;
    }

    private Set<Class<? extends Annotation>> supportedAnnotations() {
        return new LinkedHashSet<>(
                Arrays.asList(
                        CustomTypeAnnotation.class,
                        CustomVariableAnnotation.class,
                        CustomMethodAnnotation.class
                )
        );
    }

}
