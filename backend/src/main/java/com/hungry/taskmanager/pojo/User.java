package com.hungry.taskmanager.pojo;

public class User {

     int privateid;
    private String name;
    private String pwd;

    public User() {
    }

    public User(int privateid, String name, String pwd) {
        this.privateid = privateid;
        this.name = name;
        this.pwd = pwd;
    }

    public int getPrivateid() {
        return privateid;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPrivateid(int privateid) {
        this.privateid = privateid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "privateid=" + privateid +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
