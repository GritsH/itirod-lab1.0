package by.grits.group;

import by.grits.user.User;

public class GroupService {
    private GroupDao groupDao;

    public GroupService() {
        groupDao = new GroupDao();
    }

    public void addGroup(Group group) {
        groupDao.addGroup(group);
    }

    public Group findGroupById(String id) {
        return groupDao.findGroupById(id);
    }

    public void addUserToGroup(String groupId, User user) {
        groupDao.addUserToGroup(groupId, user);
    }

    public User getGroupCreator(String groupId) {
        return groupDao.findGroupCreator(groupId);
    }

    public void addUserToWaitingList(User user, String groupId) {
        groupDao.addUser(user, groupId);
    }

    public void removeUserFromWaitingList(User user, String groupId) {
        groupDao.removeUser(user, groupId);
    }

    public User getUserFromWaitingList(String groupId) {
        return groupDao.getUser(groupId);
    }
}
