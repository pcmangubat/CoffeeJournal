package pocholo.coffeejournal;

import org.json.JSONObject;

import pocholo.coffeejournal.Model.WeatherData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherService {
    @GET("weather")
    Call<WeatherModel> getWeather(@Query("q") String city, @Query("appId") String appId);

    @GET("forecast")
    Call<WeatherData> getForecast (@Query("lat") String latitude,
                         @Query("lon") String longitude,
                         @Query("cnt") String cnt,
                         @Query("appid") String appid);

    @GET("forecast")
    Call<WeatherData> getForecast (@Query("q") String city,
                                   @Query("cnt") String cnt,
                                   @Query("appid") String appid);
}
