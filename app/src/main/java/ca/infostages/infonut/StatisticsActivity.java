package ca.infostages.infonut;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    //define a piechart
    private PieChart mChart;

    //define all the buttons
    private Button fat;
    private Button goodFat;
    private Button badFat;
    private Button cholesterol;
    private Button sodium;
    private Button carbohydrate;
    private Button fibre;
    private Button potassium;
    private Button protein;
    private Button vitaminA;
    private Button vitaminC;
    private Button calcium;
    private Button iron;
    private Button calories;

    //default plans
    private Long default_bad_fats = 0l;
    private Long default_calcium = 0l;
    private Long default_calories = 0l;
    private Long default_carbohydrates = 0l;
    private Long default_cholesterol = 0l;
    private Long default_fibre = 0l;
    private Long default_good_fats = 0l;
    private Long default_iron = 0l;
    private Long default_potassium = 0l;
    private Long default_protein = 0l;
    private Long default_sodium = 0l;
    private Long default_vitamin_A = 0l;
    private Long default_vitamin_C = 0l;

    String selected_plan = "";

    String label;
    double nutrientValue = 0;
    double intake = 100;
    int percent = 0;
    int full = 100;

    FirebaseUser currentUser;
    private static final String TAG = "StatisticsActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        mChart = (PieChart) findViewById(R.id.pie);

        fat = findViewById(R.id.fat);
        goodFat = findViewById(R.id.saturatedFat);
        badFat = findViewById(R.id.transFat);
        cholesterol = findViewById(R.id.cholesterol);
        sodium = findViewById(R.id.sodium);
        carbohydrate = findViewById(R.id.carbohydrate);
        fibre = findViewById(R.id.fibre);
        potassium = findViewById(R.id.sugar);
        protein = findViewById(R.id.protein);
        vitaminA = findViewById(R.id.vitaminA);
        vitaminC = findViewById(R.id.vitaminC);
        calcium = findViewById(R.id.calcium);
        iron = findViewById(R.id.iron);
        calories = findViewById(R.id.calories);


        final HashMap<String, Double> hashmap = NutritionData.nutritionHashMap;

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            Intent intent = new Intent(StatisticsActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            //Checks if user's demographics are entered in. If not, send to NewUserActivity.
            DatabaseReference planReference;

            planReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            planReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String plan = dataSnapshot.getValue().toString();
                    if (plan.equals("false")) {
                        Intent intent = new Intent(StatisticsActivity.this, Home.class);
                        startActivity(intent);
                    } else {
                        selected_plan = dataSnapshot.child("selected_plan").getValue(String.class);
                        System.out.println("Selected Plan123: " + selected_plan);
                        planChecker(selected_plan);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, ": " + databaseError.getMessage());
                }
            });
