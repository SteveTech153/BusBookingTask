package System;

import Data.BookingsData;
import Data.BusesData;
import Data.UsersData;

import java.util.*;

public class Booking {
    private int id;
    private int busId;
    private int userId;
    private String date;
    private String source;
    private String destination;
    private String startTime;
    private String endTime;
    private String day;
    private int noOfConfirmedSeats;
    private int noOfWaitingListSeats;
    static Scanner sc = new Scanner(System.in);

    public Booking(int userId, int busId, String date, int noOfConfirmedSeats, int noOfWaitingListSeats, String source, String destination, String startTime, String endTime){
        this.userId = userId;
        this.busId = busId;
        this.date = date;
        this.source = source;
        this.destination = destination;
        this.id = BookingsData.getNumberOfBookings()+1;
        BookingsData.incrementNumberOfBookings();
        this.noOfConfirmedSeats = noOfConfirmedSeats;
        this.noOfWaitingListSeats = noOfWaitingListSeats;
        this.startTime = startTime;
        this.endTime = endTime;
        BookingsData.getBookingMap().put(id, this);
    }
    public void showBooking() {
        System.out.println("Booking ID: " + id);
        System.out.println("User: " + UsersData.getUser(userId).getUserName());
        System.out.println("no Of Confirmed Seats: " + noOfConfirmedSeats);
        System.out.println("no Of Waiting List Seats: " + noOfWaitingListSeats);
        System.out.println("Source: " + source);
        System.out.println("Destination: " + destination);
        System.out.println("Bus: " + busId);
        System.out.println("Date: " + date);
        System.out.println("");
    }
    public void viewDetails(){
        showBooking();
    }

    @Override
    public String toString() {
        return "Booking ID: " + id + "\n" +
                "User: " + UsersData.getUser(userId).getUserName() + "\n" +
                "Bus: " + busId + "\n" +
                "Date: " + date + "\n";
    }
    public int getId() {
        return id;
    }
    public int getBusId() {
        return busId;
    }
    public String getSource() {
        return source;
    }
    public String getDestination() {
        return destination;
    }

    public Bus getBus() {
        return BusesData.getBus(busId);
    }

    public User getUser() {
        return UsersData.getUser(userId);
    }

    public String getDate() {
        return date;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public int getUserId() {
        return userId;
    }

    public void setDate(String newDate) {
        this.date = newDate;
    }
    public int getNoOfSeats(){
        return noOfConfirmedSeats + noOfWaitingListSeats;
    }
    public int getNoOfConfirmedSeats() {
        return noOfConfirmedSeats;
    }
    public void setNoOfConfirmedSeats(int i) {
        noOfConfirmedSeats = i;
    }
    public int getNoOfWaitingListSeats() {
        return noOfWaitingListSeats;
    }
    public void setNoOfWaitingListSeats(int i) {
        noOfWaitingListSeats = i;
    }

}
