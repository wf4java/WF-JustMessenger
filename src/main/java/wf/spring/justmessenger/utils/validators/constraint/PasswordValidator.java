package wf.spring.justmessenger.utils.validators.constraint;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import wf.spring.justmessenger.utils.validators.annotation.Password;


@RequiredArgsConstructor
public class PasswordValidator implements ConstraintValidator<Password, String> {


    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9_#&^%@!?()*=+$;:'№\\\\\"]{8,64}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if(password == null) return true;
        if(password.isBlank()) return false;

        return password.matches(PASSWORD_PATTERN);
    }

}