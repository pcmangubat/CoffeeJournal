package pocholo.coffeejournal;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

    public class NewCoffeeLog extends AppCompatActivity implements OnClickListener {


    //UI References
    //private EditText brewTxt;
    @Bind(R.id.editTextRoastDate)EditText brewTxt;
    private EditText roastTxt;
    private DatePickerDialog brewDatePickerDialog;
    private DatePickerDialog roastDatePickerDialog;
    private RadarChart radarChart;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
    private ArrayList<RadioGroup> rgTasteProfileList;
    private ArrayList<TextView> tasteTextViews;
    private ArrayList<View> tasteViews;
    private DBCoffeeLogHelper mydb;
    private long db_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_coffee_log);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        // set id if 0 then new record, > 0 means record from database and just update
        db_id = 0;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            db_id = bundle.getLong(DBCoffeeLogHelper.COFFEELOG_COLUMN_ID);
        }
        if (db_id > 0){
            ((TextView)findViewById(R.id.textNewCoffeeLogTitle)).setText(R.string.coffee_title_update);
        } else {
            ((TextView)findViewById(R.id.textNewCoffeeLogTitle)).setText(R.string.coffee_title_new);
        }

        // Create reuse layout
        tasteTextViews = new ArrayList<>();
        tasteViews = new ArrayList<>();
        //ViewGroup myLayout = (ViewGroup)findViewById(R.id.tasteOptions);
        LinearLayout myLayout = (LinearLayout)findViewById(R.id.tasteOptions);

        //Sweet
        View sweetView = LayoutInflater.from(this).inflate(R.layout.taste_rating, myLayout,false);
        TextView sweetText = (TextView)sweetView.findViewById(R.id.textTaste);
        sweetText.setText( getResources().getString(R.string.sweet));
        RadioGroup radioGroupSweet = (RadioGroup)sweetView.findViewById(R.id.radioGroupTaste);
        radioGroupSweet.setId(sweetText.generateViewId());
        myLayout.addView(sweetView);

        //Sour
        View sourView = LayoutInflater.from(this).inflate(R.layout.taste_rating, myLayout, false);
        TextView sourText = (TextView)sourView.findViewById(R.id.textTaste);
        sourText.setText( getResources().getString(R.string.sour));
        RadioGroup radioGroupSour = (RadioGroup)sourView.findViewById(R.id.radioGroupTaste);
        radioGroupSour.setId(sourView.generateViewId());
        myLayout.addView(sourView);

        //Floral
        View floralView = getLayoutInflater().inflate(R.layout.taste_rating, myLayout, false);
        TextView floralText = (TextView)floralView.findViewById(R.id.textTaste);
        floralText.setText( getResources().getString(R.string.floral));
        RadioGroup radioGroupFloral = (RadioGroup)floralView.findViewById(R.id.radioGroupTaste);
        radioGroupFloral.setId(floralView.generateViewId());
        myLayout.addView(floralView);
        // Create DB Helper
        mydb = new DBCoffeeLogHelper(this);

        // Load List with Radio Groups
        rgTasteProfileList = new ArrayList<>();
        //rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupSweet));
        //rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupSour));
        rgTasteProfileList.add(radioGroupSweet);
        rgTasteProfileList.add(radioGroupSour);
        rgTasteProfileList.add(radioGroupFloral);
        //rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupFloral));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupSpicy));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupSalty));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupBerry));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupCitrus));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupStoreFruit));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupChocolate));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupCaramel));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupSmokey));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupBitter));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupSavory));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupBody));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupClean));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupFinish));

        // For calendar stuff

        findViewsById();
        setDateTimeField();

        // stop on click listeners
        stopOnClickListeners();


        if (db_id > 0) {
            // Update all fields
            CoffeeLog coffeeLog = mydb.getCoffeeLog(db_id);
            UpdateCoffeeLogView(coffeeLog);
        }


        // Initialize Radar Chart
        radarChart = (RadarChart) findViewById(R.id.chart);
        if (radarChart != null) {
            RadarData data = new RadarData(getXAxisValues(), getDataSet());
            radarChart.setData(data);
            radarChart.setDescription("TASTE PROFILE");
            radarChart.getYAxis().setAxisMinValue(1);
            radarChart.getYAxis().setAxisMaxValue(5);
            radarChart.animateXY(2000, 2000);
            radarChart.setWebColor(Color.GRAY);
            radarChart.setDrawWeb(false);

            // Set Background Color to Overall Rating
            RadioGroup rgOverall = (RadioGroup) findViewById(R.id.radioGroupOverall);

            RadioButton rbOverall = (RadioButton) findViewById(rgOverall.getCheckedRadioButtonId());
            if (rbOverall == null) {
                radarChart.setBackgroundColor(Color.TRANSPARENT);
            } else if (rbOverall.getText().equals("1")) {
                radarChart.setBackgroundColor(Color.RED);
            } else if (rbOverall.getText().equals("2")) {
                radarChart.setBackgroundColor(Color.MAGENTA);
            } else if (rbOverall.getText().equals("3")) {
                radarChart.setBackgroundColor(Color.YELLOW);
            } else if (rbOverall.getText().equals("4")) {
                radarChart.setBackgroundColor(Color.GREEN);
            } else if (rbOverall.getText().equals("5")) {
                radarChart.setBackgroundColor(Color.CYAN);
            }

            // Hide Legends
            Legend legend = radarChart.getLegend();
            legend.setEnabled(false);
            radarChart.invalidate();

            // Attach adapter for AutoComplete Country
            AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.editTextCountry);
            // Get the string array
            String[] countries = getResources().getStringArray(R.array.countries_array);
            // Create the adapter and set it to the AutoCompleteTextView
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countries);
            textView.setAdapter(adapter);
        }
        startOnClickListeners();
    }

    private void startOnClickListeners() {
        // Set On Check Listener to Array Profile List
        for (RadioGroup rg : rgTasteProfileList) {
            rg.setOnCheckedChangeListener(new HandleClick());
        }
        RadioGroup rgOverall = (RadioGroup) findViewById(R.id.radioGroupOverall);
        rgOverall.setOnCheckedChangeListener(new HandleClickOverall());
    }

    private void stopOnClickListeners() {
        // Set On Check Listener to Array Profile List
        for (RadioGroup rg : rgTasteProfileList) {
            rg.setOnCheckedChangeListener(null);
        }
        RadioGroup rgOverall = (RadioGroup) findViewById(R.id.radioGroupOverall);
        rgOverall.setOnCheckedChangeListener(null);
    }

    private void UpdateCoffeeLogView(CoffeeLog coffeeLog) {
        ((EditText) findViewById(R.id.editTextName)).setText(coffeeLog.Name);
        ((EditText) findViewById(R.id.editTextRoaster)).setText(coffeeLog.Roaster);
        ((EditText) findViewById(R.id.editTextCity)).setText(coffeeLog.City);
        ((AutoCompleteTextView) findViewById(R.id.editTextCountry)).setText(coffeeLog.Country);
        ((EditText) findViewById(R.id.editTextRoastDate)).setText(dateFormatter.format(coffeeLog.RoastDate.getTime()));
        ((EditText) findViewById(R.id.editTextBrewDate)).setText(dateFormatter.format(coffeeLog.BrewDate.getTime()));
        ((EditText) findViewById(R.id.editTextCoffeeGrams)).setText(String.valueOf(coffeeLog.CoffeeGrams));
        ((EditText) findViewById(R.id.editTextWaterGrams)).setText(String.valueOf(coffeeLog.WaterGrams));
        ((EditText) findViewById(R.id.editTextNotes)).setText(coffeeLog.Notes);
        ((TextView) findViewById(R.id.imageLocation)).setText(coffeeLog.ImageLocation);
        ImageView image = (ImageView)findViewById(R.id.uxImageView);
        Bitmap imageBitmap = BitmapFactory.decodeFile(coffeeLog.ImageLocation);
        if(imageBitmap != null)
            imageView.setImageBitmap(imageBitmap);


        setRadioGroupCheck(R.id.radioGroupBrewMethod, coffeeLog.BrewMethod);
        setRadioGroupCheck(R.id.radioGroupOverall, coffeeLog.Overall);
        for (int i = 0; i < rgTasteProfileList.size(); i++) {
            RadioGroup radioGroup = rgTasteProfileList.get(i);
            if (i < coffeeLog.TasteProfile.length()) {
                setRadioGroupCheck(radioGroup.getId(), Character.getNumericValue(coffeeLog.TasteProfile.charAt(i)));
            }
        }
    }

    private void setRadioGroupCheck(int id, int text) {
        setRadioGroupCheck(id, String.valueOf(text));
    }

    private void setRadioGroupCheck(int id, String text) {
        RadioGroup radioGroup = (RadioGroup) findViewById(id);
        if (radioGroup != null) {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                View o = radioGroup.getChildAt(i);
                if (o instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    if (radioButton.getText().toString().equals(text)) {
                        radioGroup.check(radioButton.getId());
                        break;
                    }
                }
            }
        }
    }

    private class HandleClickOverall implements RadioGroup.OnCheckedChangeListener {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Log.d("chk", "id" + checkedId);
            RadioButton rb = (RadioButton) findViewById(checkedId);
            if (rb == null) {
                radarChart.setBackgroundColor(Color.TRANSPARENT);
            } else if (rb.getText().equals("1")) {
                radarChart.setBackgroundColor(Color.RED);
            } else if (rb.getText().equals("2")) {
                radarChart.setBackgroundColor(Color.MAGENTA);
            } else if (rb.getText().equals("3")) {
                radarChart.setBackgroundColor(Color.YELLOW);
            } else if (rb.getText().equals("4")) {
                radarChart.setBackgroundColor(Color.GREEN);
            } else if (rb.getText().equals("5")) {
                radarChart.setBackgroundColor(Color.CYAN);
            }
        }
    }

    private class HandleClick implements RadioGroup.OnCheckedChangeListener {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Log.d("chk", "id" + checkedId);
            RadarData data = new RadarData(getXAxisValues(), getDataSet());
            radarChart.setData(data);
            radarChart.invalidate();
        }
    }

    private ArrayList<RadarDataSet> getDataSet() {
        ArrayList<Entry> valueSet1 = new ArrayList<>();

        for (RadioGroup rg : rgTasteProfileList) {
            RadioButton button = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
            float value = 0;
            if (button != null) {
                value = Integer.parseInt(button.getText().toString());
            }
            Entry v1e1 = new Entry(value, 0);
            valueSet1.add(v1e1);
        }

        RadarDataSet radarDataSet1 = new RadarDataSet(valueSet1, "Brand 1");
        radarDataSet1.setColor(Color.rgb(0, 155, 0));

        radarDataSet1.setFillColor(Color.GREEN);
        radarDataSet1.setHighLightColor(Color.RED);
        radarDataSet1.setFillAlpha(100);
        radarDataSet1.setDrawFilled(true);
        ArrayList<RadarDataSet> dataSets = new ArrayList<>();
        dataSets.add(radarDataSet1);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("SWEET");       //0
        xAxis.add("SOUR");        //1
        xAxis.add("FLORAL");      //2
        xAxis.add("SPICY");       //3
        xAxis.add("SALTY");       //4
        xAxis.add("BERRY");       //5
        xAxis.add("CITRUS");      //6
        xAxis.add("STORE FRUIT"); //7
        xAxis.add("CHOCOLATE");   //8
        xAxis.add("CARAMEL");     //9
        xAxis.add("SMOKEY");      //10
        xAxis.add("BITTER");      //11
        xAxis.add("SAVORY");      //12
        xAxis.add("BODY");        //13
        xAxis.add("CLEAN");       //14
        xAxis.add("FINISH");      //15
        return xAxis;
    }

    private void findViewsById() {
       // brewTxt = (EditText) findViewById(R.id.editTextRoastDate);
        brewTxt.setInputType(InputType.TYPE_NULL);
        brewTxt.requestFocus();

        roastTxt = (EditText) findViewById(R.id.editTextBrewDate);
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

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        roastDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                roastTxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                navigateUpTo(new Intent(this, MainActivity.class));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == brewTxt) {
            brewDatePickerDialog.show();
        } else if (view == roastTxt) {
            roastDatePickerDialog.show();
        }
    }

    private CoffeeLog getCoffeeLogDetails(Long id) {
        CoffeeLog coffeeLog = new CoffeeLog();
        coffeeLog.Id = id;
        coffeeLog.Name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
        coffeeLog.Roaster = ((EditText) findViewById(R.id.editTextRoaster)).getText().toString();
        coffeeLog.City = ((EditText) findViewById(R.id.editTextCity)).getText().toString();
        coffeeLog.Country = ((AutoCompleteTextView) findViewById(R.id.editTextCountry)).getText().toString();
        String dateStr = ((EditText) findViewById(R.id.editTextRoastDate)).getText().toString();
        String imageStr = ((TextView) findViewById(R.id.imageLocation)).getText().toString();
        coffeeLog.ImageLocation = imageStr;
        //SimpleDateFormat curFormater = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
        Date date = new Date();
        try {
            date = dateFormatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        coffeeLog.RoastDate = date;

        dateStr = ((EditText) findViewById(R.id.editTextBrewDate)).getText().toString();
        try {
            date = dateFormatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        coffeeLog.BrewDate = date;

        int buttonId = ((RadioGroup) findViewById(R.id.radioGroupBrewMethod)).getCheckedRadioButtonId();
        coffeeLog.BrewMethod = ((RadioButton) findViewById(buttonId)).getText().toString();

        double grams = 0;
        String gramsStr = ((EditText) findViewById(R.id.editTextCoffeeGrams)).getText().toString();
        try {
            grams = Double.parseDouble(gramsStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        coffeeLog.CoffeeGrams = grams;

        grams = 0;
        gramsStr = ((EditText) findViewById(R.id.editTextWaterGrams)).getText().toString();
        try {
            grams = Double.parseDouble(gramsStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        coffeeLog.WaterGrams = grams;

        buttonId = ((RadioGroup) findViewById(R.id.radioGroupOverall)).getCheckedRadioButtonId();
        coffeeLog.Overall = Integer.parseInt(((RadioButton) findViewById(buttonId)).getText().toString());

        coffeeLog.Notes = ((EditText) findViewById(R.id.editTextNotes)).getText().toString();

        String tasteProfileStr = "";
        for (int i = 0; i < rgTasteProfileList.size(); i++) {
            RadioGroup radioGroup = rgTasteProfileList.get(i);
            RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            tasteProfileStr = tasteProfileStr + radioButton.getText().toString();
        }
        coffeeLog.TasteProfile = tasteProfileStr;
        return coffeeLog;
    }

    public void cancelCoffeeLog(View view) {
        Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.buttonSave)
    public void saveCoffeeLog(View view) {
        if (db_id > 0) {
            if (mydb.updateCoffeeLog(db_id, getCoffeeLogDetails(db_id)) > 0) {
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (mydb.insertCoffeeLog(getCoffeeLogDetails(db_id)) > 0) {

                Notification notification = new Notification.Builder(this)
                        .setContentTitle("New Coffee Log")
                        .setContentText(getCoffeeLogDetails(db_id).Name)
                        .setSmallIcon(R.drawable.small_coffee)
                        .setPriority(Notification.PRIORITY_DEFAULT)
                        .setStyle(new Notification.BigTextStyle().bigText("New Coffee Log Created")) // optional
                        .build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1, notification);
                Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }




        public static final int MEDIA_TYPE_IMAGE = 1;
        public static final int MEDIA_TYPE_VIDEO = 2;
        private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
        @Bind(R.id.uxImageView)
        public ImageView imageView;
        private File filePath;

          private File getOutputMediaFile(int type) {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.

            Log.d("sdcard state=", Environment.getExternalStorageState());

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "MyNewCameraApp");
            Log.d("dir=", mediaStorageDir.toString());

            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_" + timeStamp + ".jpg");
            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            Log.d("mediaFile", mediaFile.getAbsolutePath());

            return mediaFile;
        }

        @OnClick(R.id.uxCamera)
        public void onCamera(View view) {


            // Create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            filePath = getOutputMediaFile(MEDIA_TYPE_IMAGE); // create a file to save the image
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePath)); // set the image file name

            // Check if there is a camera application
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        Bitmap imageBitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath());
                        TextView tmp = (TextView)findViewById(R.id.imageLocation);
                        tmp.setText(filePath.getAbsolutePath());
                        imageView.setImageBitmap(imageBitmap);
                    }
                    else{
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(imageBitmap);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the image capture
                    Toast.makeText(this, "Image capture cancelled", Toast.LENGTH_LONG).show();
                } else {
                    // Image capture failed, advise user
                    Toast.makeText(this, "Image capture failed", Toast.LENGTH_LONG).show();
                }
            }
        }




}