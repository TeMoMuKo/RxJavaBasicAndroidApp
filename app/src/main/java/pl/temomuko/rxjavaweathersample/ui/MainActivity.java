package pl.temomuko.rxjavaweathersample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.temomuko.rxjavaweathersample.R;
import pl.temomuko.rxjavaweathersample.data.DataManager;
import pl.temomuko.rxjavaweathersample.data.model.WeatherResponse;
import pl.temomuko.rxjavaweathersample.util.AddressBuilderUtil;
import pl.temomuko.rxjavaweathersample.util.StringFormatterUtil;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.edit_text_location) EditText mEditTextLocation;
    @BindView(R.id.text_status) TextView mTextStatus;
    @BindView(R.id.image_weather_icon) ImageView mImageWeatherIcon;
    @BindView(R.id.text_place) TextView mTextPlace;
    @BindView(R.id.text_temperature) TextView mTextTemperature;
    @BindView(R.id.text_temperature_max) TextView mTextTemperatureMax;
    @BindView(R.id.text_temperature_min) TextView mTextTemperatureMin;
    @BindView(R.id.text_description) TextView mTextDescription;

    private static final int DEBOUNCE_MILLISECONDS = 500;

    private Subscription mEditTextSubscription;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();
        setSupportActionBar(mToolbar);
        subscribeToEditText();
    }

    @Override
    protected void onDestroy() {
        if (mEditTextSubscription != null) mEditTextSubscription.unsubscribe();
        super.onDestroy();
    }

    private void subscribeToEditText() {
        mEditTextSubscription = RxTextView.textChanges(mEditTextLocation)
                .filter(charSequence -> charSequence.length() > 0)
                .doOnNext(charSequence -> setRefreshingIndicator(true))
                .debounce(DEBOUNCE_MILLISECONDS, TimeUnit.MILLISECONDS)
                .switchMap(charSequence -> mDataManager.getCurrentWeatherWithObservable(charSequence.toString())
                        .flatMap(weatherResponse -> mDataManager.handleWeatherResponse(weatherResponse))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorResumeNext(throwable -> {
                            showApiError(throwable.getMessage());
                            return Observable.empty();
                        }))
                .subscribe(finalWeatherResponse -> {
                    setRefreshingIndicator(false);
                    showWeather(finalWeatherResponse);
                });
    }

    private void setRefreshingIndicator(boolean state) {
        mTextStatus.setText(state ? getString(R.string.loading) : null);
    }

    private void showWeather(WeatherResponse weatherResponse) {
        Picasso.with(this)
                .load(AddressBuilderUtil.getIconAddress(weatherResponse.getWeather().get(0).getIcon()))
                .fit()
                .centerCrop()
                .into(mImageWeatherIcon);
        mTextPlace.setText(StringFormatterUtil.getPlace(weatherResponse));
        mTextTemperature.setText(StringFormatterUtil.getTemperature(weatherResponse));
        mTextTemperatureMax.setText(StringFormatterUtil.getMaxTemperature(weatherResponse));
        mTextTemperatureMin.setText(StringFormatterUtil.getMinTemperature(weatherResponse));
        mTextDescription.setText(StringFormatterUtil.getDescription(weatherResponse));
    }

    private void showApiError(String apiError) {
        mTextStatus.setText(apiError);
    }
}
