package pocholo.coffeejournal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pocholo.coffeejournal.Model.SavedImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bebe on 5/31/2016.
 */
public class RecyclerSaveViewActivity extends AppCompatActivity {
    private static final String TAG = RecyclerSaveViewActivity.class.getName();
    @Bind(R.id.uxRecyclerView)
    public RecyclerView mRecyclerView;




    RecyclerAdapterView mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    GettyService gettyService;
    Firebase mFirebaseRef;
    private List<SavedImage> mSavedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase("https://blinding-fire-804.firebaseio.com/").child("save");
        setContentView(R.layout.activity_recycler_view);


        findViewById(R.id.uxSearchText).setVisibility(View.GONE);
        findViewById(R.id.uxSearchButton).setVisibility(View.GONE);
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
/*
        mRecyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        // TODO: Figure out how to get the item clicked
                        RecyclerAdapter adapter = (RecyclerAdapter)mRecyclerView.getAdapter();
                        ImagesModel.Image image = (ImagesModel.Image) adapter.getItem(position);
                       // mFirebaseRef.child(image.id).setValue(image.displaySizes.get(0).uri);
                        //Snackbar.make(view, "Saved URI to Firebase", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(RecyclerSaveViewActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

*/
        mSavedImage = new ArrayList<>();
        mAdapter = new RecyclerAdapterView(RecyclerSaveViewActivity.this,mSavedImage);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(RecyclerSaveViewActivity.this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://blinding-fire-804.firebaseio.com/save");

        mFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try{

                        SavedImage model = dataSnapshot.getValue(SavedImage.class);

                        mSavedImage.add(model);
                        mRecyclerView.scrollToPosition(mSavedImage.size() - 1);
                        mAdapter.notifyItemInserted(mSavedImage.size() - 1);
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}