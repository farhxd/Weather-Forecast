package com.farhad.weatherforecast;

import java.util.List;

public class WeatherResponse {
    private City city;
    private List<ForecastItem> list;

    public List<ForecastItem> getList() {
        return list;
    }

    public City getCity() {
        return city;
    }

    public static class City {
        private long sunrise;
        private long sunset;
        private String name;
        private Coord coord;

        public long getSunrise() {
            return sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public String getName() {
            return name;
        }

        public Coord getCoord() {
            return coord;
        }
    }

    public static class Coord {
        private double lat;
        private double lon;

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }
    }

    public static class ForecastItem {
        private long dt;
        private Main main;
        private List<Weather> weather;
        private Rain rain;
        private Snow snow;
        private Wind wind;
        private Clouds clouds;
        private City city;
        private int visibility;

        public long getDt() {
            return dt;
        }

        public Main getMain() {
            return main;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public Rain getRain() {
            return rain;
        }

        public int getVisibility() {
            return visibility;
        }

        public Snow getSnow() {
            return snow;
        }

        public Wind getWind() {
            return wind;
        }

        public Clouds getClouds() {
            return clouds;
        }
    }

    public static class Main {
        private double temp_min;
        private double temp_max;
        private double temp;
        private int humidity;
        private double pressure;
        private double feels_like;

        public double getTemp_min() {
            return temp_min;
        }

        public double getTemp_max() {
            return temp_max;
        }

        public double getTemp() {
            return temp;
        }

        public int getHumidity() {
            return humidity;
        }

        public double getPressure() {
            return pressure;
        }
        public double getFeels_like() {
            return feels_like;
        }
    }
    public static class Visibility {
        private int visibility;

        public int getVisibility() {
            return visibility;
        }
    }

    public static class Weather {
        private String description;
        private String main;

        public String getDescription() {
            return description;
        }

        public String getMain() {
            return main;
        }
    }

    public static class Rain {
        private double _3h; // Rain volume for last 3 hours

        public double getVolume() {
            return _3h;
        }
    }

    public static class Snow {
        private double _3h; // Snow volume for last 3 hours

        public double getVolume() {
            return _3h;
        }
    }

    public static class Wind {
        private double speed;
        private double deg;
        private double gust;

        public double getSpeed() {
            return speed;
        }

        public double getDeg() {
            return deg;
        }
        public double getGust() {
            return gust;
        }
    }

    public static class Clouds {
        private int all; // Cloudiness %

        public int getAll() {
            return all;
        }
    }
}
