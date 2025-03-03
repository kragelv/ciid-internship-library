package io.kragelv.library.view;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import io.kragelv.library.model.Genre;
import io.kragelv.library.service.GenreService;
import io.kragelv.library.service.exception.ServiceException;
import io.kragelv.library.service.impl.GenreServiceImpl;

public class GenreView {
    private final Scanner scanner = new Scanner(System.in);
    private final GenreService genreService = new GenreServiceImpl();

    public void show() {
        while (true) {
            try {
                System.out.println("\n===== Genre Management =====");
                System.out.println("1. Add Genre");
                System.out.println("2. View All Genres");
                System.out.println("3. Find Genre by ID");
                System.out.println("4. Update Genre");
                System.out.println("5. Delete Genre");
                System.out.println("0. Back to Main Menu");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        addGenre();
                        break;
                    case "2":
                        viewAllGenres();
                        break;
                    case "3":
                        findGenreById();
                        break;
                    case "4":
                        updateGenre();
                        break;
                    case "5":
                        deleteGenre();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (ServiceException e) {
                System.out.println("Warning: " + e.getMessage());
            }
        }
    }

    private void addGenre() {
        System.out.print("Enter genre name: ");
        String name = scanner.nextLine();

        Genre newGenre = genreService.createGenre(name);
        System.out.println("Genre created: " + newGenre);
    }

    private void viewAllGenres() {
        List<Genre> genres = genreService.getAllGenres();
        if (genres.isEmpty()) {
            System.out.println("No genres found.");
        } else {
            for (Genre genre : genres) {
                System.out.println(genre);
            }
        }
    }

    private void findGenreById() {
        System.out.print("Enter genre ID: ");
        UUID id = UUID.fromString(scanner.nextLine());
        Genre genre = genreService.getGenreById(id);
        System.out.println(genre);
    }

    private void updateGenre() {
        System.out.print("Enter genre ID: ");
        UUID id = UUID.fromString(scanner.nextLine());
        System.out.print("Enter new genre name: ");
        String name = scanner.nextLine();

        genreService.updateGenre(id, name);
        System.out.println("Genre updated successfully.");
    }

    private void deleteGenre() {
        System.out.print("Enter genre ID: ");
        UUID id = UUID.fromString(scanner.nextLine());
        genreService.deleteGenre(id);
        System.out.println("Genre deleted successfully.");
    }
}
