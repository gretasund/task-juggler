package de.hsba.bi.projectwork.user;

import de.hsba.bi.projectwork.web.exception.IncorrectPasswordException;
import de.hsba.bi.projectwork.web.exception.UserAlreadyExistException;
import de.hsba.bi.projectwork.web.user.ChangePasswordForm;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void createUser(RegisterUserForm userForm, String role) throws UserAlreadyExistException {
        if (usernameExists(userForm.getName())) {
            throw new UserAlreadyExistException("There is an account with the username '" + userForm.getName() + "'");
        }
        User user = new User();
        user.setName(userForm.getName());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setRole(role);
        this.save(user);
    }

    public boolean usernameExists(String name) {
        return userRepository.findByName(name).isPresent();
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

    public User findCurrentUser() {
        Optional<User> user = userRepository.findByName(User.getCurrentUsername());
        return user.orElse(null);
    }

    public boolean checkOldPassword(String rawPassword, User user) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public ChangePasswordForm changePassword(ChangePasswordForm changePasswordForm) throws IncorrectPasswordException {
        // TODO Als Nutzer kann ich mein Passwort ändern

        // Load the authenticated user
        Optional<User> userOptional = userRepository.findByName(User.getCurrentUsername());

        if (userOptional.isPresent()) {
            // check if user was found
            User user = userOptional.get();

            // Check old password
            if (!checkOldPassword(changePasswordForm.getOldPassword(), user)) {
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

    public boolean changeRole(Long id, String role) {
        // TODO Als Admin kann ich die Rollen anderer Nutzer ändern
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRole(role);
            userRepository.save(user);
        }
        return false;
    }

}
