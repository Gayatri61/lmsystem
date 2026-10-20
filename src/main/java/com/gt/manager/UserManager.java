package com.gt.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.gt.exception.LMSException;
import com.gt.model.User;

public class UserManager {

    private static final Logger LOGGER =
        Logger.getLogger(UserManager.class.getName());

    private static final UserManager INSTANCE = new UserManager();

    private final Map<Integer, User> users;

    // Privater Konstruktor verhindert new UserManager()
    private UserManager() {
        users = new HashMap<>();
    }

    public static UserManager getInstance() {
        return INSTANCE;
    }

    public void addUser(User user) throws LMSException {

        if (user == null) {
            LOGGER.warning(
                "Registrierung fehlgeschlagen: User ist null."
        );
            throw new LMSException(
                    "Benutzer darf nicht null sein."
            );
        }

        if (users.containsKey(user.getId())) {
            LOGGER.warning(
                "Benutzer-ID existiert bereits: "
                + user.getId() );
            throw new LMSException(
                    "Benutzer mit ID "
                    + user.getId()
                    + " existiert bereits."
            );
        }

        if (findUserByEmail(user.getEmail()) != null) {
            LOGGER.warning(
                "E-Mail-Adresse existiert bereits: "
                + user.getEmail()
        );

            throw new LMSException(
                    "Email ist bereits vergeben: "
                    + user.getEmail()
            );
        }

        users.put(user.getId(), user);
        LOGGER.info(
            "Benutzer registriert: ID=" + user.getId()
    );
    }

    public void removeUser(int id) throws LMSException {

        if (!users.containsKey(id)) {
            throw new LMSException(
                    "Benutzer mit ID "
                    + id
                    + " wurde nicht gefunden."
            );
        }

        users.remove(id);
    }

    public User findUserById(int id) {
        return users.get(id);
    }

    public User findUserByEmail(String email) {

        for (User user : users.values()) {

            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }

        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}