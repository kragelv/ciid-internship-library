package io.kragelv.library.controller.command.constant;

public class CommandPaths {
    
    private CommandPaths () { }

    public static final String GO_TO_INDEX = "/";
    public static final String LIST_BOOKS = "/books";
    public static final String LIST_GENRES = "/genres";
    public static final String VIEW_BOOK = "/book";
    public static final String VIEW_GENRE = "/genre";

    public static final String CREATE_BOOK = "/actions/book/create";
    public static final String UPDATE_BOOK = "/actions/book/edit";
    public static final String DELETE_BOOK = "/actions/book/delete";

    public static final String CREATE_GENRE = "/actions/genre/create";
    public static final String UPDATE_GENRE = "/actions/genre/edit";
    public static final String DELETE_GENRE = "/actions/genre/delete";

}
