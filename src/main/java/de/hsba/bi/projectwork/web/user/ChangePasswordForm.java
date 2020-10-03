package de.hsba.bi.projectwork.web.user;

import de.hsba.bi.projectwork.user.annotations.PasswordMatches;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@PasswordMatches
public class ChangePasswordForm {

    private String oldPassword;

    @NotEmpty(message = "Please enter a password.")
    @NotNull(message = "Please confirm your password.")
    @Size(min = 10, message = "Your password has to be at least 10 characters long.")
    private String password;

    @NotEmpty(message = "Please confirm your password.")
    @NotNull(message = "Please confirm your password.")
    @Size(min = 10, message = "Your password has to be at least 10 characters long.")
    private String matchingPassword;

}