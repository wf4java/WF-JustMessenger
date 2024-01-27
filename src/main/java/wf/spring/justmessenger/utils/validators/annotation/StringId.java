package wf.spring.justmessenger.utils.validators.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import wf.spring.justmessenger.utils.validators.constraint.StringIdValidator;

import java.lang.annotation.*;


@Constraint(validatedBy = StringIdValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StringId {

    String message() default "Invalid id pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
