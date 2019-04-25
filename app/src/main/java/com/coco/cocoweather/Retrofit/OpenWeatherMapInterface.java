package com.coco.cocoweather.Retrofit;

import com.coco.cocoweather.Model.Forecast.WeatherForecastResultModel;
import com.coco.cocoweather.Model.Today.WeatherResultModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Abhishek Singh on 23/4/19.
 */
public interface OpenWeatherMapInterface {

    @GET("weather")
    Observable<WeatherResultModel> getWeatherByLatLon(@Query("lat") String lat,
                                                      @Query("lon") String lon,
                                                      @Query("appid") String appid,
                                                      @Query("units") String unit);

    @GET("weather")
    Observable<WeatherResultModel> getWeatherByCityName(@Query("q") String cityName,
                                                      @Query("appid") String appid,
                                                      @Query("units") String unit);

    @GET("forecast")
    Observable<WeatherForecastResultModel> getForecastWeatherByLatLon(@Query("lat") String lat,
                                                                      @Query("lon") String lon,
                                                                      @Query("appid") String appid,
                                                                      @Query("units") String unit);
}
