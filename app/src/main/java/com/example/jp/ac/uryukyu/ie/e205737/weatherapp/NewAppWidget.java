package com.example.jp.ac.uryukyu.ie.e205737.weatherapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import java.util.Locale;

/**
 * This class represents an AppWidgetProvider for a weather widget.
 * It extends AppWidgetProvider and implements the WeatherCallback interface
 * to handle the asynchronous retrieval of weather data and update the widget.
 */
public class NewAppWidget extends AppWidgetProvider implements WeatherCallback {

    private Context context;

    /**
     * Called in response to the AppWidgetManager's request to update all widgets of this provider.
     *
     * @param context          The context in which the receiver is running.
     * @param appWidgetManager A manager to communicate with AppWidget hosts.
     * @param appWidgetIds     The appWidgetIds for which an update is needed.
     */

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;

        // ウィジェットの更新
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    /**
     * Updates the AppWidget's layout with weather data retrieved asynchronously.
     *
     * @param context          The context in which the receiver is running.
     * @param appWidgetManager A manager to communicate with AppWidget hosts.
     * @param appWidgetId      The appWidgetId to update.
     */

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // ウィジェットのレイアウトを設定
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        // Weather API からデータを非同期に取得
        Geocoding geocoding = new Geocoding("ginowan", "JP", 1, "0cc10e651c9d70f8fa70565a4a081246", this);
        geocoding.executeAsyncTask();
    }

    /**
     * Callback method invoked upon successful retrieval of weather data.
     * Updates the widget's RemoteViews with the received data.
     *
     * @param cityName            The name of the city.
     * @param temperatureKelvin   The temperature in Kelvin.
     * @param weatherDescription  The description of the weather.
     */
    // WeatherCallback のメソッドを実装
    @Override
    public void onWeatherDataReceived(String cityName, double temperatureKelvin, String weatherDescription) {
        // ウィジェットにデータを表示
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.widget_city_name, "都市名: " + cityName);

        // 温度をケルビンから摂氏に変換
        double temperatureCelsius = temperatureKelvin - 273.15;
        String formattedTemperature = String.format(Locale.getDefault(), "%.1f", temperatureCelsius);
        views.setTextViewText(R.id.widget_temperature, "温度: " + formattedTemperature + "℃");

        views.setTextViewText(R.id.widget_weather_description, "天気情報: " + weatherDescription);

        // ウィジェットを再度更新
        AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, NewAppWidget.class), views);
    }

    /**
     * Callback method invoked when an error occurs during weather data retrieval.
     *
     * @param errorMessage The error message describing the issue.
     */
    @Override
    public void onError(String errorMessage) {
        // エラーが発生した場合の処理を記述
        // 例: エラーメッセージをログに出力するなど
        // Log.e("NewAppWidget", errorMessage);
    }
}

