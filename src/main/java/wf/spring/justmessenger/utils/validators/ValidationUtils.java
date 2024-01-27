package wf.spring.justmessenger.utils.validators;


import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;

@Component
public class ValidationUtils {

    private static Validator validator;

    public ValidationUtils(Validator validator) {
        ValidationUtils.validator = validator;
    }


    public void validateAndThrow(Object target) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, target.getClass().getSimpleName());

        if (validator instanceof SmartValidator sv) {sv.validate(target, bindingResult);}
        else {validator.validate(target, bindingResult);}

        if (bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);
    }

}
