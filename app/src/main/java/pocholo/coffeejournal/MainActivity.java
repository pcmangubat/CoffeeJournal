package pocholo.coffeejournal;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private DBCoffeeLogHelper mydb;

    AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // 5. Check to see if we have a camera hardware
        PackageManager packageManager = getPackageManager();
        boolean hasCamera = false;
        StringBuilder sb = new StringBuilder();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            sb.append("\nDevice has camera");
            hasCamera = true;
        } else {
            sb.append("\nDevice has no camera");
            hasCamera = false;
        }

        final TextView cameraText = new TextView(this);
        // Display information in TextView
        cameraText.setText(sb.toString());
        LinearLayout mainPage = (LinearLayout)findViewById(R.id.mainPage);
        mainPage.addView(cameraText);
        try {
            Resources res = getResources();
            InputStream inputStreamRaw = res.openRawResource(R.raw.raw);

            byte[] bufferRaw = new byte[inputStreamRaw.available()];
            inputStreamRaw.read(bufferRaw);
            inputStreamRaw.close();

            ((TextView) findViewById(R.id.rawText)).setText( new String(bufferRaw));


            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("asset.txt");

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            String text = new String(buffer);
            TextView assetText = (TextView)findViewById(R.id.assetText);
            assetText.setText(text);


        } catch (IOException e) {
            e.printStackTrace();
        }


        alert = new AlertDialog.Builder(this);

        mydb = new DBCoffeeLogHelper(this);
        ArrayList<CoffeeLog> coffeeLogs = new ArrayList<>(Arrays.asList(mydb.getCoffeeLogs()));
        CoffeeLogAdapter dataAdapter = new CoffeeLogAdapter(this, coffeeLogs);
        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                CoffeeLog coffeeLog = (CoffeeLog) listView.getItemAtPosition(position);

                //long id_To_Search = cursor.getLong(cursor.getColumnIndexOrThrow(DBCoffeeLogHelper.COFFEELOG_COLUMN_ID));
                long id_To_Search = coffeeLog.Id;
                //Toast.makeText(getApplicationContext(),countryCode, Toast.LENGTH_SHORT).show();

                Bundle dataBundle = new Bundle();
                dataBundle.putLong(DBCoffeeLogHelper.COFFEELOG_COLUMN_ID, id_To_Search);

                Intent intent = new Intent(getApplicationContext(), NewCoffeeLog.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "ID:" + id_To_Search, Toast.LENGTH_SHORT).show();
            }
        });

        listView.setLongClickable(true);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                CoffeeLog coffeeLog = (CoffeeLog) arg0.getItemAtPosition(pos);

                final long id_To_Search = coffeeLog.Id;
//                    Log.v("long clicked","pos"+" "+pos);
                alert.setTitle("Confirm Delete");
                // set dialog message
                alert
                        .setMessage("Do you want to DELETE this entry?")
                        .setCancelable(false)
                        .setPositiveButton("Do it!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity

                                mydb.deleteCoffeeLog(id_To_Search);
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);

                            }
                        })
                        .setNegativeButton("I need this", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alert.create();

                // show it
                alertDialog.show();

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.item1:
                Bundle dataBundle = new Bundle();
                dataBundle.putInt(DBCoffeeLogHelper.COFFEELOG_COLUMN_ID, 0);

                Intent intent = new Intent(getApplicationContext(), NewCoffeeLog.class);
                intent.putExtras(dataBundle);

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
