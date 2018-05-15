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

        boolean checkedServing = getIntent().getBooleanExtra("servingChecked", true);
        boolean checked100 = getIntent().getBooleanExtra("100Checked", false);
        System.out.println("CHECKED SERVING: " + checkedServing);
        System.out.println("CHECKED 100: " + checked100);

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
                nutrientValue = hashmap.get("fat");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
            }
        });
        saturatedFat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Saturated Fat";
                nutrientValue = hashmap.get("saturatedFat");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
            }
        });
        transFat .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Trans Fat";
                nutrientValue = hashmap.get("transFat");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
            }
        });
        cholesterol .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Cholesterol";
                nutrientValue = hashmap.get("cholesterol");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
            }
        });
        sodium .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Sodium";
                nutrientValue = hashmap.get("sodium");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
            }
        });
        carbohydrate .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Carbohydrate";
                nutrientValue = hashmap.get("carbohydrate");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
            }
        });
        fibre .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Fibre";
                nutrientValue = hashmap.get("fibre");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
            }
        });
        sugars .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Sugars";
                nutrientValue = hashmap.get("sugars");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
            }
        });
        protein .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Protein";
                nutrientValue = hashmap.get("protein");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
            }
        });
        vitaminA .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Vitamin A";
                nutrientValue = hashmap.get("vitaminA");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
            }
        });
        vitaminC .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Vitamin C";
                nutrientValue = hashmap.get("vitaminC");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
            }
        });
        calcium .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Calcium";
                nutrientValue = hashmap.get("calcium");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
            }
        });
        iron .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                label = "Iron";
                nutrientValue = hashmap.get("iron");
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                createChart();
            }
        });
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
