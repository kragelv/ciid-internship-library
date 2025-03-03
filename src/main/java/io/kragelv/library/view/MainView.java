package io.kragelv.library.view;

import java.util.Scanner;

import io.kragelv.library.service.exception.ServiceException;

public class MainView {
    private final Scanner scanner = new Scanner(System.in);
    private final GenreView genreView = new GenreView();
    private final AuthorView authorView = new AuthorView();
    private final BookView bookView = new BookView();

    public void show() {
        try {
            loop();
        } catch (Exception e) {
            System.out.println("Internal error occurred. Please, try again.");
            e.printStackTrace(System.err);
        }

    }

    private void loop() {
        while (true) {
            try {
                System.out.println("\n===== Library Management System =====");
                System.out.println("1. Manage Readers");
                System.out.println("2. Manage Books");
                System.out.println("3. Manage Borrowings");
                System.out.println("4. Manage Genres");
                System.out.println("5. Manage Authors");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        // readerView.show();
                        break;
                    case "2":
                        bookView.show();
                        break;
                    case "3":
                        // borrowingView.show();
                        break;
                    case "4":
                        genreView.show();
                        break;
                    case "5":
                        authorView.show();
                        break;
                    case "0":
                        System.out.println("Exit");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (ServiceException e) {
                System.out.println("Warning:" + e.getMessage());
            }
        }
    }

}
