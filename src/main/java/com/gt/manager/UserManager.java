package com.gt.manager;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.logging.Logger;

import com.gt.dao.UserDAO;
import com.gt.exception.LMSException;
import com.gt.model.User;

public class UserManager {

    private static final Logger LOGGER =
        Logger.getLogger(UserManager.class.getName());

    private static UserManager instance = null;

    private final UserDAO userDAO;

    // Privater Konstruktor verhindert new UserManager()
    private UserManager() {
        userDAO = new UserDAO();
    }

    public static UserManager getInstance() {
            if (instance == null) {
            instance = new UserManager();
        }

        return instance;
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

    try {
        // E-Mail prüfen
        if (userDAO.findByEmail(user.getEmail()) != null) {

            LOGGER.warning(
                    "E-Mail-Adresse existiert bereits: "
                            + user.getEmail()
            );

            throw new LMSException(
                    "Email ist bereits vergeben: "
                            + user.getEmail()
            );
        }

        // User speichern
        userDAO.create(user);

        LOGGER.info(
                "Benutzer registriert: ID="
                        + user.getId()
        );

    } catch (SQLException e) {

        LOGGER.warning(
                "Datenbankfehler beim Erstellen des Benutzers."  );

        throw new LMSException(
                "Benutzer konnte nicht erstellt werden.",
                e
        );
    }
}

    public HashMap<Integer, User> getAllUsers() {
        HashMap<Integer, User> users = new HashMap<>();
        try {
            users = userDAO.findAll().stream()
                    .collect(
                            HashMap::new,
                            (map, u) -> map.put(u.getId(), u),
                            HashMap::putAll
                    );
 } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void removeUser(int id) throws LMSException {

        HashMap<Integer, User> users = new HashMap<>();
        users = getAllUsers();

        if (!users.containsKey(id)) {
            throw new LMSException(
                    "Benutzer mit ID "
                    + id
                    + " wurde nicht gefunden."
            );
        }

        users.remove(id);
    }

    public User findUserById(int id) throws LMSException {
        try {
            return userDAO.findById(id);
        } catch (SQLException e) {
            LOGGER.severe(
                    "Fehler beim Abrufen des Benutzers: "
                    + e.getMessage()
            );
            throw new LMSException(
                    "Fehler beim Abrufen des Benutzers: "
                    + e.getMessage()
            );
        }
    }

    public User findUserByEmail(HashMap<Integer,User> users, String email) {

        for (User user : users.values()) {

            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }

        return null;
    }

}