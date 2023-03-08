package com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.forecast7day;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pyxis.compass.gpscompassnavigationmap.R;
import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.hourtoday.modelhour.DailyData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ForeCastAdapter extends RecyclerView.Adapter<ForeCastAdapter.ViewHolder> {
    List<DailyData> foreCastDayList;
    Context context;
    boolean checkF = false;

    public ForeCastAdapter(List<DailyData> foreCastDayList, Context context, boolean isFarenheit) {
        this.foreCastDayList = foreCastDayList;
        this.context = context;
        this.checkF = isFarenheit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weather_7day, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String rainProb = String.valueOf(Math.round(foreCastDayList.get(i).getPrecipProbability() * 100));
        viewHolder.tvWet.setText(rainProb + "%");

        viewHolder.tvTime.setText(new SimpleDateFormat("E, dd/MM").format(new Date(foreCastDayList.get(i).getTime() * 1000)));
        StringBuilder min = new StringBuilder(foreCastDayList.get(i).getTemperatureLow().toString());
        min.delete(2, 5);
        viewHolder.tvMinTemp.setText(min + "ºC");

        StringBuilder max = new StringBuilder(foreCastDayList.get(i).getTemperatureHigh().toString());
        max.delete(2, 5);
        viewHolder.tvMaxTemp.setText(max + "ºC");

        int iconId = 0;
        switch (foreCastDayList.get(i).getIcon()) {
            case "clear-day":
                iconId = R.drawable.ic_weather_clear_day;
                break;
            case "clear-night":
                iconId = R.drawable.ic_weather_clear_night;
                break;
            case "rain":
                iconId = R.drawable.ic_weather_rain;
                break;
            case "snow":
                iconId = R.drawable.ic_weather_snow;
                break;
            case "sleet":
                iconId = R.drawable.ic_weather_sleet;
                break;
            case "wind":
                iconId = R.drawable.ic_weather_wind;
                break;
            case "fog":
                iconId = R.drawable.ic_weather_fog;
                break;
            case "cloudy":
                iconId = R.drawable.ic_weather_cloudy;
                break;
            case "partly-cloudy-day":
                iconId = R.drawable.ic_weather_partly_cloudy_day;
                break;
            case "partly-cloudy-night":
                iconId = R.drawable.ic_weather_partly_cloudy_night;
                break;
        }
        viewHolder.imvSummary.setImageResource(iconId);

        if (checkF) {
            viewHolder.tvMaxTemp.setText(convertCF(foreCastDayList.get(i).getTemperatureHigh().toString()) + "ºF");
            viewHolder.tvMinTemp.setText(convertCF(foreCastDayList.get(i).getTemperatureLow().toString()) + "ºF");
        }
    }

    public String convertCF(String c) {
        Double aDouble = Double.parseDouble(c);
        Double bDouble = aDouble * 9 / 5 + 32;
        String result = String.valueOf(Math.round(bDouble));
        return result;
    }

    @Override
    public int getItemCount() {
        Log.d("GetAdapterSize", "getItemCount: " + foreCastDayList.size());
        return foreCastDayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime;
        private ImageView imvWet;
        private ImageView imvSummary;
        private TextView tvMinTemp;
        private TextView tvMaxTemp;
        private TextView tvWet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            imvWet = itemView.findViewById(R.id.imvWet);
            imvSummary = itemView.findViewById(R.id.imvSummary);
            tvMinTemp = itemView.findViewById(R.id.tvMinTemp);
            tvMaxTemp = itemView.findViewById(R.id.tvMaxTemp);
            tvWet = itemView.findViewById(R.id.tvWet);
        }
    }
}
