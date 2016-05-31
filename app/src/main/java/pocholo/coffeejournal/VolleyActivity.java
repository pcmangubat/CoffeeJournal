/*
package pocholo.coffeejournal;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TimingLogger;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VolleyActivity extends AppCompatActivity {

    @Bind(R.id.uxTextView)
    public TextView mTextView;

    @Bind(R.id.uxImageView)
    ImageView mImageView;

    @Bind(R.id.uxNetworkImageView)
    NetworkImageView mNetworkImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final TimingLogger timingLogger = new TimingLogger("Volley", "request");

        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(this, new OkHttpStack(new OkHttpClient()));
        String url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        timingLogger.addSplit("before add RequestQueue");

        requestQueue.add(stringRequest);

        timingLogger.addSplit("after add RequestQueue");
        Log.v("Volley", "addRequest");

        url = "http://i.imgur.com/7spzG.png";

        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Log.v("Volley", "onResponse");
                        timingLogger.addSplit("onResponse");
                        mImageView.setImageBitmap(bitmap);
                        timingLogger.addSplit("setImageBitmap");

                        timingLogger.dumpToLog();
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        mImageView.setImageResource(R.drawable.fab_background);
                    }
                });

        requestQueue.add(imageRequest);

        //Picasso.with(this)
        //        .load("http://i.imgur.com/DvpvklR.png")
        //        .into(mImageView);

        // Get the ImageLoader through your singleton class.
        ImageLoader mImageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        mImageLoader.get(url, ImageLoader.getImageListener(mImageView,
                R.drawable.abc_btn_default_mtrl_shape, R.drawable.fab_background));

        mNetworkImageView.setImageUrl(url, mImageLoader);

        url = "https://api.random.org/json-rpc/1/invoke";

        String json = "{\n" +
                "\"jsonrpc\": \"2.0\",\n" +
                "\"method\": \"generateIntegers\",\n" +
                "\"params\": {\n" +
                "    \"apiKey\": \"fdb7ff0d-b1cb-4a30-8743-22575e20be9b\",\n" +
                "    \"n\": 6,\n" +
                "    \"min\": 1,\n" +
                "    \"max\": 49,\n" +
                "    \"replacement\": true\n" +
                "},\n" +
                "\"id\": \"111\"\n" +
                "}";

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                mTextView.setText("Response: " + response.toString());
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("ERROR: " + error.toString());
            }
        } );
        requestQueue.add(jsonObjectRequest);
    }
}
*/