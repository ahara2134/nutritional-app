package ca.infostages.infonut;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Statistics extends AppCompatActivity{

    private PieChart mChart;
    Button fat;
    Button saturatedFat;
    Button transFat;
    Button cholesterol;
    Button sodium;
    Button carbohydrate;
    Button fibre;
    Button sugars;
    Button protein;
    Button vitaminA;
    Button vitaminC;
    Button calcium;
    Button iron;

    String label;
    double nutrientValue = 0;
    double intake = 100; //CHANGE HERE WITH USER SPECIFIED INTAKE
    int percent = 0;
    int full = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        mChart = (PieChart) findViewById(R.id.pie);

        fat = findViewById(R.id.fat);
        saturatedFat = findViewById(R.id.saturatedFat);
        transFat = findViewById(R.id.transFat);
        cholesterol = findViewById(R.id.cholesterol);
        sodium = findViewById(R.id.sodium);
        carbohydrate = findViewById(R.id.carbohydrate);
        fibre = findViewById(R.id.fibre);
        sugars = findViewById(R.id.sugar);
        protein = findViewById(R.id.protein);
        vitaminA = findViewById(R.id.vitaminA);
        vitaminC = findViewById(R.id.vitaminC);
        calcium = findViewById(R.id.calcium);
        iron = findViewById(R.id.iron);

        final HashMap<String, Double> hashmap =  NutritionData.nutritionHashMap;

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);

        List<PieEntry> pieEntries = new ArrayList<>();

        pieEntries.add(new PieEntry(95, "Intake"));
        pieEntries.add(new PieEntry(5, "Test"));

        // The name of the chart
        PieDataSet dataSet = new PieDataSet(pieEntries, label);

        // Color of the chart entries
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        // Displaying the chart data
        PieData data = new PieData(dataSet);
        mChart.setData(data);
        mChart.animateY(1000); // Animation for the chart
        mChart.invalidate(); // refresh

        fat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Fat";
                nutrientValue = hashmap.get("fat");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
                fat.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                fat.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
        // change this to good fats!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        saturatedFat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Saturated Fat";
                nutrientValue = hashmap.get("saturatedFat");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
                saturatedFat.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                saturatedFat.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
        // change this to bad fats!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        transFat .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Trans Fat";
                nutrientValue = hashmap.get("transFat");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
                transFat.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                transFat.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
        cholesterol .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Cholesterol";
                nutrientValue = hashmap.get("cholesterol");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
                cholesterol.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                cholesterol.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
        sodium .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Sodium";
                nutrientValue = hashmap.get("sodium");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
                sodium.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                sodium.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
        carbohydrate .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Carbohydrate";
                nutrientValue = hashmap.get("carbohydrate");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
                carbohydrate.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                carbohydrate.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
        fibre .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Fibre";
                nutrientValue = hashmap.get("fibre");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
                fibre.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                fibre.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
        // change this to potassium!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        sugars .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Sugars";
                nutrientValue = hashmap.get("sugars");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
                sugars.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                sugars.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
        protein .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Protein";
                nutrientValue = hashmap.get("protein");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
                protein.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                protein.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
        vitaminA .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Vitamin A";
                nutrientValue = hashmap.get("vitaminA");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
                vitaminA.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                vitaminA.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
        vitaminC .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Vitamin C";
                nutrientValue = hashmap.get("vitaminC");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
                vitaminC.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                vitaminC.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
        calcium .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Calcium";
                nutrientValue = hashmap.get("calcium");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
                calcium.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                calcium.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
        iron .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Iron";
                nutrientValue = hashmap.get("iron");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
                iron.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                iron.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
    }

    private void buttonChange(){
        iron.getBackground().clearColorFilter();
        iron.setTextColor(getResources().getColor(R.color.white));
        fat.getBackground().clearColorFilter();
        fat.setTextColor(getResources().getColor(R.color.white));
        saturatedFat.getBackground().clearColorFilter();
        saturatedFat.setTextColor(getResources().getColor(R.color.white));
        transFat.getBackground().clearColorFilter();
        transFat.setTextColor(getResources().getColor(R.color.white));
        cholesterol.getBackground().clearColorFilter();
        cholesterol.setTextColor(getResources().getColor(R.color.white));
        sodium.getBackground().clearColorFilter();
        sodium.setTextColor(getResources().getColor(R.color.white));
        carbohydrate.getBackground().clearColorFilter();
        carbohydrate.setTextColor(getResources().getColor(R.color.white));
        fibre.getBackground().clearColorFilter();
        fibre.setTextColor(getResources().getColor(R.color.white));
        sugars.getBackground().clearColorFilter();
        sugars.setTextColor(getResources().getColor(R.color.white));
        protein.getBackground().clearColorFilter();
        protein.setTextColor(getResources().getColor(R.color.white));
        vitaminA.getBackground().clearColorFilter();
        vitaminA.setTextColor(getResources().getColor(R.color.white));
        vitaminC.getBackground().clearColorFilter();
        vitaminC.setTextColor(getResources().getColor(R.color.white));
        calcium.getBackground().clearColorFilter();
        calcium.setTextColor(getResources().getColor(R.color.white));
    }

    private void createChart() {
        List<PieEntry> pieEntries = new ArrayList<>();

        pieEntries.add(new PieEntry(full, "Intake"));
        pieEntries.add(new PieEntry(percent, label));

        // The name of the chart
        PieDataSet dataSet = new PieDataSet(pieEntries, label);

        // Color of the chart entries
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        // Displaying the chart data
        PieData data = new PieData(dataSet);
        mChart.setData(data);
        mChart.animateY(1000); // Animation for the chart
        mChart.invalidate(); // refresh
    }

    private void valueConverter(double value, double remain) {
        percent = (int) (value/remain*100);
        full = 100 - percent;
    }

}
