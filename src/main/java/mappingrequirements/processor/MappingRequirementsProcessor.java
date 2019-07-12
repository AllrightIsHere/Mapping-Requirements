package mappingrequirements.processor;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mappingrequirements.annotation.CustomMethodAnnotation;
import mappingrequirements.annotation.CustomTypeAnnotation;
import mappingrequirements.annotation.CustomVariableAnnotation;
import mappingrequirements.model.CustomType;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MappingRequirementsProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elements;
    private Gson gson;
    private List<CustomType> customTypes;

    @Override public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        this.customTypes = new LinkedList<>();

        this.filer = processingEnvironment.getFiler();

        this.messager = processingEnvironment.getMessager();

        this.elements = processingEnvironment.getElementUtils();

        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()
                .create();
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

        findAndParseElementsAnnotatedWithCustomTypeAnnotation(roundEnvironment, customTypes);

        messager.printMessage(Diagnostic.Kind.NOTE, "running round...");

        messager.printMessage(Diagnostic.Kind.NOTE, "customTypes.isEmpty() = " + customTypes.isEmpty());

        if(!customTypes.isEmpty() && !roundEnvironment.errorRaised() && !roundEnvironment.processingOver()) {
            writeElements();
        }

        cleanup();

        return false;

    }

    private void findAndParseElementsAnnotatedWithCustomTypeAnnotation(RoundEnvironment roundEnvironment, List<CustomType> customTypes) {
        for(Element element : roundEnvironment.getElementsAnnotatedWith(CustomTypeAnnotation.class)) {
            String name = element.getSimpleName().toString();
            PackageElement packageElement = elements.getPackageOf(element);
            String packageName = packageElement.isUnnamed() ? "" : packageElement.toString();
            CustomTypeAnnotation annotation = element.getAnnotation(CustomTypeAnnotation.class);

            CustomType customType = new CustomType(
                    name,
                    packageName,
                    annotation.priority(),
                    annotation.createdBy(),
                    annotation.lastModified(),
                    Arrays.asList(annotation.tags())
            );

            customTypes.add(customType);
        }
    }

    private void writeElements() {
        try {
            FileObject fileObject = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", "output.json");

            try (OutputStream outputStream = fileObject.openOutputStream()) {
                String json = gson.toJson(customTypes);

                messager.printMessage(Diagnostic.Kind.NOTE, json);

                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                writer.write(json);

                writer.flush();

                writer.close();
            }
        } catch (IOException ex) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Failed to write to output.json: " + ex.toString());
        }
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
