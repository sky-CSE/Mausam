package com.example.mausam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherCity extends AppCompatActivity {
    private TextView other_cityName, other_temp, other_condition, other_humidity, other_maxTemp, other_minTemp, other_pressure, other_wind;
    private ImageView other_image, other_search;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_city);

        other_cityName= findViewById(R.id.other_city);
        other_temp=findViewById(R.id.other_temp);
        other_condition= findViewById(R.id.other_condition);
        other_humidity=findViewById(R.id.other_humidity);
        other_maxTemp=findViewById(R.id.other_maxtemp);
        other_minTemp=findViewById(R.id.other_mintemp);
        other_pressure=findViewById(R.id.other_pressure);
        other_wind=findViewById(R.id.other_wind);
        editText = findViewById(R.id.other_searchcityname);
        other_image=findViewById(R.id.other_image);
        other_search = findViewById(R.id.other_search);

        other_search.setOnClickListener(view->{
            String cityName = editText.getText().toString();
            getWeatherData(cityName);
            editText.setText("");

        });
    }

    public void getWeatherData(String name) {
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithCityName(name);

        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                if (response.isSuccessful()) {
                    other_cityName.setText(response.body().getName() + " , " + response.body().getSys().getCountry());
                    other_temp.setText(response.body().getMain().getTemp() + " °C");
                    other_condition.setText(response.body().getWeather().get(0).getDescription());
                    other_humidity.setText(" : " + response.body().getMain().getHumidity() + "%");
                    other_maxTemp.setText(" : " + response.body().getMain().getTempMax() + " °C");
                    other_minTemp.setText(" : " + response.body().getMain().getTempMin() + " °C");
                    other_pressure.setText(" : " + response.body().getMain().getPressure());
                    other_wind.setText(" : " + response.body().getWind().getSpeed());

                    String iconCode = response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                            .placeholder(R.drawable.ic_launcher_background).into(other_image);

                }
                else
                    Toast.makeText(getApplicationContext(),"City not found,Try again.",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(@NonNull Call<OpenWeatherMap> call, @NonNull Throwable t) {

            }
        });
    }
}