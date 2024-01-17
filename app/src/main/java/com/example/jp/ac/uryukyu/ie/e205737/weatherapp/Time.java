package com.example.jp.ac.uryukyu.ie.e205737.weatherapp;

import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A utility class for handling time-related operations and updating the UI with the current time.
 */
public class Time {
    private Handler handler;
    private MainActivity mainActivity;  // MainActivityのインスタンスを格納するメンバ変数

    /**
     * Constructor for the Time class.
     *
     * @param mainActivity The instance of MainActivity to associate with this Time instance.
     */
    public Time(MainActivity mainActivity) {
        this.handler = new Handler();
        this.mainActivity = mainActivity;  // コンストラクタでMainActivityのインスタンスを受け取る
    }

    /**
     * Starts the clock and initiates the periodic update task for displaying the current time.
     */
    public void startClock() {
        // 初回の表示
        displayCurrentTime();

        // 定期的に時間を更新するタスクを開始
        handler.postDelayed(timeUpdateTask, 1000); // 1000ミリ秒（1秒）ごとに更新
    }

    /**
     * A periodic task to update and display the current time.
     */
    private Runnable timeUpdateTask = new Runnable() {
        @Override
        public void run() {
            displayCurrentTime();
            // 再度タスクをスケジュール
            handler.postDelayed(this, 1000);
        }
    };

    /**
     * Stops the clock by removing the time update task.
     */

    public void stopClock() {
        // 時計更新のタスクを停止
        handler.removeCallbacks(timeUpdateTask);
    }

    /**
     * Displays the current date and time, and updates the UI in MainActivity.
     */
    public void displayCurrentTime() {
        // 現在時刻を取得
        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        // 日付と時刻をフォーマットしてAndroidのログに出力（確認用）
        Log.d("Time_a", "日付：" + dateFormat.format(date));
        Log.d("Time_a", "時刻：" + timeFormat.format(date));

        // MainActivityのUIを更新
        mainActivity.runOnUiThread(() -> {
            mainActivity.updateCurrentTime("Current Time: " + timeFormat.format(date));
        });
    }
}
