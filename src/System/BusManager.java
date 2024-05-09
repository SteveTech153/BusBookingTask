package System;

import Data.BusesData;
import Utility.Validator;

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
        while(!(!validator.validateNoOfSeats(noOfWaitingListString) || !(Integer.parseInt(noOfWaitingListString)>0) || !(Integer.parseInt(noOfWaitingListString)<noOfSeats))){
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
            System.out.print((i+1) + "-> ");
            BusesData.getBusIdMap().get(i+1).showSchedule();
        }
        System.out.println("");
    }
}
