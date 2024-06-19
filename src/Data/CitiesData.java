package Data;

import java.util.ArrayList;
import java.util.List;

public class CitiesData {
    private static List<String> availableCities = new ArrayList<>();

    public static List<String> getAvailableCities() {
        return availableCities;
    }
    public static void setAvailableCities(List<String> availableCities) {
        CitiesData.availableCities = availableCities;
    }
    public static void setCities(List<String> cities){
        CitiesData.setAvailableCities(cities);
    }
}
