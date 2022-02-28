package by.grits.group;

import by.grits.user.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroupDao {
    private Map<String, Group> groups = new ConcurrentHashMap<>();
    private Map<String, User> pendingUsers = new ConcurrentHashMap<>();

    public void addGroup(Group group) {
        groups.put(group.getId(), group);
    }

    public Group findGroupById(String id) {
        return groups.get(id);
    }

    public void addUserToGroup(String id, User user) {
        groups.get(id).registerUser(user);
    }

    public User findGroupCreator(String groupId) {
        Group group = groups.get(groupId);
        return group.getCreator();
    }

    public void addUser(User user, String groupId) {
        pendingUsers.put(groupId, user);
    }

    public void removeUser(User user, String groupId) {
        pendingUsers.remove(groupId, user);
    }

    public User getUser(String groupId) {
        return pendingUsers.get(groupId);
    }

}
