package Utility;

import java.time.LocalDate;

public class Validator {
    public boolean validateUsername(String username) {
        if (username.length() < 6 || username.length() > 15 || !Character.isAlphabetic(username.charAt(0)) || !(username.matches("[a-zA-Z0-9]+"))) {
            return false;
        }
        return true;
    }
    public boolean validatePassword(String password){
        return password.length() >= 6 && password.length() <= 15 &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[0-9].*");
    }

    public boolean validateCity(String city) {
        if (city.length() < 3 || city.length() > 15 || !Character.isAlphabetic(city.charAt(0)) || !(city.matches("[a-zA-Z]+"))) {
            return false;
        }
        return true;
    }

    public boolean validateNoOfSeats(String noOfSeats) {
        if (noOfSeats.length() < 1 || noOfSeats.length() > 3 || !noOfSeats.matches("[0-9]+") || Integer.parseInt(noOfSeats) < 10 || Integer.parseInt(noOfSeats) > 50) {
            return false;
        }
        return true;
    }
    public boolean validateInteger(String num){
        if(!num.matches("[0-9]+"))
            return false;
        return true;
    }
    public boolean validateDate(String date) throws Exception{
        LocalDate parsedDate = LocalDate.parse(date);
        if (LocalDate.now().compareTo(parsedDate) > 0) {
            return false;
        }
        return true;
    }
    public boolean validateCredit(String credit){
        if(validateInteger(credit) && Integer.parseInt(credit) >= 0 && Integer.parseInt(credit) <= 100)
            return true;
        return false;
    }
}
