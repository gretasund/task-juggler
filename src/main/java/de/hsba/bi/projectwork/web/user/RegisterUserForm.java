package de.hsba.bi.projectwork.web.user;

import de.hsba.bi.projectwork.user.annotations.PasswordMatches;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@RequiredArgsConstructor
@PasswordMatches
public class RegisterUserForm {

    @NotEmpty(message = "Please enter a username.")
    @NotNull(message = "Please enter a username.")
    @Size(min = 3, message = "Your username has to be at least 3 characters long.")
    private String name;

    @NotEmpty(message = "Please enter a password.")
    @NotNull(message = "Please confirm your password.")
    @Size(min = 10, message = "Your password has to be at least 10 characters long.")
    private String password;

    @NotEmpty(message = "Please confirm your password.")
    @NotNull(message = "Please confirm your password.")
    @Size(min = 10, message = "Your password has to be at least 10 characters long.")
    private String matchingPassword;


    public RegisterUserForm(String name, String password, String matchingPassword) {
        this.name = name;
        this.password = password;
        this.matchingPassword = matchingPassword;
    }

}
