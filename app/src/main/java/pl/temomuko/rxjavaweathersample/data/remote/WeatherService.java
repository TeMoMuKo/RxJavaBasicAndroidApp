package pl.temomuko.rxjavaweathersample.data.remote;

import pl.temomuko.rxjavaweathersample.data.model.WeatherResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Rafał Naniewicz on 28.04.2016.
 */
public interface WeatherService {
    @GET("data/2.5/weather")
    Observable<WeatherResponse> getCurrentWeatherWithObservable(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("appid") String apiKey
    );
}
