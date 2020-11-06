package com.romeo.weatherappnew;

import java.util.HashSet;
import java.util.Set;

public class CityWeatherContainer {

    private Set<String> cities = new HashSet<>();
    private static final CityWeatherContainer instance = new CityWeatherContainer();

    public static CityWeatherContainer getInstance() {
        return instance;
    }

    private CityWeatherContainer() {
    }

    public void addCity(String city) {
        cities.add(city);
    }

    public Object[] getCitiesArray() {
        return cities.toArray();
    }

    public void clear() {
        cities = new HashSet<>();
    }
}