//            // entry label styling
//            mChart.setEntryLabelColor(Color.WHITE);
//            mChart.setEntryLabelTextSize(12f);
        }

        System.out.println("Selected Plan456: " + selected_plan);




        final boolean checkedServing = getIntent().getBooleanExtra("servingChecked", true);
        final double servingAmount = getIntent().getDoubleExtra("100Portion", 100);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);

        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(95, "Intake"));
        pieEntries.add(new PieEntry(5, "Test"));
        // The name of the chart
        PieDataSet dataSet = new PieDataSet(pieEntries, label);

        //Enable Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Color of the chart entries
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        // Displaying the chart data
        PieData data = new PieData(dataSet);
        mChart.setData(data);
        mChart.animateY(1000); // Animation for the chart
        mChart.invalidate(); // refresh

        /**
         * Buttons assigning starts here.
         * Calories is just a placeholder; due to the API not
         * supporting calories.
         */

        calories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Current API does not include calories.
            }
        });

        fat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Fat";
                if(checkedServing) {
                    nutrientValue = hashmap.get("fat");
                } else {
                    nutrientValue = hashmap.get("fat_100");
                    nutrientValue = consumptionManip(nutrientValue, servingAmount);
                }
                intake = default_bad_fats;
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                chartSetting();
                createChart();
                fat.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                fat.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });

        goodFat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkedServing) {
                    nutrientValue = hashmap.get("goodFat");
                } else {
                    nutrientValue = hashmap.get("goodFat_100");
                    nutrientValue = consumptionManip(nutrientValue, servingAmount);
                }
                intake = default_good_fats;
                buttonChange();
                label = "Good Fat";
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                chartSetting();
                createChart();
                goodFat.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                goodFat.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });

        badFat .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(checkedServing) {
                    nutrientValue = hashmap.get("badFat");
                } else {
                    nutrientValue = hashmap.get("badFat_100");
                    nutrientValue = consumptionManip(nutrientValue, servingAmount);
                }
                intake = default_bad_fats;
                buttonChange();
                label = "Bad Fat";
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                chartSetting();
                createChart();
                badFat.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                badFat.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });

        cholesterol .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Cholesterol";
                if(checkedServing) {
                    nutrientValue = hashmap.get("cholesterol");
                } else {
                    nutrientValue = hashmap.get("cholesterol_100");
                    nutrientValue = consumptionManip(nutrientValue, servingAmount);
                }
                intake = default_cholesterol;
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                chartSetting();
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
                if(checkedServing) {
                    nutrientValue = hashmap.get("sodium");
                } else {
                    nutrientValue = hashmap.get("sodium_100");
                    nutrientValue = consumptionManip(nutrientValue, servingAmount);
                }
                intake = default_sodium;
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                chartSetting();
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
                if(checkedServing) {
                    nutrientValue = hashmap.get("carbohydrate");
                } else {
                    nutrientValue = hashmap.get("carbohydrate_100");
                    nutrientValue = consumptionManip(nutrientValue, servingAmount);
                }
                intake = default_carbohydrates;
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                chartSetting();
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
                if(checkedServing) {
                    nutrientValue = hashmap.get("fibre");
                } else {
                    nutrientValue = hashmap.get("fibre_100");
                    nutrientValue = consumptionManip(nutrientValue, servingAmount);
                }
                intake = default_fibre;
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                chartSetting();
                createChart();
                fibre.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                fibre.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });

        potassium .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Potassium";
                if(checkedServing) {
                    nutrientValue = hashmap.get("potassium");
                } else {
                    nutrientValue = hashmap.get("potassium_100");
                    nutrientValue = consumptionManip(nutrientValue, servingAmount);
                }
                intake = default_potassium;
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                chartSetting();
                createChart();
                potassium.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                potassium.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });
        protein .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                buttonChange();
                label = "Protein";
                if(checkedServing) {
                    nutrientValue = hashmap.get("protein");
                } else {
                    nutrientValue = hashmap.get("protein_100");
                    nutrientValue = consumptionManip(nutrientValue, servingAmount);
                }
                intake = default_protein;
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                chartSetting();
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
                if(checkedServing) {
                    nutrientValue = hashmap.get("vitaminA");
                } else {
                    nutrientValue = hashmap.get("vitaminA_100");
                    nutrientValue = consumptionManip(nutrientValue, servingAmount);
                }
                intake = default_vitamin_A;
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                chartSetting();
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
                if(checkedServing) {
                    nutrientValue = hashmap.get("vitaminC");
                } else {
                    nutrientValue = hashmap.get("vitaminC_100");
                    nutrientValue = consumptionManip(nutrientValue, servingAmount);
                }
                intake = default_vitamin_C;
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                chartSetting();
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
                if(checkedServing) {
                    nutrientValue = hashmap.get("calcium");
                } else {
                    nutrientValue = hashmap.get("calcium_100");
                    nutrientValue = consumptionManip(nutrientValue, servingAmount);
                }
                intake = default_calcium;
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                chartSetting();
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
                if(checkedServing) {
                    nutrientValue = hashmap.get("iron");
                } else {
                    nutrientValue = hashmap.get("iron_100");
                    nutrientValue = consumptionManip(nutrientValue, servingAmount);

                }
                intake = default_iron;
                System.out.println("Nut value: "+ nutrientValue);
                valueConverter(nutrientValue, intake);
                chartSetting();
                createChart();
                iron.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                iron.setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });

        //

    }

    /**
     * Changes the background and text color of all the buttons
     */
    private void buttonChange(){
        iron.getBackground().clearColorFilter();
        iron.setTextColor(getResources().getColor(R.color.white));
        fat.getBackground().clearColorFilter();
        fat.setTextColor(getResources().getColor(R.color.white));
        goodFat.getBackground().clearColorFilter();
        goodFat.setTextColor(getResources().getColor(R.color.white));
        badFat.getBackground().clearColorFilter();
        badFat.setTextColor(getResources().getColor(R.color.white));
        cholesterol.getBackground().clearColorFilter();
        cholesterol.setTextColor(getResources().getColor(R.color.white));
        sodium.getBackground().clearColorFilter();
        sodium.setTextColor(getResources().getColor(R.color.white));
        carbohydrate.getBackground().clearColorFilter();
        carbohydrate.setTextColor(getResources().getColor(R.color.white));
        fibre.getBackground().clearColorFilter();
        fibre.setTextColor(getResources().getColor(R.color.white));
        potassium.getBackground().clearColorFilter();
        potassium.setTextColor(getResources().getColor(R.color.white));
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

    /**
     * This will take the user back to the previous activity
     * @param item what button is being selected
     * @return super.onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Both navigation bar back press and title bar back press will trigger this method
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed(); // In this case, this will always go to this
        }
    }

    private double consumptionManip(double nutrition, double amount) {
        return amount/100 *nutrition;
    }

    private void planChecker(final String plan) {
        System.out.println("PLAN NAME: " + plan);

        final String newPlan = plan.toLowerCase();
        if(!plan.equals("default_plan")) {
            DatabaseReference planReference;

            planReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("plan").child(newPlan).child("nutrients");
            planReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("CURRENTLY IN:" + newPlan);
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
                    default_vitamin_A = dataSnapshot.child("vitamin_a").getValue(Long.class);
                    default_vitamin_C = dataSnapshot.child("vitamin_c").getValue(Long.class);

                    System.out.println("DEFAULT CARBS: " + default_carbohydrates);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, ": " + databaseError.getMessage());
                }
            });
        } else {
            DatabaseReference planReference;

            planReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("plan").child("default_plan");
            planReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
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

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, ": " + databaseError.getMessage());
                }
            });
        }

    }

    public void chartSetting(){
        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);
        mChart.setCenterText(percent + "%" );
        mChart.setCenterTextSize(14f);
        mChart.setCenterTextColor(Color.BLUE);
    }

}
