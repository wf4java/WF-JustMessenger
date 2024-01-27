package wf.spring.justmessenger.utils.validators.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import wf.spring.justmessenger.utils.validators.constraint.PasswordValidator;


import java.lang.annotation.*;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Password {

    String message() default "Invalid password pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
