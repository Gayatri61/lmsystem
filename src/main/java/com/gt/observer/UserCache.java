package com.gt.observer;

import com.gt.model.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCache implements DataObserver {

    private static UserCache instance;

    private final Map<Integer, User> users =
            new HashMap<>();

    private UserCache() {
    }

    public static UserCache getInstance() {

        if (instance == null) {
            instance = new UserCache();
        }

        return instance;
    }

    public User get(int id) {
        return users.get(id);
    }

    public void put(User user) {
        users.put(user.getId(), user);
    }

    public void putAll(List<User> userList) {

        users.clear();

        for (User user : userList) {
            users.put(user.getId(), user);
        }
    }

    public Collection<User> getAll() {
        return users.values();
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }

    public void clear() {
        users.clear();
    }

    @Override
    public void onDataChanged() {
        clear();
    }
}
