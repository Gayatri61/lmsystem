package com.gt;

import java.util.List;
import java.util.Scanner;
import com.gt.config.LoggingConfig;
import com.gt.enums.CourseCategory;
import com.gt.enums.DifficultyLevel;
import com.gt.exception.LMSException;
import com.gt.filter.CategoryFilter;
import com.gt.filter.CourseFilterStrategy;
import com.gt.filter.DifficultyFilter;
import com.gt.filter.PriceFilter;
import com.gt.manager.CourseManager;
import com.gt.manager.EnrollmentManager;
import com.gt.manager.UserManager;
import com.gt.model.Course;
import com.gt.model.Lesson;
import com.gt.model.Student;
import com.gt.model.User;

public class App {

    public static void main(String[] args) {

        LoggingConfig.configure();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();

            System.out.print("Auswahl: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    System.out.println("Kurse anzeigen");
                    break;

                case "2":
                    System.out.println("Für Kurs anmelden");
                    break;

                case "3":
                    System.out.println("Lernfortschritt anzeigen");
                    break;

                case "4":
                    System.out.println("Kursverwaltung");
                    break;

                case "5":
                    searchCourses(scanner);
                    break;

                case "6":
                    filterCourses(scanner);
                    break;
                case "7":
                    rateCourse(scanner);

                case "8":
                    completeLesson(scanner);
                    break;

                case "0":
                    running = false;
                    System.out.println(
                        "LMS wird beendet. Auf Wiedersehen!"
                    );
                    break;

                default:
                    System.out.println(
                        "Ungültige Auswahl. Bitte erneut versuchen."
                    );
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("==============================");
        System.out.println(" JavaTech Solutions - LMS");
        System.out.println("==============================");
        System.out.println("1 - Kurse anzeigen");
        System.out.println("2 - Für Kurs anmelden");
        System.out.println("3 - Lernfortschritt anzeigen");
        System.out.println("4 - Kursverwaltung");
        System.out.println("5 - Kurse suchen");
        System.out.println("6 - Kurse filtern");
        System.out.println("7 - Kurs bewerten");
        System.out.println("8 - Lektion als abgeschlossen markieren");
        System.out.println("0 - Programm beenden");
        System.out.println("==============================");
    }

    private static void searchCourses(Scanner scanner) {

    CourseManager courseManager =
            CourseManager.getInstance();

    System.out.print(
            "Suchbegriff oder Regex eingeben: "
    );

    String searchPattern = scanner.nextLine();

    try {

        List<Course> results =
                courseManager.searchCourses(searchPattern);

        if (results.isEmpty()) {
            System.out.println(
                    "Keine passenden Kurse gefunden."
            );
            return;
        }

        System.out.println("\nGefundene Kurse:");

        for (Course course : results) {

            System.out.println(
                    course.getId()
                            + " | "
                            + course.getTitle()
                            + " | "
                            + course.getDescription()
            );
        }

    } catch (LMSException e) {

        System.out.println(
                "Fehler: " + e.getMessage()
        );
    }
}

private static void filterCourses(Scanner scanner) {

    CourseManager courseManager =
            CourseManager.getInstance();

    System.out.println("\n--- Kurse filtern ---");
    System.out.println("1 - Nach Kategorie");
    System.out.println("2 - Nach Schwierigkeitsgrad");
    System.out.println("3 - Nach Preis");

    System.out.print("Auswahl: ");
    String choice = scanner.nextLine();

    CourseFilterStrategy strategy;

    switch (choice) {

        case "1":
            strategy = createCategoryFilter(scanner);
            break;

        case "2":
            strategy = createDifficultyFilter(scanner);
            break;

        case "3":
            strategy = createPriceFilter(scanner);
            break;

        default:
            System.out.println("Ungültige Auswahl.");
            return;
    }

    List<Course> results =
            courseManager.filterCourses(strategy);

    printCourses(results);
}

private static CourseFilterStrategy createCategoryFilter(
        Scanner scanner) {

    System.out.println("\nKategorie:");
    System.out.println("1 - Programming");
    System.out.println("2 - Database");
    System.out.println("3 - Web Development");
    System.out.println("4 - DevOps");
    System.out.println("5 - Other");

    System.out.print("Auswahl: ");
    String choice = scanner.nextLine();

    CourseCategory category;

    switch (choice) {

        case "1":
            category = CourseCategory.PROGRAMMING;
            break;

        case "2":
            category = CourseCategory.DATABASE;
            break;

        case "3":
            category = CourseCategory.WEB_DEVELOPMENT;
            break;

        case "4":
            category = CourseCategory.DEVOPS;
            break;

        default:
            category = CourseCategory.OTHER;
            break;
    }

    return new CategoryFilter(category);
}

private static CourseFilterStrategy createDifficultyFilter(
        Scanner scanner) {

    System.out.println("\nSchwierigkeitsgrad:");
    System.out.println("1 - Beginner");
    System.out.println("2 - Intermediate");
    System.out.println("3 - Advanced");

    System.out.print("Auswahl: ");
    String choice = scanner.nextLine();

    DifficultyLevel level;

    switch (choice) {

        case "1":
            level = DifficultyLevel.BEGINNER;
            break;

        case "2":
            level = DifficultyLevel.INTERMEDIATE;
            break;

        case "3":
            level = DifficultyLevel.ADVANCED;
            break;

        default:
            System.out.println(
                    "Ungültige Auswahl. Beginner wird verwendet."
            );

            level = DifficultyLevel.BEGINNER;
    }

    return new DifficultyFilter(level);
}

private static CourseFilterStrategy createPriceFilter(
        Scanner scanner) {

    System.out.print("Minimaler Preis: ");
    double minPrice =
            Double.parseDouble(scanner.nextLine());

    System.out.print("Maximaler Preis: ");
    double maxPrice =
            Double.parseDouble(scanner.nextLine());

    return new PriceFilter(
            minPrice,
            maxPrice
    );
}

private static void printCourses(
        List<Course> courses) {

    if (courses.isEmpty()) {
        System.out.println(
                "Keine passenden Kurse gefunden."
        );
        return;
    }

    System.out.println("\nGefundene Kurse:");

    for (Course course : courses) {

        System.out.println(
                course.getId()
                + " | "
                + course.getTitle()
                + " | "
                + course.getCategory()
                + " | "
                + course.getDifficultyLevel()
                + " | "
                + course.getPrice()
                + " €"
        );
    }
}

private static void rateCourse(Scanner scanner) {

    UserManager userManager =
            UserManager.getInstance();

    CourseManager courseManager =
            CourseManager.getInstance();

    EnrollmentManager enrollmentManager =
            EnrollmentManager.getInstance();

    try {

        System.out.print("Student-ID: ");
        int studentId =
                Integer.parseInt(scanner.nextLine());

        User user =
                userManager.findUserById(studentId);

        if (!(user instanceof Student)) {
            System.out.println(
                    "Student nicht gefunden."
            );
            return;
        }

        Student student = (Student) user;

        System.out.print("Kurs-ID: ");
        int courseId =
                Integer.parseInt(scanner.nextLine());

        Course course =
                courseManager.findCourseById(courseId);

        if (course == null) {
            System.out.println(
                    "Kurs nicht gefunden."
            );
            return;
        }

        System.out.print("Sterne (1-5): ");
        int stars =
                Integer.parseInt(scanner.nextLine());

        enrollmentManager.rateCourse(
                student,
                course,
                stars
        );

        System.out.println(
                "Bewertung gespeichert."
        );

    } catch (LMSException e) {

        System.out.println(
                "Fehler: " + e.getMessage()
        );

    } catch (NumberFormatException e) {

        System.out.println(
                "Bitte eine gültige Zahl eingeben."
        );
    }
}

private static void completeLesson(Scanner scanner) {

    UserManager userManager =
            UserManager.getInstance();

    CourseManager courseManager =
            CourseManager.getInstance();

    EnrollmentManager enrollmentManager =
            EnrollmentManager.getInstance();

    try {

        // Student auswählen
        System.out.print("Student-ID: ");
        int studentId =
                Integer.parseInt(scanner.nextLine());

        User user =
                userManager.findUserById(studentId);

        if (!(user instanceof Student)) {
            System.out.println(
                    "Student nicht gefunden."
            );
            return;
        }

        Student student = (Student) user;

        // Kurs auswählen
        System.out.print("Kurs-ID: ");
        int courseId =
                Integer.parseInt(scanner.nextLine());

        Course course =
                courseManager.findCourseById(courseId);

        if (course == null) {
            System.out.println(
                    "Kurs nicht gefunden."
            );
            return;
        }

        // Lektionen anzeigen
        System.out.println("\nLektionen:");

        for (Lesson lesson : course.getLessons()) {

            System.out.println(
                    lesson.getId()
                    + " - "
                    + lesson.getTitle()
            );
        }

        // Lektion auswählen
        System.out.print("\nLektion-ID: ");
        int lessonId =
                Integer.parseInt(scanner.nextLine());

        Lesson selectedLesson = null;

        for (Lesson lesson : course.getLessons()) {

            if (lesson.getId() == lessonId) {
                selectedLesson = lesson;
                break;
            }
        }

        if (selectedLesson == null) {
            System.out.println(
                    "Lektion nicht gefunden."
            );
            return;
        }

        // Lektion abschließen
        enrollmentManager.markLessonAsCompleted(
                student,
                course,
                selectedLesson
        );

        System.out.println(
                "Lektion wurde als abgeschlossen markiert."
        );

        // neuen Fortschritt berechnen
        double progress =
                enrollmentManager.calculateCourseProgress(
                        student,
                        course
                );

        System.out.printf(
                "Kursfortschritt: %.1f%%%n",
                progress
        );

    } catch (LMSException e) {

        System.out.println(
                "Fehler: " + e.getMessage()
        );

    } catch (NumberFormatException e) {

        System.out.println(
                "Bitte eine gültige Zahl eingeben."
        );
    }
}
}