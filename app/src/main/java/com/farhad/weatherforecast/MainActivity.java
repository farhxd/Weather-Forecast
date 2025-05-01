package com.farhad.weatherforecast;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    private TextView todayLow, todayHigh,temp, address2, visiblity, feellike, address, desc, windGust, humidity, pressure, sunrise, sunset, rainVol, cloudiness, windSpeed, windDeg;
    private WeatherApiService apiService;
    private double latitude = 35.6895;
    private double longitude = 139.6917;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        todayHigh = findViewById(R.id.high);
        todayLow = findViewById(R.id.low);
        temp = findViewById(R.id.temp);
        address = findViewById(R.id.address);
        address2 = findViewById(R.id.address2);
        desc = findViewById(R.id.desc);

        humidity = findViewById(R.id.valHumidity);
        pressure = findViewById(R.id.valPressure);
        sunrise = findViewById(R.id.valSunrise);
        sunset = findViewById(R.id.valSunset);
        rainVol = findViewById(R.id.valRain);
        cloudiness = findViewById(R.id.valCloudiness);
        windDeg = findViewById(R.id.valWindDeg);
        windSpeed = findViewById(R.id.valWindSpeed);
        windGust = findViewById(R.id.windGust);
        visiblity = findViewById(R.id.visibility);
        feellike = findViewById(R.id.feellike);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
         apiService = retrofit.create(WeatherApiService.class);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getLocationAndFetchWeather();
        }
        if (!isLocationEnabled()) {
            showLocationEnableDialog();
        }


    }

    private void getLocationAndFetchWeather() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d(TAG, "FusedLocation: " + latitude + " " + longitude);

                        updateAddressAndFetchWeather();
                    } else {
                        // LAST location is null. Let's try requesting NEW location
                        requestNewLocation();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Location error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void requestNewLocation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(500);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null) {
                    Toast.makeText(MainActivity.this, "Still unable to fetch location", Toast.LENGTH_SHORT).show();
                    return;
                }
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.d(TAG, "NewLocation: " + latitude + " " + longitude);
                    updateAddressAndFetchWeather();
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void updateAddressAndFetchWeather() {
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String cityName = addresses.get(0).getLocality();
                address2.setText(cityName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        fetchWeather();
    }



    private void fetchWeather() {
        String apiKey = "a143d09bd564d24b4652d34a2e2f6aa0";
        apiService.getWeatherData(latitude, longitude, "metric", apiKey)
                    .enqueue(new Callback<WeatherResponse>() {
                        @Override
                        public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<WeatherResponse.ForecastItem> allForecasts = response.body().getList();
                                List<WeatherResponse.ForecastItem> dailyForecasts = new ArrayList<>();

                                String lastDate = "";

                                for (WeatherResponse.ForecastItem item : allForecasts) {
                                    Date date = new Date(item.getDt() * 1000L);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    String currentDate = dateFormat.format(date);

                                    // Pick one forecast per day (first time we see this date)
                                    if (!currentDate.equals(lastDate)) {
                                        dailyForecasts.add(item);
                                        lastDate = currentDate;
                                    }

                                    if (dailyForecasts.size() == 5) break; // Only 5 days
                                }

                                // Show current
                                WeatherResponse.ForecastItem today = dailyForecasts.get(0);
                                try {


                                    temp.setText(Math.round(today.getMain().getTemp_max()) + "°C");
                                    todayLow.setText("Min: " + Math.round(today.getMain().getTemp_min()) + "°C");
                                    todayHigh.setText("Max: " + Math.round(today.getMain().getTemp_max()) + "°C");
                                    desc.setText(capitalizeString(today.getWeather().get(0).getDescription()));
                                    humidity.setText(String.valueOf(today.getMain().getHumidity()) + "%");
                                    pressure.setText(String.valueOf(today.getMain().getPressure()));

                                    long sunRiseMillis = response.body().getCity().getSunrise() * 1000L;
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                    sunrise.setText(simpleDateFormat.format(new Date(sunRiseMillis)));

                                    long sunsetMillis = response.body().getCity().getSunset() * 1000L;
                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                    sunset.setText(simpleDateFormat.format(new Date(sunsetMillis)));
                                    if (today.getRain() == null) {
                                        rainVol.setText("null");
                                    } else {
                                        rainVol.setText(String.valueOf(today.getRain().getVolume()));
                                    }
                                    cloudiness.setText(String.valueOf(today.getClouds().getAll()) + "%");

                                    windDeg.setText((Math.round(today.getWind().getDeg()) + "° Direction"));
                                    windSpeed.setText(Math.round(today.getWind().getSpeed() * 3.6) + "KM/H Speed");
                                    windGust.setText("Wind Gust: " + Math.round(today.getWind().getGust() * 3.6) + "KM/H");
                                    feellike.setText(String.valueOf(Math.round(today.getMain().getFeels_like())) + "°");
                                    visiblity.setText(String.valueOf(today.getVisibility() / 1000) + "KM");

                                    Log.d("TAG", "onResponse: " + (capitalizeString(today.getWeather().get(0).getDescription())));


                                    weatherAdapter = new WeatherAdapter(MainActivity.this, dailyForecasts);
                                    recyclerView.setAdapter(weatherAdapter);

                                } catch (Exception e) {
                                    Log.d(TAG, "onResponse: " + e.toString());
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to get weather data", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherResponse> call, Throwable t) {
                            AlertDialog.Builder networkIssue = new AlertDialog.Builder(MainActivity.this);
                            networkIssue.setTitle("There is an issue with your network")
                                            .setMessage("Please Turn on your Wifi or Mobile data to get location updates from our servers")
                                                    .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            fetchWeather();
                                                        }
                                                    });
                            networkIssue.create().show();

                            Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


    }
    public static String capitalizeString(String str) {
        String[] words = str.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            sb.append(Character.toUpperCase(words[i].charAt(0)))
                    .append(words[i].substring(1));
            if (i < words.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    private void showLocationEnableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable Location")
                .setMessage("Location is required for this feature. Please enable it in settings.")
                .setCancelable(false)
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    Toast.makeText(this, "Location is needed", Toast.LENGTH_SHORT).show();
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}