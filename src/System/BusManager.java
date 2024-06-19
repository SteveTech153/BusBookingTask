package System;

import Data.BusesData;
import Data.CitiesData;
import Data.SchedulesData;
import Utility.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public interface BusManager {
    static Scanner sc = new Scanner(System.in);
    Validator validator = new Validator();

    public default void createBus(){
        System.out.println("Enter number of seats: ");
        String noOfSeatsString = sc.next();
        while(!validator.validateNoOfSeats(noOfSeatsString)){
            System.out.println("Number of seats should be greater than 0 and less than 50. Enter again");
            noOfSeatsString = sc.next();
        }
        int noOfSeats = Integer.parseInt(noOfSeatsString);
        while(noOfSeats<=0){
            System.out.println("Number of seats should be greater than 0 and less than 50. Enter again");
            noOfSeatsString = sc.next();
            if(!validator.validateNoOfSeats(noOfSeatsString))
                continue;
            noOfSeats = Integer.parseInt(noOfSeatsString);
        }

        System.out.println("Enter number of waiting list: ");
        String noOfWaitingListString = sc.next();
        while(!validator.validateInteger(noOfWaitingListString) || !(Integer.parseInt(noOfWaitingListString)>=0) || !(Integer.parseInt(noOfWaitingListString)<=noOfSeats)){
            System.out.println("Number of waiting list should be greater than or equal to 0 and should be less than or equal to total number of seats. Enter again: ");
            noOfWaitingListString = sc.next();
        }
        int noOfWaitingList = Integer.parseInt(noOfWaitingListString);
        int busId = createBus(noOfSeats, noOfWaitingList);
        System.out.print("Bus created successfully. BusId: "+ busId);
    }
    public default int createBus(int noOfSeats, int noOfWaitingList){
        Bus bus = new Bus(noOfSeats, noOfWaitingList);
        BusesData.getBusIdMap().put(bus.getId(), bus);
        return bus.getId();
    }
    public default boolean modifyBus(int busId, int noOfSeats, int noOfWaitingList){
        Bus bus = BusesData.getBusIdMap().get(busId);
        if(bus==null){
            System.out.println("Bus not found");
            return false;
        }
        bus.setTotalSeats(noOfSeats);
        bus.setTotalNumberOfWaitingList(noOfWaitingList);
        System.out.println("Bus modified successfully");
        return true;
    }

    public default boolean deleteBus(int busId){
        if(BusesData.getBusIdMap().containsKey(busId)){
            BusesData.getBusIdMap().remove(busId);
            System.out.println("Bus deleted successfully");
            return true;
        }
        else{
            System.out.println("Bus not found");
            return false;
        }
    }
    public default void showAllBuses(){
        if(BusesData.getBusIdMap().isEmpty()){
            System.out.println("No buses available.");
            return;
        }
        System.out.println("Available buses: ");
        for(int i=0; i<BusesData.getBusIdMap().size(); i++){
            Bus busTmp = BusesData.getBusIdMap().get(i+1);
            System.out.print((i+1) + "-> No of seats: "+busTmp.getTotalSeats()+" No of waiting list: "+busTmp.getTotalNumberOfWaitingList()+" Schedules: ");
            BusesData.getBusIdMap().get(i+1).showSchedule();
        }
        System.out.println("");
    }
    public default boolean addSchedule(Bus bus, String source, String destination, String startTime, String endTime, List<String> days){
        List<Schedule_SourceDestinationTimeDaysPair> schedules = bus.getSchedules();
        for(Schedule_SourceDestinationTimeDaysPair schedule: schedules){
            for(Object day: schedule.getDays()){
                if(days.contains((String)day)){
                    if(schedule.getStartTime().compareTo(schedule.getEndTime())<0) {
                        if ((schedule.getStartTime().compareTo(startTime) <= 0 && schedule.getEndTime().compareTo(startTime) >= 0) || (schedule.getStartTime().compareTo(endTime) <= 0 && schedule.getEndTime().compareTo(endTime) >= 0)) {
                            System.out.println("Time slot collides with another schedule. Cannot add schedule.");
                            return false;
                        }
                    }else{
                        String tmpEndTime = Integer.parseInt(schedule.getEndTime().split(":")[0])+24 + ":" + schedule.getEndTime().split(":")[1];
                        if( (schedule.getStartTime().compareTo(startTime)<=0 && tmpEndTime.compareTo(startTime)>=0) || (schedule.getStartTime().compareTo(endTime)<=0 && tmpEndTime.compareTo(endTime)>=0) || (schedule.getEndTime().compareTo(startTime) >= 0)){
                            System.out.println("Time slot collides with another schedule. Cannot add schedule.");
                            return false;
                        }
                    }
                }
            }
        }
        List<String> daysList = new ArrayList<>(Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"));
        List<String> prevDays = new ArrayList<>();
        for(String day: days){
            int prevIndex = daysList.indexOf(day)-1;
            if(prevIndex==-1){
                prevIndex = 6;
            }
            prevDays.add(daysList.get(prevIndex));
        }
        for(Schedule_SourceDestinationTimeDaysPair schedule: schedules){
            for(String prevDay: prevDays){
                if(schedule.getDays().contains(prevDay) && schedule.getStartTime().compareTo(schedule.getEndTime())>=0 && schedule.getEndTime().compareTo(startTime)>=0){
                    System.out.println("Time slot collides with another schedule. Cannot add schedule.");
                    return false;
                }
            }
        }

        schedules.add(new Schedule_SourceDestinationTimeDaysPair(source, destination, startTime, endTime, days));
        if(!SchedulesData.getAllSchedulesBusIdMap().containsKey(bus.getId())){
            SchedulesData.getAllSchedulesBusIdMap().put(bus.getId(), schedules);
        }
        SchedulesData.getAllSchedules().add(new Schedule_SourceDestinationTimeDaysPair(source, destination, startTime, endTime, days));
        return true;
    }

    public default void assignSourceDestinationTimeToBus(){
        System.out.println("Available buses: ");
        for(int i = 0; i< BusesData.getBusIdMap().size(); i++){
            Bus bus = BusesData.getBusIdMap().get(i+1);
            System.out.print((i+1) + "-> BusId: "+bus.getId()+" Number of seats: "+bus.getTotalSeats()+" Number of waiting list: "+bus.getTotalNumberOfWaitingList()+" Schedules: ");
            BusesData.getBusIdMap().get(i+1).showSchedule();
        }
        if(BusesData.getBusIdMap().isEmpty()){
            System.out.println("No buses available.");
            return;
        }
        System.out.println("");
        System.out.println("Enter BusId: ");
        String busIdString = sc.next();
        while(!busIdString.matches("[0-9]+")){
            System.out.println("Invalid choice. Enter again: ");
            busIdString = sc.next();
        }
        int busId = Integer.parseInt(busIdString);
        while(busId<=0 || busId>BusesData.getBusIdMap().size()){
            System.out.println("Invalid choice. Enter again: ");
            busIdString = sc.next();
            if(!busIdString.matches("[0-9]+"))
                continue;
            busId = Integer.parseInt(busIdString);
        }
        System.out.println("Enter source city: ");
        System.out.println("Available cities: ");
        for(int i = 0; i< CitiesData.getAvailableCities().size(); i++){
            System.out.println((i+1) + "-> " + CitiesData.getAvailableCities().get(i));
        }
        if(CitiesData.getAvailableCities().size()<2){
            System.out.println("Cities not enough to create a schedule");
            return;
        }
        String sourceIndexString = sc.next();
        while(!sourceIndexString.matches("[0-9]+")){
            System.out.println("Invalid source city Index. Enter again: ");
            sourceIndexString = sc.next();
        }
        int sourceIndex = Integer.parseInt(sourceIndexString);
        while(sourceIndex<=0 || sourceIndex>CitiesData.getAvailableCities().size()){
            System.out.println("Invalid source city Index. Enter again: ");
            sourceIndexString = sc.next();
            if(!sourceIndexString.matches("[0-9]+"))
                continue;
            sourceIndex = Integer.parseInt(sourceIndexString);
        }
        String source = CitiesData.getAvailableCities().get(sourceIndex-1);

        System.out.println("Enter destination city: ");

        String destinationIndexString = sc.next();
        while(!destinationIndexString.matches("[0-9]+")){
            System.out.println("Invalid destination city Index. Enter again: ");
            destinationIndexString = sc.next();
        }
        int destinationIndex = Integer.parseInt(destinationIndexString);
        while(destinationIndex<=0 || destinationIndex>CitiesData.getAvailableCities().size() || destinationIndex==sourceIndex){
            System.out.println("Invalid destination city Index. Enter again: ");
            destinationIndexString = sc.next();
            if(!destinationIndexString.matches("[0-9]+"))
                continue;
            destinationIndex = Integer.parseInt(destinationIndexString);
        }
        String destination = CitiesData.getAvailableCities().get(destinationIndex-1);

        System.out.println("Enter starting time(HH:MM): ");
        String startTime = sc.next();
        while(!startTime.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")){
            System.out.println("Invalid time slot. Enter again: ");
            startTime = sc.next();
        }
        System.out.println("Enter ending time(HH:MM): ");
        String endTime = sc.next();
        while(!endTime.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]") ){
            System.out.println("Invalid time slot. Enter again: ");
            endTime = sc.next();
        }

        System.out.println("Enter number of days the bus is available for this route in a week: ");
        String cntString = sc.next();
        while(!cntString.matches("[1-7]")){
            System.out.println("Invalid number of days. Enter again: ");
            cntString = sc.next();
        }
        int cnt = Integer.parseInt(cntString);
        List<String> days = new ArrayList<>();
        if(cnt==7){
            days.addAll(new ArrayList<>(Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")));
            if(assignSourceDestinationTimeToBus(source, destination, startTime, endTime, days, busId))
                System.out.println("Schedule added successfully");
            else
                System.out.println("Schedule addition failed");
            return;
        }
        for (int i = 0; i < cnt; i++) {
            System.out.println("Enter day " + (i + 1) + ": ");
            String day = sc.next();
            day = day.toLowerCase();
            while (!day.matches("monday|tuesday|wednesday|thursday|friday|saturday|sunday")) {
                System.out.println("Invalid day.(Type Monday or Tuesday or Wednesday or Thursday or Friday or Saturday or Sunday) Enter again: ");
                day = sc.next();
                day = day.toLowerCase();
            }
            days.add(day);
        }

        if(assignSourceDestinationTimeToBus(source, destination, startTime, endTime, days, busId))
            System.out.println("Schedule added successfully");
        else
            System.out.println("Schedule addition failed");
    }

    public default boolean assignSourceDestinationTimeToBus(String source, String destination, String startTime, String endTime, List<String> days, int busId){
        return this.addSchedule(BusesData.getBusIdMap().get(busId), source, destination, startTime, endTime, days);
    }


}
