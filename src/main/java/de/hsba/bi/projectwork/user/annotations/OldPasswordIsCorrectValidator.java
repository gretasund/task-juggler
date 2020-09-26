package de.hsba.bi.projectwork.user.annotations;

import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.user.ChangePasswordForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class OldPasswordIsCorrectValidator implements ConstraintValidator<OldPasswordIsCorrect, Object> {

    @Autowired
    UserService userService;

    @Override
    public void initialize(OldPasswordIsCorrect constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        ChangePasswordForm changePasswordForm = (ChangePasswordForm) obj;
        User user = userService.findCurrentUser();
        return userService.checkOldPassword(changePasswordForm.getOldPassword(), user);
    }

}