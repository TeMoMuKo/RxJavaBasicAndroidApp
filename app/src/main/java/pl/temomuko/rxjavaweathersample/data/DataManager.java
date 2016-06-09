package pl.temomuko.rxjavaweathersample.data;

import pl.temomuko.rxjavaweathersample.Constants;
import pl.temomuko.rxjavaweathersample.data.model.WeatherResponse;
import pl.temomuko.rxjavaweathersample.data.remote.ApiManager;
import pl.temomuko.rxjavaweathersample.data.remote.WeatherResponseApiException;
import rx.Observable;

/**
 * Created by Rafał Naniewicz on 09.06.2016.
 */
public class DataManager {

    private static DataManager sInstance;
    private ApiManager mApiManager;

    public static DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }

    private DataManager() {
        mApiManager = ApiManager.getInstance();
    }

    public Observable<WeatherResponse> getCurrentWeatherWithObservable(String cityName) {
        return mApiManager.getWeatherService().getCurrentWeatherWithObservable(cityName, Constants.UNITS, Constants.API_KEY);
    }

    public Observable<WeatherResponse> handleWeatherResponse(WeatherResponse weatherResponse) {
        if (weatherResponse.getCode() == 200) {
            return Observable.just(weatherResponse);
        } else {
            return Observable.error(new WeatherResponseApiException(weatherResponse.getMessage()));
        }
    }
}
