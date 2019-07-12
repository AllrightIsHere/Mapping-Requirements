package mappingrequirements.processor;

import mappingrequirements.annotation.CustomMethodAnnotation;
import mappingrequirements.annotation.CustomTypeAnnotation;
import mappingrequirements.annotation.CustomVariableAnnotation;
import mappingrequirements.model.CustomType;
import mappingrequirements.model.CustomVariable;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.StandardLocation;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static mappingrequirements.processor.Utils.getModifiers;

public class MappingRequirementsProcessor extends AbstractProcessor {

    private Elements elements;
    private List<Element> customTypeElements;
    private List<CustomType> customTypes;
    private JsonResourceWriter writer;

    @Override public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        this.customTypeElements = new ArrayList<>();

        this.customTypes = new ArrayList<>();

        this.elements = processingEnvironment.getElementUtils();

        writer = new JsonResourceWriter(processingEnvironment);
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        final Set<Class<? extends Annotation>> annotations = supportedAnnotations();
        final Set<String> names = new LinkedHashSet<>();

        for(Class<? extends Annotation> annotation : annotations) {
            names.add(annotation.getCanonicalName());
        }

        return names;
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        findAndParseElementsAnnotatedWithCustomTypeAnnotation(roundEnvironment);

        findAndParseElementsAnnotatedWithCustomVariableAnnotation(roundEnvironment);

        if(!customTypes.isEmpty() && !roundEnvironment.errorRaised() && !roundEnvironment.processingOver()) {
            writer.write(StandardLocation.SOURCE_OUTPUT, "output.json", customTypes);
        }

        cleanup();

        return false;

    }

    private void findAndParseElementsAnnotatedWithCustomTypeAnnotation(RoundEnvironment roundEnvironment) {
        for(Element element : roundEnvironment.getElementsAnnotatedWith(CustomTypeAnnotation.class)) {
            customTypeElements.add(element);
            final String name = element.getSimpleName().toString();
            final PackageElement packageElement = elements.getPackageOf(element);
            final String packageName = packageElement.isUnnamed() ? "" : packageElement.toString();
            CustomTypeAnnotation annotation = element.getAnnotation(CustomTypeAnnotation.class);

            customTypes.add(new CustomType(
                    name,
                    packageName,
                    getModifiers(element),
                    annotation.priority(),
                    annotation.createdBy(),
                    annotation.lastModified(),
                    Arrays.asList(annotation.tags())
            ));
        }
    }

    private void findAndParseElementsAnnotatedWithCustomVariableAnnotation(RoundEnvironment roundEnvironment) {
        for(Element element : roundEnvironment.getElementsAnnotatedWith(CustomVariableAnnotation.class)) {
            Element rootElement = element.getEnclosingElement();

            int rootElementIndex = customTypeElements.indexOf(rootElement);

            if(rootElementIndex != -1) {
                customTypes.get(rootElementIndex).variables.add(
                        parseElementsAnnotatedWithCustomVariableAnnotation(element)
                );
            }
        }
    }

    private static CustomVariable parseElementsAnnotatedWithCustomVariableAnnotation(Element element) {
        return new CustomVariable(
                element.getSimpleName().toString(),
                element.asType().toString(),
                element.getAnnotation(CustomVariableAnnotation.class).description(),
                getModifiers(element)
        );
    }

    private void cleanup() {
        customTypes.clear();
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
