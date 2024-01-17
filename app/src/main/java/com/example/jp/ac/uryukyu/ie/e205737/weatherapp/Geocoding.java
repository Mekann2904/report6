package com.example.jp.ac.uryukyu.ie.e205737.weatherapp;

import android.os.AsyncTask;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * This class handles geocoding functionality using OpenWeatherMap API.
 * It asynchronously fetches geocoding data based on city name and country code.
 */
public class Geocoding {

    private String city_name;
    private String country_code;
    private int limit;
    private String API_key;
    private WeatherCallback weatherCallback;


    /**
     * Constructor for Geocoding class.
     *
     * @param city_name       The name of the city.
     * @param country_code    The country code.
     * @param limit           The limit for the number of results.
     * @param API_key         The API key for accessing the OpenWeatherMap API.
     * @param weatherCallback The callback interface for weather data.
     */

    // コンストラクタに WeatherCallback を追加
    public Geocoding(String city_name, String country_code, int limit, String API_key, WeatherCallback weatherCallback) {
        // コンストラクタの実装
        this.city_name = city_name;
        this.country_code = country_code;
        this.limit = limit;
        this.API_key = API_key;
        this.weatherCallback = weatherCallback;

    }

    // 都市名を設定するメソッド
    public void setCityName(String cityName) {
        this.city_name = cityName;
    }

    /**
     * Executes the geocoding task asynchronously.
     */
    public void executeAsyncTask() {
        new GeocodingAsyncTask().execute();
    }

    /**
     * AsyncTask for performing geocoding in the background.
     */
    private class GeocodingAsyncTask extends AsyncTask<Void, Void, String> {

        /**
         * Background task to perform geocoding using the OpenWeatherMap API.
         *
         * @param voids Not used.
         * @return The JSON response from the API.
         */
        @Override
        protected String doInBackground(Void... voids) {
            try {
                OkHttpClient client = new OkHttpClient();

                // URLを構築
                String url = "https://api.openweathermap.org/geo/1.0/direct?q=" +
                        city_name + "," + country_code + "&limit=" + limit + "&appid=" + API_key;

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                // リクエストを実行
                try (Response response = client.newCall(request).execute()) {
                    return response.body() != null ? response.body().string() : "Empty response body";
                }
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }


        /**
         * Post-execution task to handle the JSON response and initiate weather data retrieval.
         *
         * @param result The JSON response from the API.
         */
        // GeocodingAsyncTaskクラス内
        @Override
        protected void onPostExecute(String result) {

            Log.d("GeocodingAsyncTask", "Response: " + result); // 非同期タスクが完了した後に行いたい処理をここに追加
            try {
                GsonConverter gsonConverter = new GsonConverter();// GsonConverterをインスタンス化
                GsonConverter.GeocodingData[] geocodingDataArray = gsonConverter.parseJsonToGeocodingDataArray(result);// GsonConverterの呼び出し json形式からjavaに変換

                if (geocodingDataArray != null && geocodingDataArray.length > 0) {
                    GsonConverter.GeocodingData geocodingData = geocodingDataArray[0]; // 配列の先頭を取得
                    Log.d("GeocodingAsyncTask_Con", "City Name: " + geocodingData.getName());
                    Log.d("GeocodingAsyncTask_Con", "Latitude: " + geocodingData.getLat());
                    Log.d("GeocodingAsyncTask_Con", "Longitude: " + geocodingData.getLon());
                    Log.d("GeocodingAsyncTask_Con", "Country: " + geocodingData.getCountry());
                    Weather weather = new Weather(geocodingData.getLat(), geocodingData.getLon(), "0cc10e651c9d70f8fa70565a4a081246"); // 天気情報取得
                    // Weather クラスに WeatherCallback を設定
                    weather.setWeatherCallback(new WeatherCallback() {
                        @Override
                        public void onWeatherDataReceived(String cityName, double temperature, String weatherDescription) {
                            // メソッドの実装
                            Log.d("GeocodingAsyncTask_Con", "Weather Data Received - City Name: " + cityName + ", Temperature: " + temperature + ", Weather Description: " + weatherDescription);

                            // 天気情報取得時のコールバックを呼び出す
                            if (weatherCallback != null) {
                                weatherCallback.onWeatherDataReceived(cityName, temperature, weatherDescription);
                            }
                        }
                        @Override
                        public void onError(String errorMessage) {
                            // メソッドの実装
                            Log.e("GeocodingAsyncTask_Con", "Weather Data Error: " + errorMessage);

                            // エラー時のコールバックを呼び出す
                            if (weatherCallback != null) {
                                weatherCallback.onError(errorMessage);
                            }
                        }
                    });
                    weather.executeAsyncTask();
                } else {
                    Log.e("GeocodingAsyncTask", "Error: Empty geocodingDataArray");
                }
            } catch (Exception e) {
                Log.e("GeocodingAsyncTask", "Error: " + e.getMessage());
                // エラー時のコールバックを呼び出す
                if (weatherCallback != null) {
                    weatherCallback.onError("Error: " + e.getMessage());
                }
            }
        }
    }
}


