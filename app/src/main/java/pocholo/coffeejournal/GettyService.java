package pocholo.coffeejournal;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GettyService {

    @Headers("Api-Key: eq6d2sg7sqb2mkcbnh3ch3g3")
    @GET("images")
    Call<ImagesModel> getImagesByPhrase(@Query("phrase") String phrase);
}
