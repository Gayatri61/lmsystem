package com.gt.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.gt.dao.UserDAO;
import com.gt.exception.LMSException;
import com.gt.model.User;
import com.gt.observer.UserCache;

public class UserManager extends ObservableManager {

    private static final Logger LOGGER = Logger.getLogger(UserManager.class.getName());

    private static UserManager instance = null;

    private final UserDAO userDAO;

    private final UserCache userCache;

    // Privater Konstruktor verhindert new UserManager()
    private UserManager() {
        userDAO = new UserDAO();

        userCache = UserCache.getInstance();

        addObserver(userCache);
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
                    "Registrierung fehlgeschlagen: User ist null.");

            throw new LMSException(
                    "Benutzer darf nicht null sein.");
        }

        try {
            // E-Mail prüfen
            if (userDAO.findByEmail(user.getEmail()) != null) {

                LOGGER.warning(
                        "E-Mail-Adresse existiert bereits: "
                                + user.getEmail());

                throw new LMSException(
                        "Email ist bereits vergeben: "
                                + user.getEmail());
            }

            // User speichern
            userDAO.create(user);

             notifyObservers();

            LOGGER.info(
                    "Benutzer registriert: ID="
                            + user.getId());

        } catch (SQLException e) {

            LOGGER.warning(
                    "Datenbankfehler beim Erstellen des Benutzers.");

            throw new LMSException(
                    "Benutzer konnte nicht erstellt werden.",
                    e);
        }
    }

    public List<User> getAllUsers() {

        if (!userCache.isEmpty()) {
            LOGGER.info("Benutzer aus Cache abgerufen.");
            return new ArrayList<>(
                    userCache.getAll());
        }
        try {
            List<User> users = userDAO.findAll();
            LOGGER.info("Benutzer aus Datenbank abgerufen.");
            userCache.putAll(users);

            return users;
        } catch (SQLException e) {

            LOGGER.warning(
                    "Fehler beim Abrufen aller Benutzer: "
                            + e.getMessage());
            return new ArrayList<>();
        }

    }

    public void removeUser(int id) throws LMSException {

        try {
            userDAO.delete(id);

            notifyObservers();

        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Löschen des Benutzers: "
                            + e.getMessage());
            throw new LMSException(
                    "Fehler beim Löschen des Benutzers.",
                    e);
        }

    }

    public void updateUser(User user)
        throws LMSException {

    try {

        userDAO.update(user);

        notifyObservers();

    } catch (SQLException e) {
        throw new LMSException(
                "Benutzer konnte nicht aktualisiert werden.",
                e
        );
    }
}

    public User findUserById(int id) throws LMSException {

        // 1. Cache prüfen
        User cachedUser = userCache.get(id);

        if (cachedUser != null) {
            return cachedUser;
        }

        try {
            // 2. Datenbank abfragen
            User user = userDAO.findById(id);

            // 3. Ergebnis cachen
            if (user != null) {
                userCache.put(user);
            }
            return user;
        } catch (SQLException e) {
            LOGGER.severe(
                    "Fehler beim Abrufen des Benutzers: "
                            + e.getMessage());
            throw new LMSException(
                    "Fehler beim Abrufen des Benutzers: "
                            + e.getMessage());
        }
    }

    public User findUserByEmail(String email) throws LMSException {
        try {
            return userDAO.findByEmail(email);
        } catch (SQLException e) {
            LOGGER.severe(
                    "Fehler beim Abrufen des Benutzers: "
                            + e.getMessage());
            throw new LMSException(
                    "Fehler beim Abrufen des Benutzers: "
                            + e.getMessage());
        }
    }

}