package io.kragelv.library.controller.command.impl;

import java.io.IOException;

import io.kragelv.library.controller.command.Command;
import io.kragelv.library.controller.command.constant.JspPaths;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ForwardCommand implements Command {

    private final String page;

    public ForwardCommand(String page) {
        this.page = page;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JspPaths.PAGES_PATH + page).forward(request, response);
    }
}
