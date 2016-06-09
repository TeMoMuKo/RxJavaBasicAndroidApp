package pl.temomuko.rxjavaweathersample.data.remote;

import com.google.gson.GsonBuilder;

import pl.temomuko.rxjavaweathersample.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rafał Naniewicz on 09.06.2016.
 */
public class ApiManager {

    private static ApiManager sInstance;
    private WeatherService mWeatherService;

    public static ApiManager getInstance() {
        if (sInstance == null) {
            sInstance = new ApiManager();
        }
        return sInstance;
    }

    private ApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mWeatherService = retrofit.create(WeatherService.class);
    }

    public WeatherService getWeatherService() {
        return mWeatherService;
    }
}
