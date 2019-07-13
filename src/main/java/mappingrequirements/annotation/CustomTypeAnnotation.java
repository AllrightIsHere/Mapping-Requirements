package mappingrequirements.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CustomTypeAnnotation {

    Priority priority() default Priority.MEDIUM;

    String[] tags() default "";

    String createdBy() default "Gabriel";

    String lastModified() default "02/05/2019";

}
