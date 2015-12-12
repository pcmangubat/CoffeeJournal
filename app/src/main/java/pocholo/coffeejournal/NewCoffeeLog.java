package pocholo.coffeejournal;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class NewCoffeeLog extends Activity implements OnClickListener {

    //UI References
    private EditText brewTxt;
    private EditText roastTxt;
    private RadioGroup rgSweet;
    private RadioGroup rgSour;
    private RadioGroup rgFloral;
    private DatePickerDialog brewDatePickerDialog;
    private DatePickerDialog roastDatePickerDialog;
    private  RadarChart radarChart;
    private SimpleDateFormat dateFormatter;
    private ArrayList<RadioGroup> rgTasteProfileList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_coffee_log);

        // Load List with Radio Groups
        rgTasteProfileList = new ArrayList<RadioGroup>();
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupSweet));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupSour));
        rgTasteProfileList.add((RadioGroup) findViewById(R.id.radioGroupFloral));
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
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        findViewsById();
        setDateTimeField();

        // Initialize Radar Chart
        radarChart = (RadarChart) findViewById(R.id.chart);
        if(radarChart != null){
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
            rgOverall.setOnCheckedChangeListener(new HandleClickOverall());
            RadioButton rbOverall = (RadioButton) findViewById(rgOverall.getCheckedRadioButtonId());
            if(rbOverall == null){
                radarChart.setBackgroundColor(Color.TRANSPARENT);
            } else if( rbOverall.getText().equals("1") ){
                radarChart.setBackgroundColor(Color.RED);
            } else if( rbOverall.getText().equals("2") ){
                radarChart.setBackgroundColor(Color.MAGENTA);
            } else if( rbOverall.getText().equals("3") ){
                radarChart.setBackgroundColor(Color.YELLOW);
            } else if ( rbOverall.getText().equals("4") ) {
                radarChart.setBackgroundColor(Color.GREEN);
            } else if ( rbOverall.getText().equals("5") ){
                radarChart.setBackgroundColor(Color.CYAN);
            }

            // Hide Legends
            Legend legend = radarChart.getLegend();
            legend.setEnabled(false);
            radarChart.invalidate();
        }


        // Set On Check Listener to Array Profile List
        for (RadioGroup rg : rgTasteProfileList) {
            rg.setOnCheckedChangeListener(new HandleClick());
        }
        /*
        //rgSweet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        rgSweet.setOnCheckedChangeListener(new HandleClick());
        rgSour.setOnCheckedChangeListener(new HandleClick());
        rgFloral.setOnCheckedChangeListener(new HandleClick());

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("chk", "id" + checkedId);
                RadarData data = new RadarData(getXAxisValues(), getDataSet());
                chart.setData(data);
                chart.invalidate();
            }

        });
        */
    }

    private class HandleClickOverall implements RadioGroup.OnCheckedChangeListener {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Log.d("chk", "id" + checkedId);
            RadioButton rb = (RadioButton) findViewById(checkedId);
            if(rb == null){
                radarChart.setBackgroundColor(Color.TRANSPARENT);
            } else if( rb.getText().equals("1") ){
                radarChart.setBackgroundColor(Color.RED);
            } else if( rb.getText().equals("2") ){
                radarChart.setBackgroundColor(Color.MAGENTA);
            } else if( rb.getText().equals("3") ){
                radarChart.setBackgroundColor(Color.YELLOW);
            } else if ( rb.getText().equals("4") ) {
                radarChart.setBackgroundColor(Color.GREEN);
            } else if ( rb.getText().equals("5") ){
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
        ArrayList<RadarDataSet> dataSets = null;
        ArrayList<Entry> valueSet1 = new ArrayList<>();

        for(RadioGroup rg : rgTasteProfileList){
            RadioButton button = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
            float value = 0;
            if( button != null) {
                value = Integer.parseInt(button.getText().toString());
            }
            Entry v1e1 = new Entry(value, 0);
            valueSet1.add(v1e1);
        }
        /*
        rgSweet = (RadioGroup) findViewById(R.id.radioGroupSweet);
        RadioButton button = (RadioButton) findViewById(rgSweet.getCheckedRadioButtonId());;
        if( button != null) {
            Entry v1e1 = new Entry(Integer.parseInt(button.getText().toString()), 0); // Sweet
            valueSet1.add(v1e1);
        }
        rgSour =(RadioGroup) findViewById(R.id.radioGroupSour);
        button = (RadioButton) findViewById(rgSour.getCheckedRadioButtonId());;
        if( button != null) {
            Entry v1e1 = new Entry(Integer.parseInt(button.getText().toString()), 1); // Sour
            valueSet1.add(v1e1);
        }

        rgFloral =(RadioGroup) findViewById(R.id.radioGroupFloral);
        button = (RadioButton) findViewById(rgFloral.getCheckedRadioButtonId());;
        if( button != null) {
            Entry v1e1 = new Entry(Integer.parseInt(button.getText().toString()), 1); // Sour
            valueSet1.add(v1e1);
        }
*/
        /*BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);
*/
        RadarDataSet radarDataSet1 = new RadarDataSet(valueSet1, "Brand 1");
        radarDataSet1.setColor(Color.rgb(0, 155, 0));
        /*
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
*/
        radarDataSet1.setFillColor(Color.GREEN);
        radarDataSet1.setHighLightColor(Color.RED);
        radarDataSet1.setFillAlpha(100);
        radarDataSet1.setDrawFilled(true);
        dataSets = new ArrayList<>();
        dataSets.add(radarDataSet1);
        //dataSets.add(barDataSet2);
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
    public void onClick(View view) {
        if (view == brewTxt) {
            brewDatePickerDialog.show();
        } else if (view == roastTxt) {
            roastDatePickerDialog.show();
        }
    }

    public void saveCoffeeLog(View view){
        // DB Helper to save or create record
    }
}