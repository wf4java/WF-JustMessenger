package wf.spring.justmessenger.utils.validators.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import wf.spring.justmessenger.utils.validators.constraint.UsernameValidator;


import java.lang.annotation.*;

@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Username {

    String message() default "Invalid username";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
