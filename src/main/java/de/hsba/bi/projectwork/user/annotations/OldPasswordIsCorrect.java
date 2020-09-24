package de.hsba.bi.projectwork.user.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = OldPasswordIsCorrectValidator.class)
@Documented
public @interface OldPasswordIsCorrect {
    String message() default "The old password you entered wasn´t correct!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
