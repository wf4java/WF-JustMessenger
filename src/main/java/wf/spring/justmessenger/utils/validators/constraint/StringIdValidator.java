package wf.spring.justmessenger.utils.validators.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.bson.types.ObjectId;
import wf.spring.justmessenger.utils.validators.annotation.StringId;

import java.security.SecureRandom;


public class StringIdValidator implements ConstraintValidator<StringId, String> {

    @Override
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
        if(id == null) return true;
        if(id.isBlank()) return false;

        return ObjectId.isValid(id);
    }

}
