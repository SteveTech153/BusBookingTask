package System;

import Data.CitiesData;

import java.util.List;

public interface CityManager {
    public default boolean addCity(String city){
        if(CitiesData.getAvailableCities().contains(city)){
            System.out.println("City already exists");
            return false;
        }
        CitiesData.getAvailableCities().add(city);
        System.out.println("City added successfully");
        return true;
    }
    public static void showAvailableCities() {
        CitiesData.getAvailableCities().forEach(System.out::println);
        System.out.println("");
        if(CitiesData.getAvailableCities().isEmpty()){
            System.out.println("No cities available\n");
        }
    }
    public default boolean removeCity(String city){
        if(CitiesData.getAvailableCities().contains(city)) {
            CitiesData.getAvailableCities().remove(city);
            System.out.println("City removed");
            return true;
        }
        else {
            System.out.println("City not found");
            return false;
        }
    }
    public default void setCities(List<String> cities){
        CitiesData.setCities(cities);
    }
}
