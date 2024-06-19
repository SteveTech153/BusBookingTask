package Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import System.*;

public class SchedulesData {
    private static int noOfSchedules = 0;
    private static HashMap<Integer, List<Schedule_SourceDestinationTimeDaysPair>> allSchedulesBusIdMap = new HashMap<>();
    private static List<Schedule_SourceDestinationTimeDaysPair> allSchedules = new ArrayList<>();

    public static int getNoOfSchedules() {
        return noOfSchedules;
    }
    public static void setNoOfSchedules(int noOfSchedules) {
        SchedulesData.noOfSchedules = noOfSchedules;
    }
    public static void incrementNoOfSchedules() {
        SchedulesData.noOfSchedules++;
    }
    public static HashMap<Integer, List<Schedule_SourceDestinationTimeDaysPair>> getAllSchedulesBusIdMap() {
        return allSchedulesBusIdMap;
    }
    public static List<Schedule_SourceDestinationTimeDaysPair> getAllSchedules() {
        return allSchedules;
    }


}
