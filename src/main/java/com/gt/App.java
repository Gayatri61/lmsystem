package com.gt;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {

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
        System.out.println("0 - Programm beenden");
        System.out.println("==============================");
    }
}