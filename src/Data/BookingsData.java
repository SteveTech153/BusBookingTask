package Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import System.*;

public class BookingsData {
    static int numberOfBookings = 0;
    static List<Booking> bookings = new ArrayList<>();
    static HashMap<Integer, Booking> bookingMap = new HashMap<>();
    static List<Booking> waitingList = new ArrayList<>();
    public static int getNumberOfBookings() {
        return numberOfBookings;
    }
    public static void setNumberOfBookings(int numberOfBookings) {
        BookingsData.numberOfBookings = numberOfBookings;
    }
    public static void incrementNumberOfBookings() {
        BookingsData.numberOfBookings++;
    }
    public static List<Booking> getBookings() {
        return bookings;
    }
    public static void addBooking(Booking booking) {
        bookings.add(booking);
        bookingMap.put(booking.getId(), booking);
    }
    public static List<Booking> getWaitingList() {
        return waitingList;
    }
    public static void addWaitingList(Booking booking) {
        waitingList.add(booking);
    }
    public static void removeWaitingList(Booking booking) {
        waitingList.remove(booking);
    }
    public static HashMap<Integer, Booking> getBookingMap() {
        return bookingMap;
    }

}
