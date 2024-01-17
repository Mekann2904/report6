package com.example.jp.ac.uryukyu.ie.e205737.weatherapp;

/**
 * An interface for providing callbacks related to weather data retrieval.
 */
public interface WeatherCallback {
    /**
     * Callback method invoked when weather data is successfully received.
     *
     * @param cityName            The name of the city for which weather data is received.
     * @param temperature         The temperature in the specified city.
     * @param weatherDescription  The description of the weather in the specified city.
     */
    void onWeatherDataReceived(String cityName, double temperature, String weatherDescription);
    /**
     * Callback method invoked in case of an error during weather data retrieval.
     *
     * @param errorMessage  The error message describing the issue.
     */
    void onError(String errorMessage);
}
