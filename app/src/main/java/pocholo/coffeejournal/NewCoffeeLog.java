package pocholo.coffeejournal;
import android.support.v7.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class NewCoffeeLog extends Activity implements OnClickListener  {

    //UI References
    private EditText brewTxt;
    private EditText roastTxt;

    private DatePickerDialog brewDatePickerDialog;
    private DatePickerDialog roastDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_coffee_log);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();

        setDateTimeField();
    }

    private void findViewsById() {
        brewTxt = (EditText) findViewById(R.id.editText4);
        brewTxt.setInputType(InputType.TYPE_NULL);
        brewTxt.requestFocus();

        roastTxt = (EditText) findViewById(R.id.editText5);
        roastTxt.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        brewTxt.setOnClickListener(this);
        roastTxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        brewDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                brewTxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        roastDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                roastTxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view == brewTxt) {
            brewDatePickerDialog.show();
        } else if(view == roastTxt) {
            roastDatePickerDialog.show();
        }
    }
}





