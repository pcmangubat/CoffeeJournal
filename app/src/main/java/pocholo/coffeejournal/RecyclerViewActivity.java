package pocholo.coffeejournal;


        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.firebase.client.Firebase;

        import butterknife.Bind;
        import butterknife.ButterKnife;
        import butterknife.OnClick;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

public class RecyclerViewActivity extends AppCompatActivity {

    @Bind(R.id.uxRecyclerView)
    public RecyclerView mRecyclerView;

    @OnClick(R.id.uxSearchButton)
    public void onSearchButton(View view){
     callGettyService(mSearchText.getText().toString());
    }

    @Bind(R.id.uxSearchText)
    public EditText mSearchText;


    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    GettyService gettyService;
    Firebase mFirebaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase("https://blinding-fire-804.firebaseio.com/").child("save");
        setContentView(R.layout.activity_recycler_view);
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

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        // TODO: Figure out how to get the item clicked
                        RecyclerAdapter adapter = (RecyclerAdapter)mRecyclerView.getAdapter();
                        ImagesModel.Image image = (ImagesModel.Image) adapter.getItem(position);
                        mFirebaseRef.child(image.id).child("id").setValue(image.id);
                        mFirebaseRef.child(image.id).child("uri").setValue(image.displaySizes.get(0).uri);

                        Snackbar.make(view, "Saved URI to Firebase", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(RecyclerViewActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        String BASE_URL = "https://api.gettyimages.com/v3/search/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gettyService = retrofit.create(GettyService.class);


    }

    public void callGettyService (String searchText)
    {
        Call<ImagesModel> call = gettyService.getImagesByPhrase(searchText);

        call.enqueue(new Callback<ImagesModel>() {
            @Override
            public void onResponse(Call<ImagesModel> call, Response<ImagesModel> response) {
                int statusCode = response.code();
                ImagesModel imagesModel = response.body();

                mAdapter = new RecyclerAdapter(RecyclerViewActivity.this, imagesModel.images);

                mRecyclerView.addItemDecoration(new DividerItemDecoration(RecyclerViewActivity.this, LinearLayoutManager.VERTICAL));
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<ImagesModel> call, Throwable t) {

            }
        });
    }

}
