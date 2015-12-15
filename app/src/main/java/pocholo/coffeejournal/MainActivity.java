package pocholo.coffeejournal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private DBCoffeeLogHelper mydb;

    AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alert = new AlertDialog.Builder(this);

        mydb = new DBCoffeeLogHelper(this);
        ArrayList<CoffeeLog> coffeeLogs = new ArrayList<>(Arrays.asList(mydb.getCoffeeLogs()));
        CoffeeLogAdapter dataAdapter = new CoffeeLogAdapter(this,coffeeLogs);
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
                Cursor cursor = (Cursor) arg0.getItemAtPosition(pos);

                final long id_To_Search = cursor.getLong(cursor.getColumnIndexOrThrow(DBCoffeeLogHelper.COFFEELOG_COLUMN_ID));
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
}
