package ca.infostages.infonut;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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

    //default plans
    private Long default_bad_fats;
    private Long default_calcium;
    private Long default_calories;
    private Long default_carbohydrates;
    private Long default_cholesterol;
    private Long default_fibre;
    private Long default_good_fats;
    private Long default_iron;
    private Long default_potassium;
    private Long default_protein;
    private Long default_sodium;
    private Long default_vitamin_A;
    private Long default_vitamin_C;

    String label;
    double nutrientValue = 0;
    double intake = 100; //CHANGE HERE WITH USER SPECIFIED INTAKE
    int percent = 0;
    int full = 100;

    FirebaseUser currentUser;
    private static final String TAG = "Statistics.java";


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

        //GET FIREBASE STUFF HERE

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            Intent intent = new Intent(Statistics.this, MainActivity.class);
            startActivity(intent);
        } else {
            //Checks if user's demographics are entered in. If not, send to NewUserActivity.
            DatabaseReference planReference;
            planReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("plan").child("default_plan");
            planReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String plan = dataSnapshot.getValue().toString();
                    if (plan.equals("false")) {
                        Intent intent = new Intent(Statistics.this, NewUser.class);
                        startActivity(intent);
                    } else {
                        default_bad_fats = dataSnapshot.child("bad_fats").getValue(Long.class);
                        default_calcium = dataSnapshot.child("calcium").getValue(Long.class);
                        default_calories = dataSnapshot.child("calories").getValue(Long.class);
                        default_carbohydrates = dataSnapshot.child("carbohydrates").getValue(Long.class);
                        default_cholesterol = dataSnapshot.child("cholesterol").getValue(Long.class);
                        default_fibre = dataSnapshot.child("fibre").getValue(Long.class);
                        default_good_fats = dataSnapshot.child("good_fats").getValue(Long.class);
                        default_iron = dataSnapshot.child("iron").getValue(Long.class);
                        default_potassium = dataSnapshot.child("potassium").getValue(Long.class);
                        default_protein = dataSnapshot.child("protein").getValue(Long.class);
                        default_sodium = dataSnapshot.child("sodium").getValue(Long.class);
                        default_vitamin_A = dataSnapshot.child("vitamin_A").getValue(Long.class);
                        default_vitamin_C = dataSnapshot.child("vitamin_C").getValue(Long.class);

                        System.out.println("1: " + default_bad_fats );
                        System.out.println("2: " + default_calcium );
                        System.out.println("3: " + default_calories );
                        System.out.println("4: " + default_carbohydrates );
                        System.out.println("5: " + default_cholesterol);
                        System.out.println("6: " + default_fibre );
                        System.out.println("7: " + default_good_fats );
                        System.out.println("8: " + default_iron );
                        System.out.println("9: " + default_potassium );
                        System.out.println("10: " + default_protein);
                        System.out.println("11: " + default_sodium);
                        System.out.println("12: " + default_vitamin_A );
                        System.out.println("13: " + default_vitamin_C );
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, ": " + databaseError.getMessage());
                }
            });
        }
        //==================================

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
