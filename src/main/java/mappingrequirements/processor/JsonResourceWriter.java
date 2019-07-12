package mappingrequirements.processor;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public final class JsonResourceWriter {

    private final Filer filer;
    private final Messager messager;
    private final Gson gson;

    public JsonResourceWriter(ProcessingEnvironment environment) {
        this.filer = environment.getFiler();
        this.messager = environment.getMessager();
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()
                .create();
    }

    public void write(StandardLocation location, String resourceName, Object resource) {
        try {
            FileObject fileObject = filer.createResource(location, "", resourceName);

            OutputStream outputStream = fileObject.openOutputStream();

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

            String json = gson.toJson(resource);

            writer.write(json);

            writer.flush();

            writer.close();
        } catch (IOException ex) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Failed to write to output.json: " + ex.toString());
        }
    }

}
