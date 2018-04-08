package ru.job4j.eulanov.servlets;

import ru.job4j.eulanov.users.UserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("role").equals("admin")) {
            req.setAttribute("users", UserStore.getInstance().getAllUsers());
            req.getRequestDispatcher("/WEB-INF/views/AdminView.jsp").forward(req, resp);
        } else {
            req.setAttribute("currentUser", UserStore.getInstance()
                    .getUser((String)req.getSession().getAttribute("login")));
            req.getRequestDispatcher("/WEB-INF/views/UserView.jsp").forward(req, resp);
        }
    }
}
