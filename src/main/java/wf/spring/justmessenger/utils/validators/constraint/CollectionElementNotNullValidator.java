package wf.spring.justmessenger.utils.validators.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import wf.spring.justmessenger.utils.validators.annotation.CollectionElementNotNull;

import java.util.Collection;

public class CollectionElementNotNullValidator implements ConstraintValidator<CollectionElementNotNull, Collection<?>> {



    @Override
    public boolean isValid(Collection<?> collection, ConstraintValidatorContext context) {
        if(collection == null) return true;

        for(Object o : collection)
            if(o == null) return false;

        return true;
    }

}