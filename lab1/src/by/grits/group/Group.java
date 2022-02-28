package by.grits.group;

import by.grits.user.User;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private User creator;
    private String id;
    private List<User> users;

    public Group(User creator, String id) {
        this.creator = creator;
        this.id = id;
        this.users = new ArrayList<>();
    }

    public User getCreator() {
        return creator;
    }

    public String getId() {
        return id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void registerUser(User user) {
        users.add(user);
    }
}
