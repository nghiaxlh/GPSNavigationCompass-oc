package com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.model.LatLng;
import com.pyxis.compass.gpscompassnavigationmap.R;
import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.forecast7day.ForeCastAdapter;
import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.forecast7day.interfaces.RestClient;
import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.hourtoday.adapter.ForeCastHourAdapter;
import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.hourtoday.modelhour.Currently;
import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.hourtoday.modelhour.DailyData;
import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.hourtoday.modelhour.ForeCastHourModel;
import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.hourtoday.modelhour.ForecastDailyModel;
import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.hourtoday.modelhour.HourlyData;
import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.modelweather.CurrentWeatherModel;
import com.pyxis.compass.gpscompassnavigationmap.utils.AdmobAdsUtils;
import com.pyxis.compass.gpscompassnavigationmap.utils.AdmobUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWeatherActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvName;
    private TextView tvTemp;
    private TextView tvSunrise;
    private TextView tvSunset;
    private ImageView ivBackWeather;
    private CurrentWeatherModel currentWeatherModel;

    private AddressUtils addressUtils;
    private String place;
    private GpsLocationService gpsLocationService;
    private LoadDataService loadDataService;
    private boolean isGpsEnabled;
    private boolean isNetworkEnabled;
    private ForeCastHourAdapter foreCastHourAdapter;
    private Location lastKnownLocation;
    private LocationManager locationManager;

    RecyclerView recyclerView;
    ForeCastAdapter foreCastAdapter;
    List<DailyData> foreCastDayList;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManagerHour;
    private double latitude;
    private double longitude;
    private List<HourlyData> hourlyDataList;
    private RecyclerView recyclerViewHour;
    TextView tvSummerNext7;
    ImageView imageViewCurrentSummary;
    TextView tvSummerNext24;
    private TextView tvCurrentSummary;
    private TextView tvDateToday;
//    private AdView nativeExpressAdView;
    private RelativeLayout rlAllWeather;
    private ProgressBar prBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_full);
//        ShowAdsInter();
        AdmobUtils.showInter(this);
        initView();
        initData();
        askPerLocation();
