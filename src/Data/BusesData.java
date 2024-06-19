package Data;

import System.Bus;

import java.util.HashMap;

public class BusesData {
    private static int defaultSeats = 20;
    private static int defaultWaitingList = 5;
    private static int noOfBuses = 0;
    private static HashMap<Integer, Bus> busIdMap = new HashMap<>();

    public static int getDefaultSeats() {
        return defaultSeats;
    }
    public static void setDefaultSeats(int defaultSeats) {
        BusesData.defaultSeats = defaultSeats;
    }
    public static int getDefaultWaitingList() {
        return defaultWaitingList;
    }
    public static void setDefaultWaitingList(int defaultWaitingList) {
        BusesData.defaultWaitingList = defaultWaitingList;
    }
    public static int getNoOfBuses() {
        return noOfBuses;
    }
    public static void setNoOfBuses(int noOfBuses) {
        BusesData.noOfBuses = noOfBuses;
    }
    public static void incrementNoOfBuses() {
        BusesData.noOfBuses++;
    }
    public static HashMap<Integer, Bus> getBusIdMap() {
        return busIdMap;
    }
    public static void addBus(Bus bus) {
        busIdMap.put(bus.getId(), bus);
    }
    public static Bus getBus(int id) {
        return busIdMap.get(id);
    }

}
