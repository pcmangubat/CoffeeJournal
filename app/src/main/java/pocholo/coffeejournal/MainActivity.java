package pocholo.coffeejournal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startNewCoffeeActivity(View view)
    {
        Intent intent = new Intent(this,NewCoffeeLog.class);
        int id =0;
        intent.putExtra("db_id",id);
        startActivity(intent);
    }
    public void startListCoffeeActivity(View view)
    {

    }
}
