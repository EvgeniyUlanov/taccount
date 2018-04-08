package ru.job4j.eulanov.users;

public class User {

    private String name;
    private String password;
    private long workTime;
    private long startTime;
    private long finishTime;
    private boolean inWork;
    private String status;
    private String role;

    public User(String name) {
        this.name = name;
        this.password = "1";
        this.status = "on work";
        this.role = "user";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getWorkTime() {
        return workTime;
    }

    public void startWork() {
        this.startTime = System.currentTimeMillis();
        inWork = true;
    }

    public void stopWork() {
        if (inWork) {
            this.finishTime = System.currentTimeMillis();
            workTime += finishTime - startTime;
        }
        inWork = false;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isInWork() {
        return inWork;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
