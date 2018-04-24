package ru.job4j.eulanov.users;

import ru.job4j.eulanov.Const;

import java.sql.Timestamp;

public class User {

    private String name;
    private String password;
    private long workTime;
    private long startTime;
    private boolean inWork;
    private String status;
    private String role;
    private Timestamp startDate;
    private Timestamp finishDate;

    public User(String name) {
        this.name = name;
        this.password = "1";
        this.status = Const.STATUS_HOME;
        this.role = "user";
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWorkTimeString() {
        long hour = workTime / 3600;
        long minutes = (workTime % 3600) / 60;
        long sec = (workTime % 3600) % 60;
        return String.format("%sh : %sm : %ss", hour, minutes, sec);
    }

    public void startWork() {
        if (!inWork) {
            this.startTime = System.currentTimeMillis();
        }
        inWork = true;
    }

    public void stopWork() {
        if (inWork) {
            long finishTime = System.currentTimeMillis();
            workTime += (finishTime - startTime) / 1000;
        }
        inWork = false;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status != null) {
            this.status = status;
            if (status.equals(Const.STATUS_ON_WORK)) {
                inWork = true;
            } else {
                inWork = false;
            }
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Timestamp finishDate) {
        this.finishDate = finishDate;
    }

    public void setWorkTime(long workTime) {
        this.workTime = workTime;
    }

    public long getWorkTime() {
        return workTime;
    }

    public void setStartTime(Timestamp startTime) {
        if (startTime != null) {
            this.startTime = startTime.getTime();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
