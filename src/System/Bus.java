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
    public void addSchedule(String source, String destination, String startTime, String endTime, List<String> days){
        for(Schedule_SourceDestinationTimeDaysPair schedule: schedules){
            for(Object day: schedule.getDays()){
                if(days.contains((String)day)){
                    if(schedule.getStartTime().compareTo(schedule.getEndTime())<0) {
                        if ((schedule.getStartTime().compareTo(startTime) <= 0 && schedule.getEndTime().compareTo(startTime) > 0) || (schedule.getStartTime().compareTo(endTime) < 0 && schedule.getEndTime().compareTo(endTime) > 0)) {
                            System.out.println("Time slot collides with another schedule. Cannot add schedule.");
                            return;
                        }
                    }else{
                        String tmpEndTime = Integer.parseInt(schedule.getEndTime().split(":")[0])+24 + ":" + schedule.getEndTime().split(":")[1];
                        if( (schedule.getStartTime().compareTo(startTime)<=0 && tmpEndTime.compareTo(startTime)>0) || (schedule.getStartTime().compareTo(endTime)<0 && tmpEndTime.compareTo(endTime)>0) || (schedule.getEndTime().compareTo(startTime) > 0)){
                            System.out.println("Time slot collides with another schedule. Cannot add schedule.");
                            return;
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
                if(schedule.getDays().contains(prevDay) && schedule.getStartTime().compareTo(schedule.getEndTime())>0 && schedule.getEndTime().compareTo(startTime)>0){
                    System.out.println("Time slot collides with another schedule. Cannot add schedule.");
                    return;
                }
            }
        }

        schedules.add(new Schedule_SourceDestinationTimeDaysPair(source, destination, startTime, endTime, days));
        if(!SchedulesData.getAllSchedulesBusIdMap().containsKey(id)){
            SchedulesData.getAllSchedulesBusIdMap().put(id, schedules);
        }
        SchedulesData.getAllSchedules().add(new Schedule_SourceDestinationTimeDaysPair(source, destination, startTime, endTime, days));
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
}
