package de.hsba.bi.projectwork.user;

import de.hsba.bi.projectwork.web.exception.IncorrectPasswordException;
import de.hsba.bi.projectwork.web.exception.UserAlreadyExistException;
import de.hsba.bi.projectwork.web.user.ChangePasswordForm;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // CONSTRUCTOR
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // CREATE AND SAVE
    public User save(User user) {
        return userRepository.save(user);
    }

    public void createUser(RegisterUserForm userForm, Enum<User.Role> role) throws UserAlreadyExistException {
        if (usernameExists(userForm.getName())) {
            throw new UserAlreadyExistException("There is an account with the username '" + userForm.getName() + "'");
        }
        User user = new User();
        user.setName(userForm.getName());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setRole(role);
        this.save(user);
    }


    // FIND
    public static String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }

    public User findCurrentUser() {
        Optional<User> user = userRepository.findByName(getCurrentUsername());
        return user.orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByName(String name) {
        Optional<User> userOptional = userRepository.findByName(name);
        return userOptional.orElse(null);
    }

    public List<User> findUsers() {
        return userRepository.findByRole(User.DEVELOPER_ROLE);
    }


    // CHECK
    public boolean usernameExists(String name) {
        return userRepository.findByName(name).isPresent();
    }

    public boolean oldPasswordIsCorrect(String rawPassword, User user) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }


    // EDIT
    public ChangePasswordForm changePassword(ChangePasswordForm changePasswordForm) throws IncorrectPasswordException {
        // TODO Als Nutzer kann ich mein Passwort ändern

        // Load the authenticated user
        Optional<User> userOptional = userRepository.findByName(getCurrentUsername());

        if (userOptional.isPresent()) {
            // check if user was found
            User user = userOptional.get();

            // Check old password
            if (!oldPasswordIsCorrect(changePasswordForm.getOldPassword(), user)) {
                throw new IncorrectPasswordException("The old password you entered is incorrect!");
            }

            // actually change password
            if (changePasswordForm.getPassword().equals(changePasswordForm.getMatchingPassword())) {
                user.setPassword(passwordEncoder.encode(changePasswordForm.getPassword()));
                this.save(user);
            }
        }

        return changePasswordForm;
    }

    public void changeRole(Long id, String roleString) {
        // TODO Als Admin kann ich die Rollen anderer Nutzer ändern
        Enum<User.Role> roleEnum = User.Role.getEnumByDisplayValue(roleString);

        if(roleEnum != null) {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setRole(roleEnum);
                userRepository.save(user);
            }
        }
    }

}
