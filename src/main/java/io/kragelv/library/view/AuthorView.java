package io.kragelv.library.view;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import io.kragelv.library.model.Author;
import io.kragelv.library.service.AuthorService;
import io.kragelv.library.service.exception.ServiceException;
import io.kragelv.library.service.impl.AuthorServiceImpl;
import io.kragelv.library.view.model.AuthorInput;

public class AuthorView {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthorService authorService = new AuthorServiceImpl();

    public void show() {
        while (true) {
            try {
                System.out.println("\n===== Author Management =====");
                System.out.println("1. Add Author");
                System.out.println("2. View All Authors");
                System.out.println("3. Find Author by ID");
                System.out.println("4. Update Author");
                System.out.println("5. Delete Author");
                System.out.println("0. Back to Main Menu");
                System.out.print("Choose an option: ");
    
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        addAuthor();
                        break;
                    case "2":
                        viewAllAuthors();
                        break;
                    case "3":
                        findAuthorById();
                        break;
                    case "4":
                        updateAuthor();
                        break;
                    case "5":
                        deleteAuthor();
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

    private AuthorInput enterAuthor() {
        System.out.print("Enter first name (required): ");
        String firstName;
        while (true) {
            firstName = scanner.nextLine().trim();
            if (!firstName.isEmpty())
                break;
            System.out.print("First name cannot be empty. Try again: ");
        }

        System.out.print("Enter middle name (press Enter to skip): ");
        String middleName = scanner.nextLine().trim();
        if (middleName.isEmpty())
            middleName = null;

        System.out.print("Enter last name (press Enter to skip): ");
        String lastName = scanner.nextLine().trim();
        if (lastName.isEmpty())
            lastName = null;

        System.out.print("Enter birth year (press Enter to skip): ");
        String birthYearInput = scanner.nextLine().trim();
        Integer birthYear = null;
        while (true) {
            if (birthYearInput.isEmpty()) {
                break;
            }
            try {
                birthYear = Integer.parseInt(birthYearInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid birth year. Try again: ");
            }
        }
        return new AuthorInput(firstName, middleName, lastName, birthYear);
    }

    private AuthorInput enterAuthorToUpdate(Author author) {
        System.out.print("Enter first name (press Enter to skip): ");
        String firstName = scanner.nextLine().trim();
        if (firstName.isEmpty())
            firstName = author.getFirstName();

        System.out.print("Enter middle name (press Enter to skip): ");
        String middleName = scanner.nextLine().trim();
        if (middleName.isEmpty())
            middleName = author.getMiddleName();

        System.out.print("Enter last name (press Enter to skip): ");
        String lastName = scanner.nextLine().trim();
        if (lastName.isEmpty())
            lastName = author.getLastName();

        System.out.print("Enter birth year (press Enter to skip): ");
        String birthYearInput = scanner.nextLine().trim();
        Integer birthYear = null;
        while (true) {
            if (birthYearInput.isEmpty()) {
                birthYear = author.getBirthYear();
                break;
            }
            try {
                birthYear = Integer.parseInt(birthYearInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid birth year. Try again: ");
            }
        }
        return new AuthorInput(firstName, middleName, lastName, birthYear);
    }

    private void addAuthor() {
        AuthorInput authorInput = enterAuthor();

        Author newAuthor = authorService.createAuthor(authorInput.getFirstName(),
                authorInput.getMiddleName(),
                authorInput.getLastName(),
                authorInput.getBirthYear());
        System.out.println("Author created: " + newAuthor);
    }

    private void viewAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        if (authors.isEmpty()) {
            System.out.println("No authors found.");
        } else {
            for (Author author : authors) {
                System.out.println(author);
            }
        }
    }

    private void findAuthorById() {
        System.out.print("Enter author ID: ");
        UUID id = UUID.fromString(scanner.nextLine());
        Author author = authorService.getAuthorById(id);
        System.out.println(author);
    }

    private void updateAuthor() {
        System.out.print("Enter author ID: ");
        UUID id = UUID.fromString(scanner.nextLine());

        Author author = authorService.getAuthorById(id);
        System.out.println(author);
        AuthorInput updatedAuthor = enterAuthorToUpdate(author);

        authorService.updateAuthor(id,
                updatedAuthor.getFirstName(),
                updatedAuthor.getMiddleName(),
                updatedAuthor.getLastName(),
                updatedAuthor.getBirthYear());
        System.out.println("Author updated successfully.");
    }

    private void deleteAuthor() {
        System.out.print("Enter author ID: ");
        UUID id = UUID.fromString(scanner.nextLine());
        authorService.deleteAuthor(id);
        System.out.println("Author deleted successfully.");
    }
}
