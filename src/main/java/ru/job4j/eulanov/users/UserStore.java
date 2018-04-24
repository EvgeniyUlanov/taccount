package ru.job4j.eulanov.users;

import ru.job4j.eulanov.Const;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import ru.job4j.eulanov.dbconnection.DbConnectionPool;

public class UserStore {

    private final static UserStore USER_STORE = new UserStore();

    private UserStore() {
        try {
            Class.forName("ru.job4j.eulanov.dbconnection.DbConnectionPool");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static UserStore getInstance() {
        return USER_STORE;
    }

    public void createTable() {
        try (Connection conn = DbConnectionPool.getDataSource().getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate("DROP TABLE IF EXISTS roles");
            st.executeUpdate("DROP TABLE IF EXISTS users");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS roles (role_id INTEGER PRIMARY KEY, "
                    + "role_name VARCHAR(10));");
            st.executeUpdate("INSERT INTO roles (role_id, role_name) VALUES (1, 'user');");
            st.executeUpdate("INSERT INTO roles (role_id, role_name) VALUES (2, 'admin');");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "name VARCHAR(255) PRIMARY KEY, "
                    + "password VARCHAR(255) NOT NULL, "
                    + "start_date TIMESTAMP, "
                    + "finish_date TIMESTAMP, "
                    + "status VARCHAR(255), "
                    + "work_time INTEGER, "
                    + "start_time TIMESTAMP, "
                    + "role_id INTEGER);");
            st.executeUpdate("INSERT INTO users (name, password, role_id) "
                    + "VALUES ('admin', '1', (SELECT role_id FROM roles WHERE role_name = 'admin'));");
            st.executeUpdate("INSERT INTO users (name, password, role_id, status) "
                    + "VALUES ('ivan', '1', (SELECT role_id FROM roles WHERE role_name = 'user'), 'home');");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addUser(User user) {
        User userToCheck = UserStore.getInstance().getUser(user.getName());
        if (userToCheck == null) {
            try (Connection conn = DbConnectionPool.getDataSource().getConnection();
                 PreparedStatement st =
                         conn.prepareStatement("INSERT INTO users (name, password, role_id, status) "
                                 + "VALUES (?, ?, (SELECT role_id FROM roles WHERE role_name=?), ?)")) {
                st.setString(1, user.getName());
                st.setString(2, user.getPassword());
                st.setString(3, user.getRole());
                st.setString(4, Const.STATUS_HOME);
                st.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized User getUser(String name) {
        User user = null;
        try (Connection conn = DbConnectionPool.getDataSource().getConnection();
             PreparedStatement st =
                     conn.prepareStatement("SELECT name, password, start_date, finish_date, status, work_time, start_time, role_name FROM users "
                             + "INNER JOIN roles ON users.role_id=roles.role_id WHERE name = (?)")) {
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                user = generateUser(rs);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    public synchronized List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DbConnectionPool.getDataSource().getConnection();
             PreparedStatement st =
                     conn.prepareStatement("SELECT name, password, start_date, finish_date, status, work_time, start_time, role_name FROM users "
                             + "INNER JOIN roles ON users.role_id=roles.role_id")) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                users.add(generateUser(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    private User generateUser(ResultSet rs) throws SQLException {
        String userName = rs.getString("name");
        String userPassword = rs.getString("password");
        Timestamp startDate = rs.getTimestamp("start_date");
        Timestamp finishDate = rs.getTimestamp("finish_date");
        String userStatus = rs.getString("status");
        long workTime = rs.getLong("work_time");
        Timestamp startTime = rs.getTimestamp("start_time");
        String role = rs.getString("role_name");
        User user = new User(userName);
        user.setPassword(userPassword);
        user.setStartDate(startDate);
        user.setFinishDate(finishDate);
        user.setStatus(userStatus);
        user.setWorkTime(workTime);
        user.setStartTime(startTime);
        user.setRole(role);
        return user;
    }

    public synchronized void setUserStartWork(String userName) {
        String userStatus = getUserStatus(userName);
        if (userStatus == null || userStatus.equals(Const.STATUS_HOME)) {
            try (Connection conn = DbConnectionPool.getDataSource().getConnection();
                 PreparedStatement st =
                         conn.prepareStatement("UPDATE users SET start_date = ?, status = ?, work_time = 0, start_time = ? " +
                                 "WHERE name = ?")) {
                st.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                st.setString(2, Const.STATUS_ON_WORK);
                st.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                st.setString(4, userName);
                st.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else if (userStatus.equals(Const.STATUS_OUT)) {
            userContinueWork(userName);
        }
    }

    private void userContinueWork(String userName) {
        try (Connection conn = DbConnectionPool.getDataSource().getConnection();
             PreparedStatement st =
                     conn.prepareStatement("UPDATE users SET status = ?, start_time = ? " +
                             "WHERE name = ?")) {
            st.setString(1, Const.STATUS_ON_WORK);
            st.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            st.setString(3, userName);
            st.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setUserOut(String userName) {
        stopWork(userName, Const.STATUS_OUT);
    }

    public synchronized void setUserFinish(String userName) {
        stopWork(userName, Const.STATUS_FINISHED);
        try (Connection conn = DbConnectionPool.getDataSource().getConnection();
             PreparedStatement st =
                     conn.prepareStatement("UPDATE users SET finish_date = ? WHERE name = ?")) {
            st.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            st.setString(2, userName);
            st.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void stopWork(String userName, String setStatus) {
        User user = USER_STORE.getUser(userName);
        if (user.getStatus() != null && user.getStatus().equals(Const.STATUS_ON_WORK)) {
            try (Connection conn = DbConnectionPool.getDataSource().getConnection();
                 PreparedStatement st =
                         conn.prepareStatement("UPDATE users SET work_time = ?, status = ? " +
                                 "WHERE name = ?")) {
                user.stopWork();
                st.setLong(1, user.getWorkTime());
                st.setString(2, setStatus);
                st.setString(3, userName);
                st.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try (Connection conn = DbConnectionPool.getDataSource().getConnection();
                 PreparedStatement st =
                         conn.prepareStatement("UPDATE users SET status = ? WHERE name = ?")) {
                user.stopWork();
                st.setString(1, setStatus);
                st.setString(2, userName);
                st.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String getUserStatus(String name) {
        String result = "";
        try (Connection conn = DbConnectionPool.getDataSource().getConnection();
             PreparedStatement st =
                     conn.prepareStatement("SELECT status FROM users WHERE name = ?;")) {
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = rs.getString("status");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
