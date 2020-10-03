package de.hsba.bi.projectwork.web.project;

import de.hsba.bi.projectwork.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class UpdateProjectForm {

    List<User> newUsers;

    public UpdateProjectForm(List<User> users) {
        this.newUsers = users;
    }

}
