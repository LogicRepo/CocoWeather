package com.coco.cocoweather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coco.cocoweather.Common.Common;
import com.coco.cocoweather.Model.Today.WeatherResultModel;
import com.coco.cocoweather.Retrofit.OpenWeatherMapInterface;
import com.coco.cocoweather.Retrofit.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment {

    private List<String> lstCities;
    private MaterialSearchBar searchBar;

    ImageView img_weather;
    TextView txt_city_name;
    TextView txt_humidity;
    TextView txt_sunrise;
    TextView txt_sunset;
    TextView txt_pressure;
    TextView txt_temperature;
    TextView txt_description;
    TextView txt_date_time;
    TextView txt_wind;
    TextView txt_geo_coord;

    LinearLayout weather_panel;
    ProgressBar loading;

    CompositeDisposable compositeDisposable;
    OpenWeatherMapInterface service;

    static CityFragment instance;

    public static CityFragment getInstance(){
        if (instance == null){
            instance = new CityFragment();
        }
        return instance;
    }


    public CityFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        service = retrofit.create(OpenWeatherMapInterface.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_city, container, false);

        img_weather = itemView.findViewById(R.id.img_weather);
        txt_city_name = itemView.findViewById(R.id.txt_city_name);
        txt_humidity = itemView.findViewById(R.id.txt_humidity);
        txt_sunrise = itemView.findViewById(R.id.txt_sunrise);
        txt_sunset = itemView.findViewById(R.id.txt_Sunset);
        txt_pressure = itemView.findViewById(R.id.txt_pressure);
        txt_temperature = itemView.findViewById(R.id.txt_temperature);
        txt_description = itemView.findViewById(R.id.txt_description);
        txt_date_time = itemView.findViewById(R.id.txt_date_time);
        txt_wind = itemView.findViewById(R.id.txt_wind);
        txt_geo_coord = itemView.findViewById(R.id.txt_geo_coord);

        weather_panel = itemView.findViewById(R.id.weather_panel);
        loading = itemView.findViewById(R.id.loading);

        searchBar = itemView.findViewById(R.id.search_bar);
        searchBar.setEnabled(false);

        new LoadCities().execute();//Async task class to load cities;

        //getWeatherInformation();

        return itemView;
    }

    private class LoadCities extends SimpleAsyncTask<List<String>> {

        @Override
        protected List<String> doInBackgroundSimple() {
            lstCities = new ArrayList<>();
            try {

                StringBuilder stringBuilder = new StringBuilder();
                InputStream inputStream = getResources().openRawResource(R.raw.city_list);
                GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

                InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String readed;
                while ((readed = bufferedReader.readLine()) != null)
                    stringBuilder.append(readed);
                lstCities = new Gson().fromJson(stringBuilder.toString(), new TypeToken<List<String>>(){}.getType());

            }catch (IOException e){
                e.printStackTrace();
            }
            return lstCities;
        }


        @Override
        protected void onSuccess(final List<String> lstCity) {
            super.onSuccess(lstCity);

            searchBar.setEnabled(true);
            searchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    List<String> suggest = new ArrayList<>();
                    for (String search : lstCity){
                        if (search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                            suggest.add(search);
                    }
                    searchBar.setLastSuggestions(suggest);


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {

                }

                @Override
                public void onSearchConfirmed(CharSequence text) {

                    getWeatherInformation(text.toString());

                    searchBar.setLastSuggestions(lstCity);

                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });

            searchBar.setLastSuggestions(lstCity);

            loading.setVisibility(View.GONE);
        }


    }

    private void getWeatherInformation(String cityName) {
        compositeDisposable.add(service.getWeatherByCityName(
                cityName,
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResultModel>(){
                    @Override
                    public void accept(WeatherResultModel weatherResultModel) throws Exception {
                        //Load Image
                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                                .append(weatherResultModel.getWeather().get(0).getIcon())
                                .append(".png").toString()).into(img_weather);

                        txt_city_name.setText(weatherResultModel.getName());
                        txt_description.setText(new StringBuilder("Weather in")
                                .append(weatherResultModel.getName()).toString());
                        txt_temperature.setText(new StringBuilder(String.valueOf(
                                weatherResultModel.getMain().getTemp()))
                                .append("°C").toString());
                        txt_date_time.setText(Common.convertUnixToDate(weatherResultModel.getDt()));
                        txt_pressure.setText(new StringBuilder(String.valueOf(weatherResultModel.getMain().getPressure())).append("hpa").toString());
                        txt_humidity.setText(new StringBuilder(new StringBuilder(String.valueOf(weatherResultModel.getMain().getHumidity()))
                                .append(" %").toString()));
                        txt_sunrise.setText(Common.convertUnixToHour(weatherResultModel.getSys().getSunrise()));
                        txt_sunset.setText(Common.convertUnixToHour(weatherResultModel.getSys().getSunset()));
                        txt_geo_coord.setText(new StringBuilder(weatherResultModel.getCoord().toString()));

                        //Display Panel
                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);

                    }
                }, new Consumer<Throwable>(){
                    @Override
                    public void accept(Throwable throwable) {
                        Toast.makeText(getActivity(),""+throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }));
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
}
