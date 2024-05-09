package System;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User implements BookingManager {
    private int credit=0;
    private List<Booking> bookings = new ArrayList<>();
    public Customer(String name, String password) {
        super(name, password);
    }
    public boolean viewBookings() {
        bookings.forEach(Booking::showBooking);
        if(bookings.isEmpty()){
            System.out.println("No bookings");
            return false;
        }
        System.out.println("");
        return true;
    }

    public String getUserName() {
        return super.getUserName();
    }
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public Booking getBooking(int bookingId) {
        for(Booking booking: bookings) {
            if(booking.getId() == bookingId) {
                return booking;
            }
        }
        return null;
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }
    public void addCredit(int credit){
        this.credit+=credit;
    }
    public int getCredit(){
        return credit;
    }
    public void reduceCredit(int credit){
        this.credit-=credit;
    }
    public void setCredit(int credit){
        this.credit=credit;
    }
}
