package System;

import Data.UsersData;

public interface UserManager {
    public static User findUser(String name, String password) {
        for(User user: UsersData.getUsers().values()) {
            if(user.getUserName().equals(name) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    public default User getUser(int id){
        return UsersData.getUsers().get(id);
    }
    public default void showAllUsers(){
        for(User user: UsersData.getUsers().values()){
            if(user instanceof Admin)
                System.out.println("Admin: "+user.getUserName()+" Id: "+user.getId());
            else
                System.out.println("User: "+user.getUserName()+" Id: "+user.getId());
        }
        if(UsersData.getUsers().isEmpty()){
            System.out.println("No users created yet");
        }
        System.out.println("");
    }
    public static Admin findAdmin(String name, String password) {
        for(User user: UsersData.getUsers().values()) {
            if(user.getUserName().equals(name) && user.getPassword().equals(password) && user instanceof Admin) {
                return (Admin) user;
            }
        }
        return null;
    }
    public static Customer findCustomer(String name, String password) {
        for(User user: UsersData.getUsers().values()) {
            if(user.getUserName().equals(name) && user.getPassword().equals(password) && user instanceof Customer) {
                return (Customer) user;
            }
        }
        return null;
    }
}
