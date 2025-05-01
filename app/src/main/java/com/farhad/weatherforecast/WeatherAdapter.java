package com.farhad.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private Context context;
    private List<WeatherResponse.ForecastItem> forecastList;

    public WeatherAdapter(Context context, List<WeatherResponse.ForecastItem> forecastList) {
        this.context = context;
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        WeatherResponse.ForecastItem item = forecastList.get(position);

        long dt = item.getDt() * 1000L;
        String dayName;
        if (position == 0) {
            dayName = "Today";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
            dayName = sdf.format(new Date(dt));
        }

        holder.dayText.setText(dayName);
      //  holder.descText.setText(item.getWeather().get(0).getDescription());
        holder.tempMinText.setText("Min: " + Math.round(item.getMain().getTemp_min()) + "°C");
        holder.tempMaxText.setText("Max: " + Math.round(item.getMain().getTemp_max()) + "°C");
        if (position == 4) {
            holder.div1.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayText, descText, tempMinText, tempMaxText;
        LinearLayout div1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayOfWeek);
           // descText = itemView.findViewById(R.id.descText);
            tempMinText = itemView.findViewById(R.id.dayLow);
            tempMaxText = itemView.findViewById(R.id.dayHigh);
            div1 = itemView.findViewById(R.id.div1);
        }
    }
}
