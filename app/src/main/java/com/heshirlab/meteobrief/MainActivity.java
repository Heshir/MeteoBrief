package com.heshirlab.meteobrief;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private TextView tv_city, tv_date, tv_temperature, tv_feelsLike, tv_windSpeed, tv_coordinates;
    private double latitude, longitude;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_CODE_PERMISSION = 1;
    private Geocoder geocoder;
    //private static final int TIME_OUT = 1000 * 60 * 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //FloatingActionButton fab = findViewById(R.id.fab);
        tv_city = findViewById(R.id.tv_city_name);
        tv_date = findViewById(R.id.tv_date);
        tv_temperature = findViewById(R.id.tv_temperature);
        tv_feelsLike = findViewById(R.id.tv_feelsLike);
        tv_windSpeed = findViewById(R.id.tv_windSpeed);
        tv_coordinates = findViewById(R.id.tv_coords);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this);

    }

    private String getLastCityName() {
        String curAddr = null;
        try {
            curAddr = geocoder.getFromLocation(latitude, longitude, 500).get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (curAddr == null) {
            curAddr = "Unknown adress (null)";
        }
        return curAddr;
    }

    private void showWeatherInfo(PostPojo pPogo) {

        String cityText = "Location: " + getLastCityName();
        String dateText = "Current date: " + getDateForNow();
        String tempText = "The current temperature is: " + Math.round(pPogo.getMainParams().get("temp"));
        String feelsLikeText = "But it feels like: " + Math.round(pPogo.getMainParams().get("feels_like"));
        String windSpeedText = "Wind speed is: " + Math.round(pPogo.getWindParams().get("speed")) + " m/s";
        tv_city.setText(cityText);
        tv_date.setText(dateText);
        tv_temperature.setText(tempText);
        tv_feelsLike.setText(feelsLikeText);
        tv_windSpeed.setText(windSpeedText);


    }


    private String getDateForNow() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    public void myWeatherTaker(View view) {
           WeatherService.getInstance().getMessagesApi().getWeatherForCity(latitude, longitude).enqueue(new Callback<PostPojo>() {
            @Override
            public void onResponse(@NotNull Call<PostPojo> call, @NotNull Response<PostPojo> response) {
                if (response.isSuccessful()) {
                    PostPojo post = response.body();
                    if (post != null) {
                        getLocationInfo();
                        showWeatherInfo(post);

                    } else {
                        Toast.makeText(getApplicationContext(), "Какая-то херь: Json пустой", Toast.LENGTH_LONG).show();
                    }

                } else
                    Toast.makeText(getApplicationContext(), "Какая-то херь: неудачный запрос", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NotNull Call<PostPojo> call, @NotNull Throwable t) {
                tv_city.setText(R.string.text_request_getting_err);
                t.printStackTrace();

            }
        });
    }

    private void getLocationInfo() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            String tempText;

            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    tempText = ("You are at " + latitude + " latitude and " + longitude + " longitude");
                } else {
                    tempText = "location == null. Deal with it";
                }
                tv_coordinates.setText(tempText);
            }
        });

    }
}





