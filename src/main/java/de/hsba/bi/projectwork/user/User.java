package de.hsba.bi.projectwork.user;

import de.hsba.bi.projectwork.project.Project;

import de.hsba.bi.projectwork.task.Task;
import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Data
public class User implements Comparable<User> {

    public static String ADMIN_ROLE = "ADMIN";
    public static String DEVELOPER_ROLE = "DEVELOPER";
    public static String MANAGER_ROLE = "MANAGER";


    // TODO move to UserService
    public static String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }

    // TODO delete
    public static List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        roles.add(ADMIN_ROLE);
        roles.add(DEVELOPER_ROLE);
        roles.add(MANAGER_ROLE);
        return roles;
    }


    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;

    @Basic(optional = false)
    private String name;

    @Basic(optional = false)
    private String password;

    @Basic(optional = false)
    private String role;

    @ManyToMany(cascade = CascadeType.ALL)
    @OrderBy
    private List<Project> projects;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "assignee")
    @OrderBy
    private List<Task> assignedTasks;

    @Override
    public int compareTo(User other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name;
    }

}
