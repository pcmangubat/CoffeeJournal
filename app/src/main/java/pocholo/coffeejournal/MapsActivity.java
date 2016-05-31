package pocholo.coffeejournal;


        import android.graphics.Typeface;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.View;
        import android.widget.TextView;

        import com.google.android.gms.maps.CameraUpdate;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.CameraPosition;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;

        import butterknife.Bind;
        import butterknife.ButterKnife;
        import butterknife.OnClick;
        import android.widget.TextView;

        import org.json.JSONObject;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;

        import butterknife.Bind;
        import butterknife.ButterKnife;
        import okhttp3.ResponseBody;
        import pocholo.coffeejournal.Model.WeatherData;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng kenya = new LatLng(-1.283333, 36.816667);
    LatLng columbia = new LatLng(4.533333, -75.7);
    LatLng ehtiopia = new LatLng(6.161547, 38.202124);
    public List<Weather> weathers;
    String searchCity = "";

    @Bind(R.id.uxTextView)
    public TextView textView;

    TextView weather_report,place,weather_icon,country,icon_text;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String place_location="";
    private Typeface weatherFont;
    private final static String PATH_TO_WEATHER_FONT = "fonts/weather.ttf";
    private static OpenWeatherService openWeatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        openWeatherService = retrofit.create(OpenWeatherService.class);

        weather_icon=(TextView)findViewById(R.id.weather_icon);

        country=(TextView)findViewById(R.id.country);
        weatherFont = Typeface.createFromAsset(getAssets(), PATH_TO_WEATHER_FONT);
        weather_icon.setTypeface(weatherFont);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_id);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
       mRecyclerView.setLayoutManager(mLayoutManager);



        weather_report = (TextView) findViewById(R.id.weather_report);
        place =(TextView)findViewById(R.id.place);
    }

    @OnClick(R.id.uxEthiopia)
    public void onEthiopia(View view){
        searchCity = "ehtiopia";
        CameraPosition newCamPos = new CameraPosition(ehtiopia,
                10f,
                mMap.getCameraPosition().tilt, //use old tilt
                mMap.getCameraPosition().bearing); //use old bearing
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 1000, null);
        callWeatherService(searchCity);
    }

    @OnClick(R.id.uxColumbia)
    public void onColumbia(View view){
        searchCity = "columbia";
        CameraPosition newCamPos = new CameraPosition(columbia,
                10f,
                mMap.getCameraPosition().tilt, //use old tilt
                mMap.getCameraPosition().bearing); //use old bearing
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 2000, null);
        callWeatherService(searchCity);
    }

    @OnClick(R.id.uxKenya)
    public void onKenya(View view){
        searchCity = "kenya";
        CameraPosition newCamPos = new CameraPosition(kenya,
                10f,
                mMap.getCameraPosition().tilt, //use old tilt
                mMap.getCameraPosition().bearing); //use old bearing
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 2000, null);
        callWeatherService(searchCity);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(ehtiopia).title("Marker in e"));
        mMap.addMarker(new MarkerOptions().position(columbia).title("Marker in c"));
        mMap.addMarker(new MarkerOptions().position(kenya).title("Marker in k"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(ehtiopia));

        // Other supported types include: MAP_TYPE_NORMAL,
        // MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID and MAP_TYPE_NONE
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.
        //mMap.addMarker(new MarkerOptions()
        //        .icon(BitmapDescriptorFactory.fromResource(R.drawable.common_plus_signin_btn_icon_light))
        //        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
        //        .position(sydney));

        // mMap.animateCamera(CameraUpdateFactory.zoomIn());


/*
        Call<WeatherModel> object = openWeatherService.getWeather(searchCity,
                "4bfde779910aa08f5cd6e0b18587623e");

        object.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                int statusCode = response.code();
                ResponseBody rb = response.errorBody();
                String msg = response.message();
                okhttp3.Response r = response.raw();
                WeatherModel responseBody = response.body();

               // textView.setText(responseBody.toString());
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                // Log error here since request failed
               // textView.setText("ERROR:" + t.getMessage());
            }
        });
      */


    }

    public  void callWeatherService(String searchCity) {
        Call<WeatherData> object1 = openWeatherService.getForecast(searchCity, "5",
                "4bfde779910aa08f5cd6e0b18587623e");

        object1.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                int statusCode = response.code();
                ResponseBody rb = response.errorBody();
                String msg = response.message();
                okhttp3.Response r = response.raw();
                WeatherData responseBody = response.body();

                //textView.setText(responseBody.toString());

                weather_report.setText(responseBody.getList().get(0).getWeather().get(0).getDescription());
                place.setText(responseBody.getCity().getName());
                country.setText(responseBody.getCity().getCountry());
                place_location = responseBody.getCity().getName();


                Log.w("icon", responseBody.getList().get(0).getWeather().get(0).getIcon());
                switch (responseBody.getList().get(0).getWeather().get(0).getIcon()) {
                    case "01d":
                        weather_icon.setText(R.string.wi_day_sunny);
                        break;
                    case "02d":
                        weather_icon.setText(R.string.wi_cloudy_gusts);
                        break;
                    case "03d":
                        weather_icon.setText(R.string.wi_cloud_down);
                        break;
                    case "04d":
                        weather_icon.setText(R.string.wi_cloudy);
                        break;
                    case "04n":
                        weather_icon.setText(R.string.wi_night_cloudy);
                        break;
                    case "10d":
                        weather_icon.setText(R.string.wi_day_rain_mix);
                        break;
                    case "11d":
                        weather_icon.setText(R.string.wi_day_thunderstorm);
                        break;
                    case "13d":
                        weather_icon.setText(R.string.wi_day_snow);
                        break;
                    case "01n":
                        weather_icon.setText(R.string.wi_night_clear);
                        break;
                    case "02n":
                        weather_icon.setText(R.string.wi_night_cloudy);
                        break;
                    case "03n":
                        weather_icon.setText(R.string.wi_night_cloudy_gusts);
                        break;
                    case "10n":
                        weather_icon.setText(R.string.wi_night_cloudy_gusts);
                        break;
                    case "11n":
                        weather_icon.setText(R.string.wi_night_rain);
                        break;
                    case "13n":
                        weather_icon.setText(R.string.wi_night_snow);
                        break;


                }
                String[] humidity = new String[10];
                String[] rain_description = new String[10];
                String[] icon = new String[10];
                String[] time = new String[10];
                weathers = new ArrayList<>();
                for (int i = 0; i < responseBody.getList().size(); i++) {
                    humidity[i] = String.valueOf(responseBody.getList().get(i).getMain().getHumidity());
                    rain_description[i] = String.valueOf(responseBody.getList().get(i).getWeather().get(0).getDescription());
                    icon[i] = String.valueOf(responseBody.getList().get(i).getWeather().get(0).getIcon());
                    time[i] = String.valueOf(responseBody.getList().get(i).getDt());

                    Log.w("humidity", humidity[i]);
                    Log.w("rain_description", rain_description[i]);
                    Log.w("icon", icon[i]);
                    Log.w("time", time[i]);

                    weathers.add(new Weather(String.valueOf(responseBody.getList().get(i).getWeather().get(0).getIcon()), String.valueOf(responseBody.getList().get(i).getMain().getHumidity()), String.valueOf(responseBody.getList().get(i).getWeather().get(0).getDescription()), String.valueOf(responseBody.getList().get(i).getDt())));

                }
                mAdapter = new WeatherAdapter(weathers, weatherFont);
                mRecyclerView.setAdapter(mAdapter);


            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                // Log error here since request failed
                // textView.setText("ERROR:" + t.getMessage());
            }
        });
    }
}