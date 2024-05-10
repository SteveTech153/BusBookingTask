package System;

import Data.BusesData;
import Data.CitiesData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public interface ScheduleManager {
    Scanner sc = new Scanner(System.in);
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
        return BusesData.getBusIdMap().get(busId).addSchedule(source, destination, startTime, endTime, days);
    }
}
