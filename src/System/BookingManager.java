package System;
import Data.*;
import Utility.Validator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public interface BookingManager {
    Scanner sc = new Scanner(System.in);
    Validator validator = new Validator();

    public default boolean book(int userId, boolean changeDate) {
        System.out.println("Available cities : ");
        for(int i = 0; i< CitiesData.getAvailableCities().size(); i++) {
            System.out.println((i+1)+" -> "+CitiesData.getAvailableCities().get(i));
        }
        if(CitiesData.getAvailableCities().isEmpty()){
            System.out.println("No cities available.\n");
            return false;
        }
        System.out.println("Enter the source city number : ");
        String sourceIndexString = sc.next();
        while(!validator.validateInteger(sourceIndexString) || Integer.parseInt(sourceIndexString)<=0 || Integer.parseInt(sourceIndexString)>CitiesData.getAvailableCities().size()){
            System.out.println("Invalid source city Index. Enter again: ");
            sourceIndexString = sc.next();
        }
        int sourceIndex = Integer.parseInt(sourceIndexString);
        String source = CitiesData.getAvailableCities().get(sourceIndex-1);
        System.out.println("Enter the destination city number : ");
        String destinationIndexString = sc.next();
        while(!validator.validateInteger(destinationIndexString) || Integer.parseInt(destinationIndexString)<=0 || Integer.parseInt(destinationIndexString)>CitiesData.getAvailableCities().size() || Integer.parseInt(destinationIndexString)==sourceIndex){
            System.out.println("Invalid destination city Index. Enter again: ");
            destinationIndexString = sc.next();
        }
        int destinationIndex = Integer.parseInt(destinationIndexString);
        String destination = CitiesData.getAvailableCities().get(destinationIndex-1);

        System.out.println("Enter the date(yyyy-mm-dd) : ");
        String date = sc.next();
        while(true){
            try {
                if(!validator.validateDate(date)){
                    System.out.println("Invalid date. Enter again: ");
                    date = sc.next();
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Enter again: ");
                date = sc.next();
            }
        }

        String day = LocalDate.parse(date).getDayOfWeek().toString();
        HashMap<Integer, List<Schedule_SourceDestinationTimeDaysPair>> allSchedulesBusIdMap = SchedulesData.getAllSchedulesBusIdMap();
        HashMap<Integer, Schedule_SourceDestinationTimeDaysPair> matchedSchedules = new HashMap<>();
        for(Map.Entry<Integer, List<Schedule_SourceDestinationTimeDaysPair>> entry: allSchedulesBusIdMap.entrySet()){
            for(Schedule_SourceDestinationTimeDaysPair schedule: entry.getValue()){
                if(schedule.getSource().equals(source) && schedule.getDestination().equals(destination) && schedule.getDays().contains(day.toLowerCase()) && (date.compareTo(LocalDate.now().toString())>0 || (date.compareTo(LocalDate.now().toString())==0 && schedule.getStartTime().compareTo(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))>0))){
                    matchedSchedules.put(entry.getKey(), schedule);
                    System.out.println("BusId: "+entry.getKey()+" Source: "+source+" Destination: "+destination+" Time: "+schedule.getStartTime()+"-"+schedule.getEndTime()+" Days: "+schedule.getDays()+ "seats available: "+ BusesData.getBus(entry.getKey()).getAvailableSeats(new DateSchedulePair<>(date, schedule)));
                }
            }
        }

        if(matchedSchedules.isEmpty()){
            System.out.println("No buses available for this route on this day\n");
            return false;
        }
        System.out.println("Enter the busId you want to book : ");
        String busIdString = sc.next();
        while(!validator.validateInteger(busIdString) || !matchedSchedules.containsKey(Integer.parseInt(busIdString))){
            System.out.println("Invalid busId. Enter again: ");
            busIdString = sc.next();
        }
        int busId = Integer.parseInt(busIdString);
        System.out.println("pick a starting time(HH:MM): ");
        String startTime = sc.next();
        while(!startTime.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]") || matchedSchedules.get(busId).getStartTime().compareTo(startTime)!=0){
            System.out.println("Invalid time slot. Enter again: ");
            startTime = sc.next();
        }

        System.out.println("Enter number of seats you want to book : ");
        String noOfSeatsString = sc.next();
        while(!validator.validateInteger(noOfSeatsString) || Integer.parseInt(noOfSeatsString)<=0){
            System.out.println("Invalid number of seats");
            noOfSeatsString = sc.next();
        }
        int noOfSeats = Integer.parseInt(noOfSeatsString);
        return book(userId, busId, source, destination, date, noOfSeats, startTime, matchedSchedules.get(busId), changeDate);
    }
    public default boolean book(int userId, int busId, String source, String destination, String date, int noOfSeats, String startTime, Schedule_SourceDestinationTimeDaysPair schedule, boolean changeDate) {
        Bus bus = BusesData.getBus(busId);
        DateSchedulePair dateSchedulePair = new DateSchedulePair<>(date, schedule);
        if(bus.getTotalAvailableSeats(dateSchedulePair) < noOfSeats ) {
            System.out.println("Requested number of seats cannot be booked. Available seats : "+bus.getAvailableSeats(dateSchedulePair)+". Available waiting list: "+ bus.getAvailableSeatsForWaitingList(dateSchedulePair)+"\n");
            return false;
        }else if(bus.getAvailableSeats(dateSchedulePair) >= noOfSeats ) {
            Booking booking = new Booking(userId, busId, date, noOfSeats, 0, source, destination, startTime, schedule.getEndTime());
            BookingsData.getBookings().add(booking);
            bus.setBookedSeats(dateSchedulePair, bus.getBookedSeats(dateSchedulePair)+noOfSeats);
            // bookedMap.put(new BusDatePair<String,String,String>(source, destination, date), bus);
            Customer customer = (Customer)UsersData.getUser(userId);
            customer.addBooking(booking);
            if(customer.getCredit()>=noOfSeats*100) {
                customer.reduceCredit(noOfSeats * 100);
                if(!changeDate){
                    System.out.println("You have to pay 0%");
                }
            }else{
                if(!changeDate)
                    System.out.println("You have to pay "+((100 - customer.getCredit()/(noOfSeats*100)))+"%");
                customer.setCredit(0);
            }
            if(!changeDate) {
                System.out.println("Booking successful");
                System.out.println("All seats are confirmed\n");
                System.out.println("Your Booking Id is : " + booking.getId() + "\n");
            }
            return true;
        }else{
            int noOfSeatsCopy = noOfSeats, remainingSeats = noOfSeats-bus.getAvailableSeats(dateSchedulePair);
            Booking booking = new Booking(userId, busId, date, bus.getAvailableSeats(dateSchedulePair), remainingSeats, source, destination, startTime, schedule.getEndTime());
            BookingsData.getBookings().add(booking);
            BookingsData.getWaitingList().add(booking);
            bus.setBookedSeats(dateSchedulePair, bus.getBookedSeats(dateSchedulePair)+bus.getAvailableSeats(dateSchedulePair));
            bus.setNumberOfWaitingList(dateSchedulePair,bus.getNumberOfWaitingList(dateSchedulePair)+remainingSeats);
            //bookedMap.put(new BusDatePair<String,String,String>(source, destination, date), bus);
            Customer customer = (Customer)UsersData.getUser(userId);
            customer.addBooking(booking);
            if(customer.getCredit()>=noOfSeats*100) {
                customer.reduceCredit(noOfSeats * 100);
                if(!changeDate)
                    System.out.println("You have to pay 0%");
            }else{
                if(!changeDate)
                    System.out.println("You have to pay "+((100 - customer.getCredit()/(noOfSeats*100)))+"%");
                customer.setCredit(0);
            }
            if(!changeDate) {
                System.out.println("Booking successful for " + (noOfSeatsCopy - remainingSeats) + " seats and you've been added to the waiting list for " + remainingSeats + " seats");
                System.out.println("Your booking Id is: " + booking.getId() + "\n");
            }
            return true;
        }
    }
    public default void cancelBooking(Integer userId, boolean changeDate) {
        System.out .println("Your bookings: ");
        Customer customer = (Customer) UsersData.getUser(userId);
        if(!customer.viewBookings())
            return;
        System.out.println("Enter booking id to cancel: ");
        String bookingIdString = sc.next();
        while(!validator.validateInteger(bookingIdString) || Integer.parseInt(bookingIdString)<=0){
            System.out.println("Invalid booking id. Enter again: ");
            bookingIdString = sc.next();
        }
        int bookingId = Integer.parseInt(bookingIdString);
        Booking booking = customer.getBooking(bookingId);
        while(booking==null){
            System.out.println("Invalid booking id. Enter again: ");
            bookingIdString = sc.next();
            if(!validator.validateInteger(bookingIdString))
                continue;
            bookingId = Integer.parseInt(bookingIdString);
            booking = customer.getBooking(bookingId);
        }
        if(booking.getDate().compareTo(LocalDate.now().toString())<0){
            System.out.println("Cannot cancel booking");
            return;
        }

        String numberOfSeatsToCancelString = "1";
        int numberOfSeatsToCancel = 1;
        if(booking.getNoOfSeats() > 1){
            System.out.println("Enter number of seats to cancel: ");
            numberOfSeatsToCancelString = sc.next();
            while(!validator.validateInteger(numberOfSeatsToCancelString)){
                System.out.println("Invalid number of seats. Enter again: ");
                numberOfSeatsToCancelString = sc.next();
            }
            numberOfSeatsToCancel = Integer.parseInt(numberOfSeatsToCancelString);
            while(numberOfSeatsToCancel<=0 || numberOfSeatsToCancel>booking.getNoOfSeats()){
                System.out.println("Invalid number of seats. Enter again: ");
                numberOfSeatsToCancelString = sc.next();
                if (!validator.validateInteger(numberOfSeatsToCancelString))
                    continue;
                numberOfSeatsToCancel = Integer.parseInt(numberOfSeatsToCancelString);
            }
        }
        cancelBooking(userId, bookingId, numberOfSeatsToCancel, changeDate);

    }
    public default void cancelBooking(Integer userId, int bookingId, int numberOfSeatsToCancel, boolean changeDate) {
        Customer customer = (Customer) UsersData.getUser(userId);
        Booking booking = customer.getBooking(bookingId);
        if(numberOfSeatsToCancel <= booking.getNoOfWaitingListSeats()){
            Bus bus = BusesData.getBus(booking.getBusId());
            Schedule_SourceDestinationTimeDaysPair schedule =  bus.getSchedule(booking.getStartTime(), booking.getDate());
            DateSchedulePair dateSchedulePair = new DateSchedulePair<>(booking.getDate(), schedule);
            bus.setNumberOfWaitingList(dateSchedulePair, bus.getNumberOfWaitingList(dateSchedulePair)-numberOfSeatsToCancel);
            booking.setNoOfWaitingListSeats(booking.getNoOfWaitingListSeats()-numberOfSeatsToCancel);
            if(booking.getNoOfWaitingListSeats()==0){
                BookingsData.getWaitingList().remove(booking);
            }
            if(booking.getNoOfWaitingListSeats()==0 && booking.getNoOfConfirmedSeats()==0){
                BookingsData.getBookings().remove(booking);
                customer.removeBooking(booking);
            }
            if(!changeDate)
                System.out.println("Booking cancelled successfully");
            if(booking.getDate().compareTo(LocalDate.now().toString())==0){
                customer.addCredit(CreditsData.getCreditForCancellationOnSameDay()*numberOfSeatsToCancel);
            }else{
                customer.addCredit(CreditsData.getCreditForCancellationBefore()*numberOfSeatsToCancel);
            }
            System.out.println("");
            return;
        }else{
            int remainingSeats = numberOfSeatsToCancel - booking.getNoOfWaitingListSeats();
            if(booking.getNoOfWaitingListSeats()>0){
                booking.setNoOfWaitingListSeats(0);
                BookingsData.getWaitingList().remove(booking);
            }
            booking.setNoOfConfirmedSeats(booking.getNoOfConfirmedSeats()-remainingSeats);
            if(booking.getNoOfConfirmedSeats()==0){
                BookingsData.getBookings().remove(booking);
                customer.removeBooking(booking);
            }
            Bus bus = BusesData.getBus(booking.getBusId());
            Schedule_SourceDestinationTimeDaysPair schedule = bus.getSchedule(booking.getStartTime(), booking.getDate());
            DateSchedulePair dateSchedulePair = new DateSchedulePair<>(booking.getDate(), schedule);
            bus.setBookedSeats(dateSchedulePair, bus.getBookedSeats(dateSchedulePair)-remainingSeats);
            bus.setNumberOfWaitingList(dateSchedulePair, bus.getNumberOfWaitingList(dateSchedulePair)-remainingSeats);
            if(!changeDate)
                System.out.println("Booking cancelled successfully");
            if(booking.getDate().compareTo(LocalDate.now().toString())==0){
                customer.addCredit(CreditsData.getCreditForCancellationOnSameDay()*numberOfSeatsToCancel);
                if(!changeDate)
                    System.out.println("Credit added: "+CreditsData.getCreditForCancellationOnSameDay()*numberOfSeatsToCancel);
            }else{
                customer.addCredit(CreditsData.getCreditForCancellationBefore()*numberOfSeatsToCancel);
                if(!changeDate)
                    System.out.println("Credit added: "+CreditsData.getCreditForCancellationBefore()*numberOfSeatsToCancel);
            }
            System.out.println("");
            if(BookingsData.getWaitingList().size()>0){
                List<Booking> bookingsToRemove = new ArrayList<>();
                Iterator<Booking> iterator = BookingsData.getWaitingList().iterator();
                while (iterator.hasNext()) {
                    Booking waitingBooking = iterator.next();
                    Bus waitingbBus = BusesData.getBus(waitingBooking.getBusId());
                    if(BusesData.getBus(waitingBooking.getBusId()).getSchedule(waitingBooking.getStartTime(), waitingBooking.getDate()).equals(schedule)){
                        if(bus.getAvailableSeats(dateSchedulePair)>0){
                            int seatsToConfirm = Math.min(waitingBooking.getNoOfWaitingListSeats(), bus.getAvailableSeats(dateSchedulePair));
                            waitingBooking.setNoOfWaitingListSeats(waitingBooking.getNoOfWaitingListSeats()-seatsToConfirm);
                            waitingBooking.setNoOfConfirmedSeats(waitingBooking.getNoOfConfirmedSeats()+seatsToConfirm);
                            bus.setBookedSeats(dateSchedulePair, bus.getBookedSeats(dateSchedulePair)+seatsToConfirm);
                            bus.setNumberOfWaitingList(dateSchedulePair, bus.getNumberOfWaitingList(dateSchedulePair)-seatsToConfirm);
                            if(waitingBooking.getNoOfWaitingListSeats()==0){
                                bookingsToRemove.add(waitingBooking);
                            }
                            if(waitingBooking.getNoOfWaitingListSeats()==0 && waitingBooking.getNoOfConfirmedSeats()==0){
                                bookingsToRemove.add(waitingBooking);
                            }
                            if(bus.getAvailableSeats(dateSchedulePair)==0){
                                break;
                            }
                        }
                    }
                }
                BookingsData.getWaitingList().removeAll(bookingsToRemove);
            }
        }
    }

    public default void requestChangeDate(Integer userId){
        System.out.println("Your bookings: ");
        Customer user = (Customer) UsersData.getUser(userId);
        if(!user.viewBookings())
            return;
        System.out.println("Enter booking id to change date: ");
        String bookingIdString = sc.next();
        while(!validator.validateInteger(bookingIdString) || Integer.parseInt(bookingIdString)<=0){
            System.out.println("Invalid booking id. Enter again: ");
            bookingIdString = sc.next();
        }
        int bookingId = Integer.parseInt(bookingIdString);
        Booking booking = user.getBooking(bookingId);
        while(booking==null){
            System.out.println("Invalid booking id. Enter again: ");
            bookingIdString = sc.next();
            if(!validator.validateInteger(bookingIdString))
                continue;
            bookingId = Integer.parseInt(bookingIdString);
            booking = user.getBooking(bookingId);
        }
        System.out.println("Enter new date(yyyy-mm-dd): ");
        String newDate = sc.next();
        while(true){
            try {
                LocalDate parsedDate = LocalDate.parse(newDate);
                if(LocalDate.now().compareTo(parsedDate)>0 || newDate.compareTo(booking.getDate())<=0){
                    System.out.println("Invalid date. Enter again: ");
                    newDate = sc.next();
                } else {
                    break;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Enter again: ");
                newDate = sc.next();
            }
        }
        if(book(userId, booking.getBus().getId(), booking.getSource(), booking.getDestination(), newDate, booking.getNoOfSeats(), booking.getStartTime(), booking.getBus().getSchedule(booking.getStartTime(), newDate), true)){
            cancelBooking(userId, bookingId, booking.getNoOfSeats(), true);
            System.out.println("Date changed successfully");
        }else{
            System.out.println("Date change failed");
        }
    }
    public default void showAllBookings(){
        for(Booking booking: BookingsData.getBookings()){
            booking.showBooking();
        }
        if(BookingsData.getBookings().isEmpty()){
            System.out.println("No bookings");
        }
    }
}
