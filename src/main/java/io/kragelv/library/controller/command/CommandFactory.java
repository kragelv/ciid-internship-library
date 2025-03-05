package io.kragelv.library.controller.command;

import java.util.HashMap;
import java.util.Map;

import io.kragelv.library.controller.command.constant.CommandPaths;
import io.kragelv.library.controller.command.constant.JspPaths;
import io.kragelv.library.controller.command.impl.ForwardCommand;
import io.kragelv.library.controller.command.impl.ListBooksCommand;
import io.kragelv.library.controller.command.impl.ListGenresCommand;
import io.kragelv.library.controller.command.impl.ViewBookCommand;
import io.kragelv.library.controller.command.impl.ViewGenreCommand;
import io.kragelv.library.controller.command.impl.action.CreateBookCommand;
import io.kragelv.library.controller.command.impl.action.CreateGenreCommand;
import io.kragelv.library.controller.command.impl.action.DeleteBookCommand;
import io.kragelv.library.controller.command.impl.action.DeleteGenreCommand;
import io.kragelv.library.controller.command.impl.action.UpdateBookCommand;
import io.kragelv.library.controller.command.impl.action.UpdateGenreCommand;
import jakarta.servlet.http.HttpServletRequest;

public class CommandFactory {
    private static final Map<String, Command> commands = new HashMap<>();
    private static final Command GO_TO_ERROR_404_COMMAND = new ForwardCommand(JspPaths.ERROR_404);

    private CommandFactory() {
    }

    static {
        commands.put(CommandPaths.GO_TO_INDEX, new ForwardCommand(JspPaths.INDEX));
        commands.put(CommandPaths.LIST_BOOKS, new ListBooksCommand());
        commands.put(CommandPaths.CREATE_BOOK, new CreateBookCommand());
        commands.put(CommandPaths.VIEW_BOOK, new ViewBookCommand());
        commands.put(CommandPaths.UPDATE_BOOK, new UpdateBookCommand());
        commands.put(CommandPaths.DELETE_BOOK, new DeleteBookCommand());
        commands.put(CommandPaths.LIST_GENRES, new ListGenresCommand());
        commands.put(CommandPaths.CREATE_GENRE, new CreateGenreCommand());
        commands.put(CommandPaths.VIEW_GENRE, new ViewGenreCommand());
        commands.put(CommandPaths.UPDATE_GENRE, new UpdateGenreCommand());
        commands.put(CommandPaths.DELETE_GENRE, new DeleteGenreCommand());
    }

    public static Command getCommand(HttpServletRequest request) {
        String commandName = request.getServletPath();
        return commands.getOrDefault(commandName, GO_TO_ERROR_404_COMMAND);
    }
}