//        admobBannerUnit();

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.rvForecast);
        recyclerView.setLayoutManager(linearLayoutManager);
        getLocation();
        getForecast();
        getForeCastHour();
        getCurrentWeather();

        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(" HH:mm, E, dd MMM yyyy");
        String formattedDate = df.format(c);
        tvDateToday.setText(formattedDate);
    }

    private void ShowAdsInter() {
        AdmobAdsUtils.getSharedInstance().showInterstitialAd(new AdmobAdsUtils.AdCloseListener() {
            @Override
            public void onAdClosed() {
            }
        });
    }

    private void getCurrentWeather() {
        Call<CurrentWeatherModel> callWeather = RestClient.getApiInterFace().getCurrentWeather("ha noi", getResources().getString(R.string.API_WEATHER_KEY));
        callWeather.enqueue(new Callback<CurrentWeatherModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<CurrentWeatherModel> call, Response<CurrentWeatherModel> response) {
                currentWeatherModel = response.body();

                StringBuilder mTemp = new StringBuilder(String.format(Locale.US, "%d°", Math.round((currentWeatherModel.getMain().getTemp()))));

                tvTemp.setText(mTemp + "C");
                @SuppressLint("SimpleDateFormat") String mSunrise = new SimpleDateFormat("hh:mm a").format
                        (new Date(currentWeatherModel.getSys().getSunrise() * 1000));
                tvSunrise.setText("" + mSunrise);

                @SuppressLint("SimpleDateFormat") String mSunset = new SimpleDateFormat("hh:mm a").format
                        (new Date(currentWeatherModel.getSys().getSunset() * 1000));
                tvSunset.setText("" + mSunset);

                tvName.setText(place);
            }

            @Override
            public void onFailure(Call<CurrentWeatherModel> call, Throwable t) {
            }
        });
    }

    private void getForecast() {
        String currentLocation = latitude + "," + longitude;
        String excludeString = "minutely,flags,alerts,hourly";
        foreCastDayList = new ArrayList<>();
        if (foreCastDayList != null) {
            Call<ForecastDailyModel> callForecast = RestClient.getForeCastDaily().getForeCastDaily(currentLocation, "si", excludeString);
            callForecast.enqueue(new Callback<ForecastDailyModel>() {
                @Override
                public void onResponse(Call<ForecastDailyModel> call, Response<ForecastDailyModel> response) {
                    if (call != null && response != null && response.body().getDaily() != null && response.body().getCurrently() != null) {
                        foreCastDayList = response.body().getDaily().getData();
                        foreCastAdapter = new ForeCastAdapter(foreCastDayList, MyWeatherActivity.this, false);

                        recyclerView.setAdapter(foreCastAdapter);
                        tvSummerNext7.setText(response.body().getDaily().getSummary());

                        int iconId = 0;
                        switch (response.body().getCurrently().getIcon()) {
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
                        imageViewCurrentSummary.setImageResource(iconId);
                        tvCurrentSummary.setText(response.body().getCurrently().getSummary());
                    }
                }

                @Override
                public void onFailure(Call<ForecastDailyModel> call, Throwable t) {
                }
            });
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastKnownLocation != null) {
            longitude = lastKnownLocation.getLongitude();
            latitude = lastKnownLocation.getLatitude();
        }
    }

    private void getForeCastHour() {

        String currentLocation = latitude + "," + longitude;
        String excludeString = "currently,minutely,daily,flags,alerts";
        hourlyDataList = new ArrayList<>();
        Call<ForeCastHourModel> callForeCastModelHour = RestClient.getForeCastHour().getForeCast11(currentLocation, "si", excludeString);
        callForeCastModelHour.enqueue(new Callback<ForeCastHourModel>() {
            @Override
            public void onResponse(Call<ForeCastHourModel> call, Response<ForeCastHourModel> response) {

                if (call!=null && response.body()!=null) {
                    hourlyDataList = response.body().getHourly().getData();
                    Currently currently = response.body().getCurrently();
                    tvSummerNext24.setText(response.body().getHourly().getSummary());
                    foreCastHourAdapter = new ForeCastHourAdapter(MyWeatherActivity.this, hourlyDataList, false);

                    recyclerViewHour.setAdapter(foreCastHourAdapter);
                    recyclerViewHour.setLayoutManager(linearLayoutManagerHour);

                    prBar.setVisibility(View.GONE);
                    rlAllWeather.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ForeCastHourModel> call, Throwable t) {
            }
        });
    }

    public void initView() {
        tvName = findViewById(R.id.tv_city);
        tvSummerNext7 = findViewById(R.id.tvSummaryNext7);
        imageViewCurrentSummary = findViewById(R.id.imvCurrentWeather);
        tvName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvName.setSelected(true);
        tvName.setText("Wait...");
        recyclerViewHour = findViewById(R.id.rvForecastHour);
        tvTemp = findViewById(R.id.tv_temp);
        tvSunrise = findViewById(R.id.tv_sunrise);
        tvSunset = findViewById(R.id.tv_sunset);
        tvSummerNext24 = findViewById(R.id.tvSummaryNext24);
        tvCurrentSummary = findViewById(R.id.tvCurrentSummary);

        tvDateToday = findViewById(R.id.tv_date_today);
        ivBackWeather = findViewById(R.id.imBack);
        ivBackWeather.setOnClickListener(this);

        rlAllWeather = findViewById(R.id.rl_all_weather);
        prBar = findViewById(R.id.prBar);

//        nativeExpressAdView = findViewById(R.id.ads_banner_weather);

        recyclerViewHour.setLayoutManager(linearLayoutManagerHour);
        linearLayoutManagerHour = new LinearLayoutManager(MyWeatherActivity.this, LinearLayoutManager.HORIZONTAL, false);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public void askPerLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        isNetworkEnabled = netInfo != null && netInfo.isConnected();

        if (isGpsEnabled && isNetworkEnabled) {
            addressUtils = new AddressUtils(MyWeatherActivity.this);
            gpsLocationService = new GpsLocationService(MyWeatherActivity.this);
            if (loadDataService != null) {
                loadDataService.cancel(true);
            }
            if (gpsLocationService.getLocation() != null) {
                loadDataService = new LoadDataService(gpsLocationService.getLocation());
                loadDataService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imBack) {
            finish();
        }
    }

    public class LoadDataService extends AsyncTask<Void, Void, String> {
        private Location location;

        public LoadDataService(Location location) {
            this.location = location;
        }

        @Override
        protected String doInBackground(Void... voids) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            place = addressUtils.getLocationNameCompass(latLng);
            String QUERY = ApiKey.QUERY(location.getLatitude(), location.getLongitude());
            String str = "";
            HttpURLConnection connection = null;
            try {
                URL url = new URL(QUERY);
                connection =
                        (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(6000);
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    str += line;
                }
                reader.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return str;
        }
    }

    private void admobBannerUnit() {
        final AdView mAdView = new AdView(this);
        final AdRequest request = new AdRequest.Builder().build();
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(getString(R.string.banner_id_1));
//        nativeExpressAdView.addView(mAdView);
        mAdView.loadAd(request);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
