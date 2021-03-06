package pl.temomuko.rxjavaweathersample;

/**
 * Created by Rafał Naniewicz on 24.01.2016.
 */
public final class Constants {

    private Constants() {
        throw new AssertionError();
    }

    public static final String API_BASE_URL = "http://api.openweathermap.org/";
    public static final String ICON_BASE_URL = "http://openweathermap.org/img/w/";
    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String UNITS = "metric";

}
