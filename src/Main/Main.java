package Main;
import Data.*;
import System.*;
import Utility.*;
import java.util.*;

public class Main {

    static{
        Admin admin = null;
        try {
            admin = new Admin("admin1", "Admin@123");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        admin.setCities(new ArrayList<>(Arrays.asList("chennai", "madurai", "trichy", "coimbatore", "salem", "tirunelveli")));
        admin.createBus(20, 5);
        admin.createBus(30, 10);
        admin.createBus(40, 10);
        admin.createBus(25, 10);
        admin.assignSourceDestinationTimeToBus("chennai", "madurai", "06:00", "12:00", new ArrayList<>(Arrays.asList("monday", "wednesday", "friday")), 1);
        admin.assignSourceDestinationTimeToBus("madurai", "chennai", "06:00", "12:00", new ArrayList<>(Arrays.asList("tuesday", "thursday", "saturday")), 1);
        admin.assignSourceDestinationTimeToBus("madurai", "trichy", "14:00", "21:00", new ArrayList<>(Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")), 2);
        admin.assignSourceDestinationTimeToBus("trichy", "madurai", "12:00", "17:00", new ArrayList<>(Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")), 3);
        admin.assignSourceDestinationTimeToBus("chennai", "coimbatore", "06:00", "17:00", new ArrayList<>(Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")), 4);
        Customer customer = new Customer("customer1", "Customer@123");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n_________________Welcome to the Bus Booking System_________________");
        String choice = "1";
        while (!choice .equals("0")) {
            System.out.println("\n_____________________________");
            System.out.println("Press 1 to login as Admin");
            System.out.println("Press 2 to register as Admin");
            System.out.println("Press 3 to login as Customer");
            System.out.println("Press 4 to register as Customer");
            System.out.println("Press 0 to exit");
            choice = scanner.next();
            Validator validator = new Validator();
            switch (choice) {
                case "0":
                    break;
                case "1":
                    System.out.println("----Login as Admin----");
                    Admin admin = null;
                    while(admin==null) {
                        System.out.println("Enter username: ");
                        String userName = scanner.next();
                        System.out.println("Enter password: ");
                        String password = scanner.next();
                        admin = UserManager.findAdmin(userName, password);
                        if(admin==null){
                            System.out.println("Invalid username or password");
                            System.out.println("Press 0 to exit");
                            System.out.println("Press any other digit or letter or symbol to try again");
                            String choiceTmp = scanner.next();
                            if(choiceTmp.equals("0")){
                                break;
                            }
                        }
                    }
                    if(admin==null){
                        break;
                    }else{
                        System.out.println("Login successful\n");
                    }
                    String adminChoice = "1";
                    while (!adminChoice.equals("0")) {
                        System.out.println("___Admin Menu___");
                        System.out.println("1 to add city");
                        System.out.println("2 to remove city");
                        System.out.println("3 to create bus");
                        System.out.println("4 to assign source destination time to bus");
                        System.out.println("5 to delete bus");
                        System.out.println("6 to view buses");
                        System.out.println("7 to modify bus");
                        System.out.println("8 to set the credits for cancellation");
                        System.out.println("9 to show All users");
                        System.out.println("10 to show all bookings");
                        System.out.println("0 to exit");
                        adminChoice = scanner.next();
                        switch (adminChoice) {
                            case "0":
                                break;
                            case "1":
                                boolean invalidCity = true;
                                while(invalidCity) {
                                    System.out.println("Enter city: ");
                                    String city = scanner.next();
                                    if (!validator.validateCity(city)) {
                                        System.out.println("Invalid city. City should contain only alphabets and should be between length 3 and 15");
                                        System.out.println("Press 0 to exit");
                                        System.out.println("Press any other digit or letter or symbol to try again");
                                        String choiceTmp = scanner.next();
                                        if (choiceTmp.equals("0")) {
                                            break;
                                        }else {
                                            continue;
                                        }
                                    }
                                    invalidCity = !admin.addCity(city.toLowerCase());
                                    if(invalidCity){
                                        System.out.println("Press 0 to exit");
                                        System.out.println("Press any other digit or letter or symbol to try again");
                                        String choiceTmp = scanner.next();
                                        if (choiceTmp.equals("0")) {
                                            break;
                                        }
                                    }
                                }
                                System.out.println("");
                                break;
                            case "2":
                                boolean cityRemoved = false;
                                while (!cityRemoved) {
                                    if (CitiesData.getAvailableCities().isEmpty()) {
                                        System.out.println("No cities available");
                                        break;
                                    }
                                    System.out.println("Available cities: ");
                                    for (int i = 0; i < CitiesData.getAvailableCities().size(); i++) {
                                        System.out.println(CitiesData.getAvailableCities().get(i));
                                    }
                                    if(CitiesData.getAvailableCities().isEmpty())
                                    {
                                        System.out.println("No cities available");
                                        break;
                                    }
                                    System.out.println("Enter city: ");
                                    String city1 = scanner.next();
                                    cityRemoved = admin.removeCity(city1.toLowerCase());
                                    if (!cityRemoved) {
                                        System.out.println("Press 0 to exit");
                                        System.out.println("Press any other digit or letter or symbol to try again");
                                        String choiceTmp = scanner.next();
                                        if (choiceTmp.equals("0")) {
                                            break;
                                        }
                                    }
                                }
                                break;
                            case "3":
                                admin.createBus();
                                System.out.println("");
                                break;
                            case "4":
                                admin.assignSourceDestinationTimeToBus();
                                System.out.println("");
                                break;
                            case "5":
                                boolean busDeleted = false;
                                while(!busDeleted) {
                                    if (BusesData.getNoOfBuses() == 0) {
                                        System.out.println("No buses to delete");
                                        break;
                                    }
                                    admin.showAllBuses();
                                    System.out.println("enter bus id to delete: ");
                                    String busIdString = scanner.next();
                                    while (!validator.validateInteger(busIdString)) {
                                        System.out.println("Invalid choice. Enter again: ");
                                        busIdString = scanner.next();
                                    }
                                    int busId = Integer.parseInt(busIdString);
                                    busDeleted = admin.deleteBus(busId);
                                    if (!busDeleted) {
                                        System.out.println("Press 0 to exit");
                                        System.out.println("Press any other digit or letter or symbol to try again");
                                        String choiceTmp = scanner.next();
                                        if (choiceTmp.equals("0")) {
                                            break;
                                        }
                                    }
                                }
                                System.out.println("");
                                break;
                            case "6":
                                admin.showAllBuses();
                                System.out.println("");
                                break;
                            case "7":
                                boolean busModified = false;
                                while(!busModified) {
                                    if (BusesData.getNoOfBuses() == 0) {
                                        System.out.println("No buses to modify");
                                        break;
                                    }
                                    admin.showAllBuses();
                                    System.out.println("Enter bus id to modify: ");
                                    String busId1String = scanner.next();
                                    while (!validator.validateInteger(busId1String)) {
                                        System.out.println("Invalid choice. Enter again: ");
                                        busId1String = scanner.next();
                                    }
                                    int busId1 = Integer.parseInt(busId1String);
                                    System.out.println("Enter new number of seats: ");
                                    String seatsString = scanner.next();
                                    while (!validator.validateNoOfSeats(seatsString)) {
                                        System.out.println("Invalid number of seats. Seats should be from 10 to 50 Enter again: ");
                                        seatsString = scanner.next();
                                    }
                                    int seats = Integer.parseInt(seatsString);
                                    System.out.println("Enter new number of waiting list: ");
                                    String waitingListString = scanner.next();
                                    while (!validator.validateInteger(waitingListString) || Integer.parseInt(waitingListString) > seats) {
                                        System.out.println("Invalid number of waiting list or waiting list is higher than number of seats. Enter again: ");
                                        waitingListString = scanner.next();
                                    }
                                    int waitingList = Integer.parseInt(waitingListString);
                                    busModified = admin.modifyBus(busId1, seats, waitingList);
                                    if (!busModified) {
                                        System.out.println("Press 0 to exit");
                                        System.out.println("Press any other digit or letter or symbol to try again");
                                        String choiceTmp = scanner.next();
                                        if (choiceTmp.equals("0")) {
                                            break;
                                        }
                                    }
                                }
                                System.out.println("");
                                break;
                            case "8":
                                System.out.println("Enter credit to set for cancellation on the day of journey: ");
                                String creditString = scanner.next();
                                while (!validator.validateCredit(creditString)) {
                                    System.out.println("Invalid credit. Credit should be lesser than 100 and greater than 0. Enter again: ");
                                    creditString = scanner.next();
                                }
                                int credit = Integer.parseInt(creditString);
                                admin.setCreditForCancellationOnSameDay(credit);
                                System.out.println("Enter credit to set for cancellation before the day of journey: ");
                                String credit1String = scanner.next();
                                while (!validator.validateCredit(credit1String)) {
                                    System.out.println("Invalid credit. Credit should be lesser than 100 and greater than 0. Enter again: ");
                                    credit1String = scanner.next();
                                }
                                int credit1 = Integer.parseInt(credit1String);
                                admin.setCreditForCancellationBefore(credit1);
                                System.out.println("");
                                break;
                            case "9":
                                admin.showAllUsers();
                                System.out.println("");
                                break;
                            case "10":
                                admin.showAllBookings();
                                System.out.println("");
                                break;
                            default:
                                System.out.println("Invalid choice");
                                System.out.println("");
                                break;
                        }
                    }
                    System.out.println("");
                    break;
                case "2":
                    System.out.println("----Register as Admin----");
                    boolean invalidCredentials = true;
                    while(invalidCredentials) {
                        System.out.println("Enter username: ");
                        String username = scanner.next();
                        if (!validator.validateUsername(username)) {
                            System.out.println("Invalid username. Username should start with alphabet, should contain only alphabets and numbers and should be between length 6 and 15");
                            System.out.println("Press 0 to exit");
                            System.out.println("Press any other digit or letter or symbol to try again");
                            String choiceTmp = scanner.next();
                            if (choiceTmp.equals("0")) {
                                break;
                            }else {
                                continue;
                            }
                        }
                        System.out.println("Enter Admin's password: ");
                        String password = scanner.next();
                        try {
                            Admin admin1 = new Admin(username, password);
                            System.out.println("Admin created successfully");
                        } catch (IllegalAccessException e) {
                            System.out.println("Invalid password");
                            System.out.println("Press 0 to exit");
                            System.out.println("Press any other digit or letter or symbol to try again");
                            String choiceTmp = scanner.next();
                            if (choiceTmp.equals("0")) {
                                break;
                            }else {
                                continue;
                            }
                        }
                        invalidCredentials = false;
                    }
                    System.out.println("");
                    break;
                case "3":
                    System.out.println("----Login as Customer----");
                    Customer customer = null;
                    while(customer==null){
                        System.out.println("Enter username: ");
                        String name = scanner.next();
                        System.out.println("Enter password: ");
                        String password = scanner.next();
                        customer = UserManager.findCustomer(name, password);
                        if(customer==null){
                            System.out.println("Invalid username or password");
                            System.out.println("Press 0 to exit");
                            System.out.println("Press any other digit or letter or symbol to try again");
                            String choiceTmp = scanner.next();
                            if(choiceTmp.equals("0")){
                                break;
                            }
                        }
                    }
                    if(customer==null){
                        break;
                    }else{
                        System.out.println("Login successful\n");
                    }
                    String userChoice = "1";
                    while (!userChoice.equals("0")) {
                        System.out.println("___User Menu___");
                        System.out.println("1 to show Available cities");
                        System.out.println("2 to book");
                        System.out.println("3 to cancel booking");
                        System.out.println("4 to request change date");
                        System.out.println("5 to view booking details and status");
                        System.out.println("6 view all bookings details and statuses");
                        System.out.println("0 to exit");
                        userChoice = scanner.next();
                        switch (userChoice) {
                            case "0":
                                break;
                            case "1":
                                CityManager.showAvailableCities();
                                System.out.println("");
                                break;
                            case "2":
                                boolean booked = customer.book(customer.getId(), false);
                                while(!booked){
                                    System.out.println("Press 0 to exit");
                                    System.out.println("Press any other digit or letter or symbol to try again");
                                    String choiceTmp = scanner.next();
                                    if(choiceTmp.equals("0")){
                                        break;
                                    }
                                    booked = customer.book(customer.getId(), false);
                                }
                                System.out.println("");
                                break;
                            case "3":
                                customer.cancelBooking(customer.getId(), false);
                                System.out.println("");
                                break;
                            case "4":
                                customer.requestChangeDate(customer.getId());
                                System.out.println("");
                                break;
                            case "5":
                                System.out.println("Enter booking id: ");
                                String bookingIdString = scanner.next();
                                while (!validator.validateInteger(bookingIdString)) {
                                    System.out.println("Invalid choice. Enter again: ");
                                    bookingIdString = scanner.next();
                                }
                                int bookingId = Integer.parseInt(bookingIdString);
                                Booking booking = customer.getBooking(bookingId);
                                if(booking!=null)
                                    customer.getBooking(bookingId).viewDetails();
                                else
                                    System.out.println("No booking exist");
                                System.out.println("");
                                break;
                            case "6":
                                customer.viewBookings();
                                System.out.println("");
                                break;
                            default:
                                System.out.println("Invalid choice");
                                System.out.println("");
                                break;
                        }
                    }
                    System.out.println("");
                    break;
                case "4":
                    System.out.println("----Register as Customer----");
                    boolean invalidCredentials1 = true;
                    while(invalidCredentials1) {
                        System.out.println("Enter username: ");
                        String username = scanner.next();
                        if (!validator.validateUsername(username)) {
                            System.out.println("Invalid username. Username should start with alphabet, should contain only alphabets and numbers and should be between length 6 and 15");
                            System.out.println("Press 0 to exit");
                            System.out.println("Press any other digit or letter or symbol to try again");
                            String choiceTmp = scanner.next();
                            if (choiceTmp.equals("0")) {
                                break;
                            }else {
                                continue;
                            }
                        }else if(UserManager.findUser(username, null)!=null){
                            System.out.println("Username already exists");
                            System.out.println("Press 0 to exit");
                            System.out.println("Press any other digit or letter or symbol to try again");
                            String choiceTmp = scanner.next();
                            if (choiceTmp.equals("0")) {
                                break;
                            }else {
                                continue;
                            }
                        }
                        System.out.println("Enter password: ");
                        String password = scanner.next();
                        if (!validator.validatePassword(password)) {
                            System.out.println("Invalid password. Password should contain at least one uppercase, one lowercase, one number and should be between length 6 and 15");
                            System.out.println("Press 0 to exit");
                            System.out.println("Press any other digit or letter or symbol to try again");
                            String choiceTmp = scanner.next();
                            if (choiceTmp.equals("0")) {
                                break;
                            }else {
                                continue;
                            }
                        }
                        Customer customer1 = new Customer(username, password);
                        invalidCredentials1 = false;
                        System.out.println("Registered successfully");
                    }
                    System.out.println("");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}