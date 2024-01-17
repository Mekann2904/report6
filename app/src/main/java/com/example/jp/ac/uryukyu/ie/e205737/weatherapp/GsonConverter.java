package com.example.jp.ac.uryukyu.ie.e205737.weatherapp;

import com.google.gson.Gson;


/**
 * Utility class for converting JSON data to Java objects using Gson library.
 */
public class GsonConverter {
    /**
     * Parses JSON data to an array of GeocodingData objects.
     *
     * @param json The JSON data to parse.
     * @return An array of GeocodingData objects.
     */

    public GeocodingData[] parseJsonToGeocodingDataArray(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, GeocodingData[].class);
    }

    /**
     * Represents Geocoding data obtained from JSON.
     */

    public static class GeocodingData {
        private String name;
        private double lat;
        private double lon;
        private String country; // add this line


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    /**
     * Parses JSON data to a WeatherData object.
     *
     * @param json The JSON data to parse.
     * @return A WeatherData object.
     */
    public WeatherData parseJsonToWeatherData(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, WeatherData.class);
    }

    /**
     * Represents weather data obtained from JSON.
     */
    public static class WeatherData {
        private String name;
        private MainData main;
        private WeatherDescription[] weather;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public MainData getMain() {
            return main;
        }

        public void setMain(MainData main) {
            this.main = main;
        }

        public WeatherDescription[] getWeather() {
            return weather;
        }

        public void setWeather(WeatherDescription[] weather) {
            this.weather = weather;
        }
    }

    /**
     * Represents main data within WeatherData.
     */
    public static class MainData {
        private double temp;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }
    }

    /**
     * Represents weather description within WeatherData.
     */
    public static class WeatherDescription {
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
