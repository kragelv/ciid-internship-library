package io.kragelv.library.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import io.kragelv.library.model.Book;
import io.kragelv.library.service.AuthorService;
import io.kragelv.library.service.BookService;
import io.kragelv.library.service.exception.AuthorNotFoundException;
import io.kragelv.library.service.impl.AuthorServiceImpl;
import io.kragelv.library.service.impl.BookServiceImpl;
import io.kragelv.library.view.model.BookInput;

public class BookView {
    private final BookService bookService = new BookServiceImpl();
    private final AuthorService authorService = new AuthorServiceImpl();
    private final Scanner scanner = new Scanner(System.in);

    public void show() {
        while (true) {
            System.out.println("\n===== Book Management =====");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Delete Book");
            System.out.println("4. View All Books");
            System.out.println("5. Find Book by ID");
            System.out.println("6. Find Books by Author");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addBook();
                    break;
                case "2":
                    updateBook();
                    break;
                case "3":
                    deleteBook();
                    break;
                case "4":
                    viewAllBooks();
                    break;
                case "5":
                    findBookById();
                    break;
                case "6":
                    viewBooksByAuthor();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private UUID enterAuthorId(boolean required) {
        UUID id;
        String idInput;
        while (true) {
            idInput = scanner.nextLine().trim();
            if (!required && idInput.isEmpty()) {
                id = null;
                break;
            }
            id = UUID.fromString(idInput);
            try {
                authorService.getAuthorById(id);
                break;
            } catch (AuthorNotFoundException e) {
                System.out.println("Author not found. Try again: ");
            }
        }
        return id;
    }

    private List<String> enterGenres() {
        List<String> genres = new ArrayList<>();

        while (true) {
            System.out.print("Enter genre name (or press Enter to finish): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                break;
            }

            genres.add(input);
        }

        if (genres.isEmpty()) {
            System.out.println("No genres selected.");
        }

        return genres;
    }

    private BookInput enterBook() {
        System.out.print("Enter title (required): ");
        String title;
        while (true) {
            title = scanner.nextLine().trim();
            if (!title.isEmpty())
                break;
            System.out.print("Title cannot be empty. Try again: ");
        }

        System.out.print("Enter author ID (required): ");
        UUID authorId = enterAuthorId(true);

        System.out.print("Enter published year (press Enter to skip): ");
        String publishedYearInput = scanner.nextLine().trim();
        Integer publishedYear = null;
        while (true) {
            if (publishedYearInput.isEmpty()) {
                break;
            }
            try {
                publishedYear = Integer.parseInt(publishedYearInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid published year. Try again: ");
            }
        }

        System.out.print("Enter available copies (press Enter to skip): ");
        String availableCopiesInput = scanner.nextLine().trim();
        Integer availableCopies = null;
        while (true) {
            if (availableCopiesInput.isEmpty()) {
                break;
            }
            try {
                availableCopies = Integer.parseInt(availableCopiesInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid available copies number. Try again: ");
            }
        }

        List<String> genres = enterGenres();
        return new BookInput(title, authorId, publishedYear, availableCopies, genres);
    }

    private void addBook() {
        BookInput bookInput = enterBook();
        Book book = bookService.createBook(bookInput.getTitle(),
                bookInput.getAuthorId(),
                bookInput.getPublishedYear(),
                bookInput.getAvailableCopies(),
                bookInput.getGenres());
        System.out.println("Book added: " + book);
    }


    private BookInput enterBookToUpdate(Book book) {
        System.out.print("Enter title (press Enter to skip): ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty())
            title = book.getTitle();

        System.out.print("Enter author ID (press Enter to skip): ");
        UUID authorId = enterAuthorId(false);
        if (authorId == null) {
            authorId = book.getAuthorId();
        }

        System.out.print("Enter birth year (press Enter to skip): ");
        String publishedYearInput = scanner.nextLine().trim();
        Integer publishedYear = null;
        while (true) {
            if (publishedYearInput.isEmpty()) {
                publishedYear = book.getPublishedYear();
                break;
            }
            try {
                publishedYear = Integer.parseInt(publishedYearInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid published year. Try again: ");
            }
        }

        System.out.print("Enter available copies (press Enter to skip): ");
        String availableCopiesInput = scanner.nextLine().trim();
        Integer availableCopies = null;
        while (true) {
            if (availableCopiesInput.isEmpty()) {
                availableCopies = book.getAvailableCopies();
                break;
            }
            try {
                availableCopies = Integer.parseInt(availableCopiesInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid available copies number. Try again: ");
            }
        }

        List<String> genres = enterGenres();
        return new BookInput(title, authorId, publishedYear, availableCopies, genres);
    }

    private void updateBook() {
        System.out.print("Enter book ID: ");
        UUID id = UUID.fromString(scanner.nextLine());

        Book book = bookService.getBookById(id);
        System.out.println(book);
        BookInput updatedBook = enterBookToUpdate(book);

        bookService.updateBook(id,
                updatedBook.getTitle(),
                updatedBook.getAuthorId(),
                updatedBook.getPublishedYear(),
                updatedBook.getAvailableCopies(),
                updatedBook.getGenres());
        System.out.println("Book updated successfully.");
    }

    private void deleteBook() {
        System.out.print("Enter book ID: ");
        UUID id = UUID.fromString(scanner.nextLine());
        bookService.deleteBook(id);
        System.out.println("Book deleted successfully.");
    }

    private void viewAllBooks() {
        List<Book> books = bookService.getAllBooks();
        printBooks(books);
    }

    private void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    private void findBookById() {
        System.out.print("Enter book ID: ");
        UUID id = UUID.fromString(scanner.nextLine());
        Book book = bookService.getBookById(id);
        System.out.println(book);
    }

    private void viewBooksByAuthor() {
        System.out.print("Enter author ID: ");
        UUID authorId = UUID.fromString(scanner.nextLine());

        List<Book> books = bookService.getBooksByAuthor(authorId);
        printBooks(books);
    }
}
