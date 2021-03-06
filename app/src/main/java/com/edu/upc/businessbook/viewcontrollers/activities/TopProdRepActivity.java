package com.edu.upc.businessbook.viewcontrollers.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.edu.upc.businessbook.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class TopProdRepActivity extends AppCompatActivity {
    private PieChart pieChart;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String[] listItems;
    Bundle idlocal;
    Bundle dateB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_prod_rep);

        idlocal = new Bundle();
        idlocal.putInt("Selected",0);

        dateB = new Bundle();

        mDisplayDate = (TextView)findViewById(R.id.dateText);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                dateB.putInt("year", cal.get(Calendar.YEAR));
                dateB.putInt("month",Calendar.MONTH );
                dateB.putInt("day",cal.get(Calendar.DAY_OF_MONTH) );

                DatePickerDialog monthDatePickerDialog = new DatePickerDialog(TopProdRepActivity.this,
                        android.app.AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mDisplayDate.setText( year  + "/" + (month + 1));
                        if(year==2018 && month==5){
                            pieChart.setData(fillPieData(idlocal.getInt("Selected")));
                        }
                        else{
                            NoDataPieChart();
                        }
                    }
                }, dateB.getInt("year"), dateB.getInt("month"), dateB.getInt("day")){
                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        getDatePicker().findViewById(getResources().getIdentifier("day","id","android")).setVisibility(View.GONE);
                    }
                };
                monthDatePickerDialog.show();
            }
        });
        pieChart = (PieChart) findViewById(R.id.chart);
        TextView text = (TextView) findViewById(R.id.titleText);
        NoDataPieChart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_reports, menu);
        return true;
    }
    public void NoDataPieChart(){
        pieChart.clear();
        pieChart.setNoDataText("No data available, please select a date");
    }
    public AlertDialog.Builder localDialog(){
        listItems = new String[] {"Av. Salaverry 342", "Av. La Marina 879", "Jr. Cuzco 598"};

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TopProdRepActivity.this);
        mBuilder.setTitle("Choose an local");
        mBuilder.setIcon(R.drawable.ic_action_local_dialog);
        mBuilder.setSingleChoiceItems(listItems,idlocal.getInt("Selected"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i==0)
                    idlocal.putInt("Selected", 0);
                if (i==1)
                    idlocal.putInt("Selected", 1);
                if (i==2)
                    idlocal.putInt("Selected", 2);

                mDisplayDate.setText("---- / --");
               NoDataPieChart();
            }
        });
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return mBuilder;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.action_location:
                AlertDialog mDialog = localDialog().create();
                mDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public PieData fillPieData(int local){

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.15f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        if(local==0) {
            yValues.clear();
            yValues.add(new PieEntry(70f, "Aceite Sol"));
            yValues.add(new PieEntry(80f, "Sal Yodada"));
            yValues.add(new PieEntry(65f, "Coca Cola"));
            yValues.add(new PieEntry(45f, "Donofrio"));
        }
        if(local == 1) {
            yValues.clear();
            yValues.add(new PieEntry(50, "Pilsen Callao"));
            yValues.add(new PieEntry(50, "Pan Bimbo"));
        }
        if(local == 2) {
            yValues.clear();
            yValues.add(new PieEntry(65, "Paneton Sayon"));
            yValues.add(new PieEntry(75, "Pan Bimbo"));
            yValues.add(new PieEntry(60f, "Donofrio"));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.WHITE);
        return  data;
    }

}
