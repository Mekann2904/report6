package com.example.jp.ac.uryukyu.ie.e205737.weatherapp;



import android.util.Log;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A class for fetching weather data using the OpenWeatherMap API.
 */

public class Weather {
    private double lat;
    private double lon;
    private String apiKey;

    /**
     * Constructor for the Weather class.
     *
     * @param lat    The latitude of the location.
     * @param lon    The longitude of the location.
     * @param apiKey The API key for accessing the OpenWeatherMap API.
     */
    public Weather(double lat, double lon, String apiKey) {
        this.lat = lat;
        this.lon = lon;
        this.apiKey = apiKey;
    }


    private WeatherCallback callback;

    /**
     * Sets the callback interface for receiving weather data.
     *
     * @param callback The callback interface.
     */

    public void setWeatherCallback(WeatherCallback callback) {
        this.callback = callback;
    }

    /**
     * Executes the weather task asynchronously.
     */
    public void executeAsyncTask() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(() -> {
            OkHttpClient client = new OkHttpClient();
            String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey;

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.body() != null) {
                    return response.body().string();
                } else {
                    throw new IOException("Empty response body");
                }
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        });
        executorService.shutdown();
        try {
            String result = future.get();
            // ネットワークリクエストが完了した後に行いたい処理をここに追加
            Log.d("WeatherAsyncTask", "Response: " + result);

            // GsonConverterをインスタンス化
            GsonConverter gsonConverter = new GsonConverter();

            // GsonConverterの呼び出し json形式からjavaに変換
            GsonConverter.WeatherData weatherData = gsonConverter.parseJsonToWeatherData(result);
            Log.d("WeatherAsyncTask_Con", "City Name: " + weatherData.getName());
            Log.d("WeatherAsyncTask_Con", "Temperature: " + weatherData.getMain().getTemp());
            Log.d("WeatherAsyncTask_Con", "Weather Description: " + weatherData.getWeather()[0].getDescription());
            String cityName = weatherData.getName();
            double temperature = weatherData.getMain().getTemp();
            String weatherDescription = weatherData.getWeather()[0].getDescription();

            // コールバックを使用してデータを通知
            if (callback != null) {
                callback.onWeatherDataReceived(cityName, temperature, weatherDescription);
            }
        } catch (Exception e) {
            // その他の例外に対するエラー処理
            Log.e("WeatherAsyncTask", "Error: " + e.getMessage());

            // エラーが発生した場合もコールバックを使用して通知
            if (callback != null) {
                callback.onError("Error: " + e.getMessage());
            }
        }
    }
}

