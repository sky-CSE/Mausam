package com.example.mausam;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView cityName, temp, condition, humidity, maxTemp, minTemp, pressure, wind;
    private ImageView image;
    FloatingActionButton fab;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.main_city);
        image = findViewById(R.id.main_image);
        temp = findViewById(R.id.main_temp);
        condition = findViewById(R.id.main_condition);
        humidity = findViewById(R.id.main_humidity);
        maxTemp = findViewById(R.id.main_maxtemp);
        minTemp = findViewById(R.id.main_mintemp);
        pressure = findViewById(R.id.main_pressure);
        wind = findViewById(R.id.main_wind);
        fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, OtherCity.class);
            startActivity(i);
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                getWeatherData(lat, lon);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
    }

    public void getWeatherData(double lat, double lon) {
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithLocation(lat, lon);

        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                cityName.setText(response.body().getName() + " , " + response.body().getSys().getCountry());
                temp.setText(response.body().getMain().getTemp() + " °C");
                condition.setText(response.body().getWeather().get(0).getDescription());
                humidity.setText(" : " + response.body().getMain().getHumidity() + "%");
                maxTemp.setText(" : " + response.body().getMain().getTempMax() + " °C");
                minTemp.setText(" : " + response.body().getMain().getTempMin() + " °C");
                pressure.setText(" : " + response.body().getMain().getPressure());
                wind.setText(" : " + response.body().getWind().getSpeed());

                String iconCode = response.body().getWeather().get(0).getIcon();
                Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                        .placeholder(R.drawable.ic_launcher_background).into(image);

            }

            @Override
            public void onFailure(@NonNull Call<OpenWeatherMap> call, @NonNull Throwable t) {

            }
        });
    }
}