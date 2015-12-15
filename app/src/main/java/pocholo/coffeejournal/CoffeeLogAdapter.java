package pocholo.coffeejournal;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;

import java.util.ArrayList;

public class CoffeeLogAdapter extends ArrayAdapter<CoffeeLog> {
    public CoffeeLogAdapter(Context context, ArrayList<CoffeeLog> coffeeLogs) {
        super(context, 0, coffeeLogs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CoffeeLog coffeeLog = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.coffeelog_details, parent, false);
        }
        // Lookup view for data population
        TextView textID = (TextView) convertView.findViewById(R.id.ID);
        TextView textName = (TextView) convertView.findViewById(R.id.Name);
        TextView textRoaster = (TextView) convertView.findViewById(R.id.Roaster);
        TextView textCountry = (TextView) convertView.findViewById(R.id.Country);
        TextView textMethod = (TextView) convertView.findViewById(R.id.BrewMethod);

        // Initialize Radar Chart
        RadarChart radarChart = (RadarChart) convertView.findViewById(R.id.chartMini);
        if (radarChart != null) {

            ArrayList<String> xAxis = new ArrayList<>();
            xAxis.add("SWEET");       //0
            xAxis.add("");        //1
            xAxis.add("FLORAL");      //2
            xAxis.add("SPICY");       //3
            xAxis.add("SALTY");       //4
            xAxis.add("BERRY");       //5
            xAxis.add("CITRUS");      //6
            xAxis.add(""); //7
            xAxis.add("CHOCOLATE");   //8
            xAxis.add("");     //9
            xAxis.add("SMOKEY");      //10
            xAxis.add("BITTER");      //11
            xAxis.add("SAVORY");      //12
            xAxis.add("BODY");        //13
            xAxis.add("CLEAN");       //14
            xAxis.add("");      //15

            ArrayList<Entry> valueSet1 = new ArrayList<>();

            for (int i = 0; i < coffeeLog.TasteProfile.length(); i++) {
                int value = Character.getNumericValue(coffeeLog.TasteProfile.charAt(i));
                Entry v1e1 = new Entry(value, 0);
                valueSet1.add(v1e1);
            }

            RadarDataSet radarDataSet1 = new RadarDataSet(valueSet1, "Brand 1");
            radarDataSet1.setColor(Color.rgb(0, 155, 0));

            radarDataSet1.setFillColor(Color.GREEN);
            radarDataSet1.setHighLightColor(Color.RED);
            radarDataSet1.setFillAlpha(100);
            radarDataSet1.setDrawFilled(true);
            radarDataSet1.setDrawValues(false);
            radarDataSet1.setValueTextSize(1f);
            ArrayList<RadarDataSet> dataSets = new ArrayList<>();
            dataSets.add(radarDataSet1);


            RadarData data = new RadarData(xAxis, dataSets);
            data.setValueTextSize(1f);
            data.setDrawValues(false);
            radarChart.setData(data);
            radarChart.setDescription("");
            radarChart.getYAxis().setAxisMinValue(0);
            radarChart.getYAxis().setDrawTopYLabelEntry(false);
            radarChart.getYAxis().setDrawLabels(false);
            //radarChart.getXAxis().setDrawLabels(false);

            radarChart.getYAxis().setAxisMaxValue(5);
            radarChart.setDescriptionTextSize(6f);
            radarChart.animateXY(2000, 2000);
            radarChart.setWebColor(Color.GRAY);
            radarChart.setDrawWeb(false);

            // Set Background Color to Overall Rating
            int overall = coffeeLog.Overall;
            if (overall == 1) {
                radarChart.setBackgroundColor(Color.RED);
            } else if (overall == 2) {
                radarChart.setBackgroundColor(Color.MAGENTA);
            } else if (overall == 3) {
                radarChart.setBackgroundColor(Color.YELLOW);
            } else if (overall == 4) {
                radarChart.setBackgroundColor(Color.GREEN);
            } else if (overall == 5) {
                radarChart.setBackgroundColor(Color.CYAN);
            } else {
                radarChart.setBackgroundColor(Color.TRANSPARENT);
            }

            // Hide Legends
            Legend legend = radarChart.getLegend();
            legend.setEnabled(false);
            radarChart.invalidate();
        }

        // Populate the data into the template view using the data object
        textID.setText(String.valueOf(coffeeLog.Id));
        textName.setText(coffeeLog.Name);
        textRoaster.setText(coffeeLog.Roaster);
        textCountry.setText(coffeeLog.Country);
        textMethod.setText(coffeeLog.BrewMethod);

        // Return the completed view to render on screen
        return convertView;
    }
}