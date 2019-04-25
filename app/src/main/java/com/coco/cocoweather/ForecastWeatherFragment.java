package com.coco.cocoweather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.coco.cocoweather.Adapter.WeatherForecastAdapter;
import com.coco.cocoweather.Common.Common;
import com.coco.cocoweather.Model.Forecast.WeatherForecastResultModel;
import com.coco.cocoweather.Model.Today.WeatherResultModel;
import com.coco.cocoweather.Retrofit.OpenWeatherMapInterface;
import com.coco.cocoweather.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastWeatherFragment extends Fragment {

    CompositeDisposable compositeDisposable;
    OpenWeatherMapInterface service;

    TextView txt_city_name;
    TextView txt_geo_coord;

    RecyclerView recycler_forecast;

    static ForecastWeatherFragment instance;

    public static ForecastWeatherFragment getInstance(){
        if (instance == null){
            instance = new ForecastWeatherFragment();
        }
        return instance;
    }


    public ForecastWeatherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        service = retrofit.create(OpenWeatherMapInterface.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_forecast_weather, container, false);

        txt_city_name = itemView.findViewById(R.id.txt_city_name);
        txt_geo_coord = itemView.findViewById(R.id.txt_geo_coord);

        recycler_forecast = itemView.findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

        getForecastWeatherInformation();
        return itemView;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    private void getForecastWeatherInformation() {
        compositeDisposable.add(service.getForecastWeatherByLatLon(
                String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResultModel>() {
                    @Override
                    public void accept(WeatherForecastResultModel weatherForecastResultModel) throws Exception {
                        displayForecastweather(weatherForecastResultModel);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("error", ""+throwable.getMessage());
                    }
                })

        );
    }

    private void displayForecastweather(WeatherForecastResultModel weatherForecastResultModel) {
        txt_city_name.setText(new StringBuilder(weatherForecastResultModel.getCity().getName()));
        txt_geo_coord.setText(new StringBuilder(weatherForecastResultModel.getCity().getCoord().toString()));

        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getContext(), weatherForecastResultModel);
        recycler_forecast.setAdapter(adapter);

    }

}
