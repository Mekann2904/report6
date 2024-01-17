package com.example.jp.ac.uryukyu.ie.e205737.weatherapp;

import java.util.Locale;
import android.webkit.WebSettings;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;





/**
 * This class represents the main activity of the weather application.
 * It displays weather information, current time, and a WebView for additional content.
 */

public class MainActivity extends AppCompatActivity implements WeatherCallback {

    private EditText cityNameEditText; //
    private Button getWeatherButton;//
    private TextView cityNameTextView;
    private TextView temperatureTextView;
    private TextView weatherDescriptionTextView;
    private TextView currentTimeTextView;
    private WebView webView;
    private Time timeInstance;

    /**
     * Called when the activity is first created.
     * Initializes UI components, fetches weather data, and starts displaying the current time.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityNameEditText = findViewById(R.id.cityNameEditText);//追加
        getWeatherButton = findViewById(R.id.getWeatherButton);

        cityNameTextView = findViewById(R.id.cityNameTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        weatherDescriptionTextView = findViewById(R.id.weatherDescriptionTextView);
        currentTimeTextView = findViewById(R.id.currentTimeTextView);
        webView = findViewById(R.id.webView); // WebViewの初期化


        Geocoding geocoding = new Geocoding("ginowan", "JP", 1, "0cc10e651c9d70f8fa70565a4a081246", this);
        geocoding.executeAsyncTask();


        // 天気取得ボタンが押されたときの処理
        Button getWeatherButton = findViewById(R.id.getWeatherButton);
        getWeatherButton.setOnClickListener(view -> {
            String cityName = cityNameEditText.getText().toString();
            if (!cityName.isEmpty()) {
                // 入力された都市名を使ってGeocodingオブジェクトを作成し、天気情報を取得
                // 既存のgeocodingオブジェクトを再利用し、新しい都市の天気情報を取得
                geocoding.setCityName(cityName);
                geocoding.executeAsyncTask();
            } else {
                // 都市名が入力されていない場合のエラーハンドリングなどを行う
                Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
            }
        });

        //現在時刻の取得
        Time time = new Time(this);
        timeInstance = time;
        time.displayCurrentTime();
        time.startClock();

        // WebViewの設定
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // JavaScriptを有効化
        webSettings.setJavaScriptEnabled(true);
        // HTTPS対応URLをWebViewにロード
        String url = "https://www.msn.com/ja-jp/weather/forecast/in-%E6%B2%96%E7%B8%84%E7%9C%8C,%E5%AE%9C%E9%87%8E%E6%B9%BE%E5%B8%82?loc=eyJsIjoi5a6c6YeO5rm%2B5biCIiwiciI6Iuaylue4hOecjCIsImMiOiLml6XmnKwiLCJpIjoiSlAiLCJnIjoiamEtanAiLCJ4IjoiMTI3Ljc1IiwieSI6IjI2LjI3In0%3D&ocid=bingnews_ts&weadegreetype=C";
        webView.loadUrl(url);
    }

    /**
     * Updates the current time on the UI.
     *
     * @param currentTime The current time to be displayed.
     */
    public void updateCurrentTime(String currentTime) {
        runOnUiThread(() -> {
            currentTimeTextView.setText(currentTime);
        });
    }

    /**
     * Called when the activity is about to be destroyed.
     * Stops the clock to avoid resource leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timeInstance != null) {
            timeInstance.stopClock();
        }
    }


    /**
     * Callback method called when weather data is received.
     * Updates the UI with the received weather information.
     *
     * @param cityName           The name of the city.
     * @param temperatureKelvin  The temperature in Kelvin.
     * @param weatherDescription The description of the weather.
     */
    @Override
    public void onWeatherDataReceived(String cityName, double temperatureKelvin, String weatherDescription) {
        // 温度をケルビンから摂氏に変換
        double temperatureCelsius = temperatureKelvin - 273.15;

        runOnUiThread(() -> {
            // 温度を小数点以下1桁まで表示
            String formattedTemperature = String.format(Locale.getDefault(), "%.1f", temperatureCelsius);

            Log.d("MainActivity_a", "データでUIを更新中 - 都市名: " + cityName + "、温度: " + formattedTemperature + "℃、天気情報: " + weatherDescription);

            cityNameTextView.setText("都市名: " + cityName);
            temperatureTextView.setText("温度 WeatherAPI: " + formattedTemperature + "℃");
            weatherDescriptionTextView.setText("天気情報: " + weatherDescription);
        });
    }


    /**
     * Callback method called when an error occurs during weather data retrieval.
     * Logs the error message.
     *
     * @param errorMessage The error message.
     */
    @Override
    public void onError(String errorMessage) {
        runOnUiThread(() -> {
            Log.e("MainActivity", errorMessage);
        });
    }


    /**
     * Overrides the default behavior of the back button.
     * Navigates back in the WebView if possible; otherwise, performs the default back action.
     */
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack(); // WebViewが戻ることができる場合は、前のページに戻る
        } else {
            super.onBackPressed();// WebViewが戻ることができない場合は、通常のバック処理を行う
        }
    }

    /**
     * Utility method to get a string resource by its ID.
     *
     * @param resId The resource ID of the string.
     * @return The string resource.
     */
    public String getStringResource(int resId) {
        return getResources().getString(resId);
    }
}
