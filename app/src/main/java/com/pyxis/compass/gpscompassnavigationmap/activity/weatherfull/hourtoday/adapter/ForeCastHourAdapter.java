package com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.hourtoday.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pyxis.compass.gpscompassnavigationmap.R;
import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.hourtoday.modelhour.HourlyData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ForeCastHourAdapter extends RecyclerView.Adapter<ForeCastHourAdapter.ViewHolder> {
    Context context;
    List<HourlyData> hourlyDataList;
    boolean checkF = false;

    public ForeCastHourAdapter(Context context, List<HourlyData> hourlyDataList,boolean isF) {
        this.context = context;
        this.hourlyDataList = hourlyDataList;
        this.checkF = isF;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weather_24hour, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        StringBuilder stringBuilder=new StringBuilder(hourlyDataList.get(i).getTemperature().toString());
        stringBuilder.delete(2,5);

        viewHolder.tvTime.setText(new SimpleDateFormat("HH:mm").format(new Date(Long.valueOf(hourlyDataList.get(i).getTime() * 1000))));
        viewHolder.tvTempHour.setText(stringBuilder+"ºC");

        int iconId = 0;
        switch (hourlyDataList.get(i).getIcon()) {
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
        viewHolder.imvSummaryHour.setImageResource(iconId);
        if (checkF) {
            viewHolder.tvTempHour.setText(convertCF(hourlyDataList.get(i).getTemperature().toString())+"ºF");
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
        Log.d("sizeHourAdapter", "getItemCount: "+hourlyDataList.size());
        return hourlyDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imvSummaryHour;
        public TextView tvTempHour;
        public TextView tvTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imvSummaryHour = itemView.findViewById(R.id.imvSummaryHour);
            tvTempHour = itemView.findViewById(R.id.tvTempHour);
            tvTime = itemView.findViewById(R.id.tvTime);

        }
    }
}
