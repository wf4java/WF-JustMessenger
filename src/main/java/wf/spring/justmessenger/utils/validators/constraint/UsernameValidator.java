package wf.spring.justmessenger.utils.validators.constraint;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import wf.spring.justmessenger.utils.validators.annotation.Username;


@RequiredArgsConstructor
public class UsernameValidator implements ConstraintValidator<Username, String> {

    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{3,25}$";


    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if(username == null) return true;
        if(username.isBlank()) return false;

        return username.matches(USERNAME_PATTERN);
    }

}