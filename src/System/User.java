package System;

import Data.UsersData;

public class User{
    private String username;
    private int id;
    private String password;
    public User(String username, String password) {
        this.username = username;
        this.id = UsersData.getNumberOfUsers()+1;
        this.password = password;
        UsersData.incrementNumberOfUsers();
        UsersData.addUser(this);
    }
    public int getId() {
        return id;
    }
    public String getUserName() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}