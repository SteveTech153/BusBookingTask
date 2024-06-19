package System;

import Data.BusesData;
import Data.SchedulesData;
import java.time.LocalDate;
import java.util.*;

public class Bus {
    private int id=0;
    private HashMap<DateSchedulePair, Integer> bookedSeats = new HashMap<>();
    private HashMap<DateSchedulePair, Integer> numberOfWaitingList = new HashMap<>();
    private int totalSeats = 20;
    private int totalNumberOfWaitingList = 5;
    private List<Schedule_SourceDestinationTimeDaysPair> schedules = new ArrayList<>();

    public Bus(int noOfSeats, int noOfWaitingList) {
        this.totalSeats = noOfSeats;
        this.totalNumberOfWaitingList = noOfWaitingList;
        this.id = BusesData.getNoOfBuses()+1;
        BusesData.incrementNoOfBuses();
        BusesData.getBusIdMap().put(id, this);
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats(DateSchedulePair dateSchedulePair) {
        return totalSeats - bookedSeats.getOrDefault(dateSchedulePair, 0);

    }

    public int getTotalAvailableSeats(DateSchedulePair dateSchedulePair) {
        return totalSeats - bookedSeats.getOrDefault(dateSchedulePair, 0) + totalNumberOfWaitingList - numberOfWaitingList.getOrDefault(dateSchedulePair, 0);
    }

    public void setTotalSeats(int i) {
        totalSeats = i;
    }

    public void setBookedSeats(DateSchedulePair dateSchedulePair, int i) {
        bookedSeats.put(dateSchedulePair, i);
    }

    public boolean isWaitingListAvailable(DateSchedulePair dateSchedulePair) {
        return totalNumberOfWaitingList - numberOfWaitingList.getOrDefault(dateSchedulePair, 0) > 0;
    }

    public void showSchedule(){
        for(int i=0; i<schedules.size(); i++){
            System.out.println("Source: "+schedules.get(i).getSource()+" Destination: "+schedules.get(i).getDestination()+" Time: "+schedules.get(i).getStartTime()+"-"+schedules.get(i).getEndTime()+" Days: "+schedules.get(i).getDays());
        }
    }

    public int getId() {
        return this.id;
    }

    public void setTotalNumberOfWaitingList(int i) {
        totalNumberOfWaitingList = i;
    }
    public int getTotalNumberOfWaitingList() {
        return totalNumberOfWaitingList;
    }
    public void setNumberOfWaitingList(DateSchedulePair dateSchedulePair, int i) {
        numberOfWaitingList.put(dateSchedulePair, i);
    }

    public int getNumberOfWaitingList(DateSchedulePair dateSchedulePair) {
        return numberOfWaitingList.getOrDefault(dateSchedulePair, 0);
    }
    public int getAvailableSeatsForWaitingList(DateSchedulePair dateSchedulePair) {
        return totalNumberOfWaitingList - numberOfWaitingList.getOrDefault(dateSchedulePair, 0);
    }

    public int getBookedSeats(DateSchedulePair dateSchedulePair) {
        return bookedSeats.getOrDefault(dateSchedulePair, 0);
    }

    public List<Schedule_SourceDestinationTimeDaysPair> getSchedules() {
        return schedules;
    }
    public Schedule_SourceDestinationTimeDaysPair getSchedule(String startTime, String date){
        LocalDate localDate = LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String day = localDate.getDayOfWeek().toString();
        for(Schedule_SourceDestinationTimeDaysPair schedule: schedules){
            if(schedule.getStartTime().equals(startTime) && schedule.getDays().contains(day.toLowerCase())){
                return schedule;
            }
        }
        return null;
    }
}
