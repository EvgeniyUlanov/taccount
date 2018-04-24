package ru.job4j.eulanov.servlets;

import ru.job4j.eulanov.users.UserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StartWork extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserStore.getInstance().setUserStartWork((String) req.getSession().getAttribute("user"));
        req.getRequestDispatcher("/").forward(req, resp);
    }
}
