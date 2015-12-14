package pocholo.coffeejournal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private DBCoffeeLogHelper mydb;
    private SimpleCursorAdapter dataAdapter;
    AlertDialog.Builder alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alert = new AlertDialog.Builder(this);

        mydb = new DBCoffeeLogHelper(this);

        Cursor cursor = mydb.getCoffeeLogsCursor();

        // The desired columns to be bound
        String[] columns = new String[] {
                DBCoffeeLogHelper.COFFEELOG_COLUMN_ID,
                DBCoffeeLogHelper.COFFEELOG_COLUMN_NAME,
                DBCoffeeLogHelper.COFFEELOG_COLUMN_ROASTER,
                DBCoffeeLogHelper.COFFEELOG_COLUMN_COUNTRY,
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.ID,
                R.id.Name,
                R.id.Roaster,
                R.id.Country,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.coffeelog_details,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                long id_To_Search  = cursor.getLong(cursor.getColumnIndexOrThrow(DBCoffeeLogHelper.COFFEELOG_COLUMN_ID));
                //Toast.makeText(getApplicationContext(),countryCode, Toast.LENGTH_SHORT).show();

                Bundle dataBundle = new Bundle();
                dataBundle.putLong(DBCoffeeLogHelper.COFFEELOG_COLUMN_ID, id_To_Search);

                Intent intent = new Intent(getApplicationContext(), NewCoffeeLog.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"ID:"+id_To_Search, Toast.LENGTH_SHORT).show();
            }
        });

        listView.setLongClickable(true);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                Cursor cursor = (Cursor) arg0.getItemAtPosition(pos);

                final long id_To_Search  = cursor.getLong(cursor.getColumnIndexOrThrow(DBCoffeeLogHelper.COFFEELOG_COLUMN_ID));
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

        /*
        ArrayList<CoffeeLog> array_list = new ArrayList<CoffeeLog>(Arrays.asList(mydb.getCoffeeLogs()));
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

        listView = (ListView)findViewById(R.id.listView1);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                int id_To_Search = (int) arg3;

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent(getApplicationContext(), NewCoffeeLog.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
        */
    }
    public void startNewCoffeeActivity(View view)
    {
        Intent intent = new Intent(this,NewCoffeeLog.class);
        int id =0;
        intent.putExtra(DBCoffeeLogHelper.COFFEELOG_COLUMN_ID,id);
        startActivity(intent);
    }
    public void startListCoffeeActivity(View view)
    {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case R.id.item1:
                Bundle dataBundle = new Bundle();
                dataBundle.putInt(DBCoffeeLogHelper.COFFEELOG_COLUMN_ID, 0);

                Intent intent = new Intent(getApplicationContext(),NewCoffeeLog.class);
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
