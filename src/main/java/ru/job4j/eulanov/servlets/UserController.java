package ru.job4j.eulanov.servlets;

import ru.job4j.eulanov.dbconnection.DbConnectionPool;
import ru.job4j.eulanov.users.User;
import ru.job4j.eulanov.users.UserStore;

import javax.servlet.ServletConfig;
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
        String role = (String) req.getSession().getAttribute("role");
        if (role.equals("admin")) {
            req.setAttribute("users", UserStore.getInstance().getAllUsers());
            req.getRequestDispatcher("/WEB-INF/views/AdminView.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/UserView.jsp").forward(req, resp);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        UserStore.getInstance().createTable();
    }

    @Override
    public void destroy() {
        DbConnectionPool.closeConnection();
    }
}
