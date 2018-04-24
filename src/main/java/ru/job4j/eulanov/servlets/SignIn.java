package ru.job4j.eulanov.servlets;

import ru.job4j.eulanov.users.User;
import ru.job4j.eulanov.users.UserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SignIn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/SignIn.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        User user = UserStore.getInstance().getUser(login);
        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = req.getSession();
            synchronized (session) {
                session.setAttribute("user", user.getName());
                session.setAttribute("role", user.getRole());
                resp.sendRedirect(String.format("%s/", req.getContextPath()));
            }
        } else {
            doGet(req, resp);
        }
    }
}
