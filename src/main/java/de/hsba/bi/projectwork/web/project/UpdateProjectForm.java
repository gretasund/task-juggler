package de.hsba.bi.projectwork.web.project;

import de.hsba.bi.projectwork.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class UpdateProjectForm {

    List<User> newUsers;

    public UpdateProjectForm(List<User> users) {
        this.newUsers = users;
    }

}
