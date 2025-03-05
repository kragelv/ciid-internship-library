package io.kragelv.library.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.kragelv.library.controller.command.Command;
import io.kragelv.library.controller.command.CommandFactory;
import io.kragelv.library.controller.command.constant.JspPaths;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(Controller.class);

    private void trySendError(HttpServletResponse response) {
        if (!response.isCommitted()) {
            response.reset();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException ex) {
                logger.error("Error sending error in response", ex);
                try {
                    response.setContentType("text/html; charset=UTF-8");
                    try (PrintWriter writer = response.getWriter()) {
                        writer.println("<html><body><h1>500 - Internal Server Error</h1></body></html>");
                    }
                } catch (IOException ioEx) {
                    logger.error("Error sending error in HTML response", ioEx);
                }
            }
        }
    }

    private void tryResponseErrorPage(HttpServletRequest request, HttpServletResponse response) {
    try {
        request.getRequestDispatcher(JspPaths.PAGES_PATH + JspPaths.ERROR_500).forward(request, response);
    } catch (Exception e) {
        logger.error("Error forwarding to error page", e);
        trySendError(response);
    }
}


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            process(req, resp);
        } catch (Exception e) {
            logger.error("doGet error: ", e);
            tryResponseErrorPage(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            process(req, resp);
        } catch (Exception e) {
            logger.error("doPost error: ", e);
            tryResponseErrorPage(req, resp);
        }
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Command command = CommandFactory.getCommand(request);
        command.execute(request, response);
    }
}
