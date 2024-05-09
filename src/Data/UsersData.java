package Data;

import java.util.HashMap;
import System.*;

public class UsersData {
    static int numberOfUsers = 0;
    static HashMap<Integer, User> users = new HashMap<>();
    public static int getNumberOfUsers() {
        return numberOfUsers;
    }
    public static void setNumberOfUsers(int numberOfUsers) {
        UsersData.numberOfUsers = numberOfUsers;
    }
    public static void incrementNumberOfUsers() {
        UsersData.numberOfUsers++;
    }
    public static HashMap<Integer, User> getUsers() {
        return users;
    }
    public static void addUser(User user) {
        users.put(user.getId(), user);
    }
    public static User getUser(int id) {
        return users.get(id);
    }

}
