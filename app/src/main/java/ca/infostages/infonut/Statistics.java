package ca.infostages.infonut;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    Float nutrientValue = 0.00f;
    Float remainder = 100f;

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
                label = "Fat";
                nutrientValue = Float.valueOf(String.valueOf(hashmap.get("fat")));
                System.out.println("Nut value: "+ nutrientValue);
                List<PieEntry> pieEntries = new ArrayList<>();

                remainder = remainder - nutrientValue;
                System.out.println("remainder: "+remainder);

                pieEntries.add(new PieEntry(remainder, "Intake"));
                pieEntries.add(new PieEntry(nutrientValue, label));

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
        });
        saturatedFat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Saturated Fat";
                nutrientValue = Float.valueOf(String.valueOf(hashmap.get("saturatedFat")));
                System.out.println("Nut value: "+ nutrientValue);
                List<PieEntry> pieEntries = new ArrayList<>();


                System.out.println("TOTAL: " + getTotal());

                DecimalFormat df = new DecimalFormat("##.##");
                nutrientValue = Float.valueOf(df.format(nutrientValue));
                remainder = Float.valueOf(df.format(remainder));

                pieEntries.add(new PieEntry(nutrientValue, "Intake"));
                pieEntries.add(new PieEntry(Float.valueOf(df.format(getTotal())), label));

                /*pieEntries.add(new PieEntry(getTotal(), "Intake"));
                pieEntries.add(new PieEntry(nutrientValue, label));*/

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
        });
        transFat .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Trans Fat";
                nutrientValue = Float.valueOf(String.valueOf(hashmap.get("transFat")));
                System.out.println("Nut value: "+ nutrientValue);
                List<PieEntry> pieEntries = new ArrayList<>();

                remainder = remainder - nutrientValue;
                System.out.println("remainder: "+remainder);

                pieEntries.add(new PieEntry(remainder, "Intake"));
                pieEntries.add(new PieEntry(nutrientValue, label));

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
        });
        cholesterol .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Cholesterol";
                nutrientValue = Float.valueOf(String.valueOf(hashmap.get("cholesterol")));
                System.out.println("Nut value: "+ nutrientValue);
                List<PieEntry> pieEntries = new ArrayList<>();

                remainder = remainder - nutrientValue;
                System.out.println("remainder: "+remainder);

                pieEntries.add(new PieEntry(remainder, "Intake"));
                pieEntries.add(new PieEntry(nutrientValue, label));

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
        });
        sodium .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Sodium";
                nutrientValue = Float.valueOf(String.valueOf(hashmap.get("sodium")));
                System.out.println("Nut value: "+ nutrientValue);
                List<PieEntry> pieEntries = new ArrayList<>();

                remainder = remainder - nutrientValue;
                System.out.println("remainder: "+remainder);

                pieEntries.add(new PieEntry(remainder, "Intake"));
                pieEntries.add(new PieEntry(nutrientValue, label));

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
        });
        carbohydrate .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Carbohydrate";
                nutrientValue = Float.valueOf(String.valueOf(hashmap.get("carbohydrate")));
                System.out.println("Nut value: "+ nutrientValue);
                List<PieEntry> pieEntries = new ArrayList<>();

                remainder = remainder - nutrientValue;
                System.out.println("remainder: "+remainder);

                pieEntries.add(new PieEntry(remainder, "Intake"));
                pieEntries.add(new PieEntry(nutrientValue, label));

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
        });
        fibre .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Fibre";
                nutrientValue = Float.valueOf(String.valueOf(hashmap.get("fibre")));
                System.out.println("Nut value: "+ nutrientValue);
                List<PieEntry> pieEntries = new ArrayList<>();

                remainder = remainder - nutrientValue;
                System.out.println("remainder: "+remainder);

                pieEntries.add(new PieEntry(remainder, "Intake"));
                pieEntries.add(new PieEntry(nutrientValue, label));

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
        });
        sugars .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Sugars";
                nutrientValue = Float.valueOf(String.valueOf(hashmap.get("sugars")));
                System.out.println("Nut value: "+ nutrientValue);
                List<PieEntry> pieEntries = new ArrayList<>();

                remainder = remainder - nutrientValue;
                System.out.println("remainder: "+remainder);

                pieEntries.add(new PieEntry(remainder, "Intake"));
                pieEntries.add(new PieEntry(nutrientValue, label));

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
        });
        protein .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Protein";
                nutrientValue = Float.valueOf(String.valueOf(hashmap.get("protein")));
                System.out.println("Nut value: "+ nutrientValue);
                List<PieEntry> pieEntries = new ArrayList<>();

                remainder = remainder - nutrientValue;
                System.out.println("remainder: "+remainder);

                pieEntries.add(new PieEntry(remainder, "Intake"));
                pieEntries.add(new PieEntry(nutrientValue, label));

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
        });
        vitaminA .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Vitamin A";
                nutrientValue = Float.valueOf(String.valueOf(hashmap.get("vitaminA")));
                System.out.println("Nut value: "+ nutrientValue);
                List<PieEntry> pieEntries = new ArrayList<>();

                remainder = remainder - nutrientValue;
                System.out.println("remainder: "+remainder);

                pieEntries.add(new PieEntry(remainder, "Intake"));
                pieEntries.add(new PieEntry(nutrientValue, label));

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
        });
        vitaminC .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Vitamin C";
                nutrientValue = Float.valueOf(String.valueOf(hashmap.get("vitaminC")));
                System.out.println("Nut value: "+ nutrientValue);
                List<PieEntry> pieEntries = new ArrayList<>();

                remainder = remainder - nutrientValue;
                System.out.println("remainder: "+remainder);

                pieEntries.add(new PieEntry(remainder, "Intake"));
                pieEntries.add(new PieEntry(nutrientValue, label));

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
        });
        calcium .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Calcium";
                nutrientValue = Float.valueOf(String.valueOf(hashmap.get("calcium")));
                System.out.println("Nut value: "+ nutrientValue);
                List<PieEntry> pieEntries = new ArrayList<>();

                remainder = remainder - nutrientValue;
                System.out.println("remainder: "+remainder);

                pieEntries.add(new PieEntry(remainder, "Intake"));
                pieEntries.add(new PieEntry(nutrientValue, label));

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
        });
        iron .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Iron";
                nutrientValue = Float.valueOf(String.valueOf(hashmap.get("iron")));
                System.out.println("Nut value: "+ nutrientValue);
                List<PieEntry> pieEntries = new ArrayList<>();

                remainder = remainder - nutrientValue;
                System.out.println("remainder: "+remainder);

                pieEntries.add(new PieEntry(remainder, "Intake"));
                pieEntries.add(new PieEntry(nutrientValue, label));

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
        });
    }

    private Float getTotal() {
        return remainder - nutrientValue;
    }

}
