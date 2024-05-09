package System;

public class Admin extends User implements UserManager, BusManager, BookingManager, ScheduleManager, CityManager, CreditsManager {
    private static final String password = "Admin@123";
    public Admin(String username, String password) throws IllegalAccessException {
        super(username, password);
        if(!password.equals(Admin.password)){
            throw new IllegalAccessException("Invalid password");
        }
    }
}
