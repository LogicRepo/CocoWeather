package com.coco.cocoweather.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coco.cocoweather.Common.Common;
import com.coco.cocoweather.Model.Forecast.WeatherForecastResultModel;
import com.coco.cocoweather.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Abhishek Singh on 25/4/19.
 */
public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder> {

    Context context;
    WeatherForecastResultModel weatherForecastResultModel;

    public WeatherForecastAdapter(Context context, WeatherForecastResultModel weatherForecastResultModel) {
        this.context = context;
        this.weatherForecastResultModel = weatherForecastResultModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_weather_forecast,parent,false);
        return new  MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Load icon
        //Load Image
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResultModel.getList().get(position).getWeather().get(0).getIcon())
                .append(".png").toString()).into(holder.img_weather);

        holder.txt_date_time.setText(new StringBuilder(
                Common.convertUnixToDate(weatherForecastResultModel.getList().get(position).getDt())));

        holder.txt_description.setText(new StringBuilder(
                weatherForecastResultModel.getList().get(position).getWeather().get(0).getDescription()));

        holder.txt_temperature.setText(new StringBuilder(String.valueOf(
                weatherForecastResultModel.getList().get(position).getMain().getTemp())).append("°C"));



    }

    @Override
    public int getItemCount() {
        return weatherForecastResultModel.getList().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_date_time;
        TextView txt_description;
        TextView txt_temperature;
        ImageView img_weather;

        public MyViewHolder(View itemView) {
            super(itemView);

            img_weather = itemView.findViewById(R.id.img_weather);
            txt_date_time = itemView.findViewById(R.id.txt_date);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_temperature = itemView.findViewById(R.id.txt_temperature);
        }
    }
}
