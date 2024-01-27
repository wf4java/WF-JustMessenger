package wf.spring.justmessenger.utils.validators.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import wf.spring.justmessenger.utils.validators.constraint.CollectionElementNotNullValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = CollectionElementNotNullValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CollectionElementNotNull {

    String message() default "Null element in list";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}