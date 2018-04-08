package ru.job4j.eulanov.users;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserStore {

    private static UserStore instance = new UserStore();
    private List<User> users = new CopyOnWriteArrayList<>();

    private UserStore() {
        User user = new User("admin");
        user.setPassword("1");
        user.setRole("admin");
        users.add(user);
    }

    public static UserStore getInstance() {
        return instance;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User getUser(String name) {
        User result = null;
        for (User user : users) {
            if (user.getName().equals(name)) {
                result = user;
                break;
            }
        }
        return result;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public long getWorkTime(String userName) {
        return getUser(userName).getWorkTime();
    }

    public void startWork(String userName) {
        getUser(userName).startWork();
    }

    public void finishWork(String userName) {
        getUser(userName).stopWork();
    }
}
