package ca.infostages.infonut;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
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
import java.util.Objects;

public class StatisticsActivity extends AppCompatActivity {
    //define a piechart
    private PieChart mChart;

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

    private ListView nutrientListView;
    private ArrayAdapter<String> spinnerAdapter;

    String selected_plan = "";

    String label;
    double nutrientValue = 0;
    double intake = 100;
    int percent = 0;
    int full = 100;

    private int like_items;

    FirebaseUser currentUser;
    private static final String TAG = "StatisticsActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        mChart = (PieChart) findViewById(R.id.pie);
        nutrientListView = findViewById(R.id.nutrient_list_view);

        Legend legend = mChart.getLegend();
        legend.setTextSize(12f);
        legend.setTextColor(Color.WHITE);
        legend.setFormSize(10f);

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
                        System.out.println("Selected Plan: " + selected_plan);
                        populateSpinner();
                        planChecker(selected_plan);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, ": " + databaseError.getMessage());
                }
            });
            //entry label styling
            mChart.setEntryLabelColor(Color.WHITE);
            mChart.setEntryLabelTextSize(12f);
        }

        final boolean checkedServing = getIntent().getBooleanExtra("servingChecked", true);
        final double servingAmount = getIntent().getDoubleExtra("100Portion", 100);
        final int like_items = getIntent().getIntExtra("likeItem", 0);
        final double portion = getIntent().getDoubleExtra("sliderPortion", 1.0);

        populateSpinner();

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);

        List<PieEntry> pieEntries = new ArrayList<>();

        // The name of the chart
        PieDataSet dataSet = new PieDataSet(pieEntries, label);

        //Enable Back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Color of the chart entries
        if(100 == percent) {
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        } else {
            dataSet.setColor(Color.RED);
        }


        // Displaying the chart data
        PieData data = new PieData(dataSet);
        //data.setValueTextSize(12f); // data text size
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE); // data text color
        mChart.setData(data);
        mChart.animateY(1000); // Animation for the chart
        mChart.invalidate(); // refresh

        nutrientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = nutrientListView.getItemAtPosition(position).toString();
                if (value.equalsIgnoreCase("fat")) {
                    label = "Fat";
                    if(checkedServing) {
                        nutrientValue = hashmap.get("fat");
                    } else {
                        nutrientValue = hashmap.get("fat_100");
                        nutrientValue = consumptionManip(nutrientValue, servingAmount, intake, like_items, portion);
                    }
                    intake = default_bad_fats;
                    valueConverter(nutrientValue, intake);
                    chartSetting();
                    createChart();
                } else if (value.equalsIgnoreCase("good_fats")) {
                    if(checkedServing) {
                        nutrientValue = hashmap.get("goodFat");
                    } else {
                        nutrientValue = hashmap.get("goodFat_100");
                        nutrientValue = consumptionManip(nutrientValue, servingAmount, intake, like_items, portion);
                    }
                    intake = default_good_fats;
                    label = "Good Fat";
                    valueConverter(nutrientValue, intake);
                    chartSetting();
                    createChart();
                } else if (value.equalsIgnoreCase("bad_fats")) {
                    if(checkedServing) {
                        nutrientValue = hashmap.get("badFat");
                    } else {
                        nutrientValue = hashmap.get("badFat_100");
                        nutrientValue = consumptionManip(nutrientValue, servingAmount, intake, like_items, portion);
                    }
                    intake = default_bad_fats;
                    label = "Bad Fat";
                    valueConverter(nutrientValue, intake);
                    chartSetting();
                    createChart();
                } else if (value.equalsIgnoreCase("cholesterol")) {
                    label = "Cholesterol";
                    if(checkedServing) {
                        nutrientValue = hashmap.get("cholesterol");
                    } else {
                        nutrientValue = hashmap.get("cholesterol_100");
                        nutrientValue = consumptionManip(nutrientValue, servingAmount, intake, like_items, portion);
                    }
                    intake = default_cholesterol;
                    valueConverter(nutrientValue, intake);
                    chartSetting();
                } else if (value.equalsIgnoreCase("sodium")) {
                    label = "Sodium";
                    if(checkedServing) {
                        nutrientValue = hashmap.get("sodium");
                    } else {
                        nutrientValue = hashmap.get("sodium_100");
                        nutrientValue = consumptionManip(nutrientValue, servingAmount, intake, like_items, portion);
                    }
                    intake = default_sodium;
                    valueConverter(nutrientValue, intake);
                    chartSetting();
                    createChart();
                } else if (value.equalsIgnoreCase("carbohydrates")) {
                    label = "Carbohydrate";
                    intake = default_carbohydrates;
                    if(checkedServing) {
                        nutrientValue = hashmap.get("carbohydrate");
                        double nutval = nutrientValue;
                        nutrientValue = defaultManip(nutval, intake, like_items, portion);
                        System.out.println("Carbohydrate nut value: " + nutrientValue);
                    } else {
                        nutrientValue = hashmap.get("carbohydrate_100");
                        double nutval = nutrientValue;
                        nutrientValue = consumptionManip(nutval, servingAmount, intake, like_items, portion);
                        System.out.println("Carbohydrate nut value: " + nutrientValue);
                    }
                    valueConverter(nutrientValue, intake);
                    System.out.println(percent);
                    chartSetting();
                    createChart();
                } else if (value.equalsIgnoreCase("fibre")) {
                    label = "Fibre";
                    if(checkedServing) {
                        nutrientValue = hashmap.get("fibre");
                    } else {
                        nutrientValue = hashmap.get("fibre_100");
                        nutrientValue = consumptionManip(nutrientValue, servingAmount, intake, like_items, portion);
                    }
                    intake = default_fibre;
                    valueConverter(nutrientValue, intake);
                    chartSetting();
                    createChart();
                } else if (value.equalsIgnoreCase("potassium")) {
                    label = "Potassium";
                    if(checkedServing) {
                        nutrientValue = hashmap.get("potassium");
                    } else {
                        nutrientValue = hashmap.get("potassium_100");
                        nutrientValue = consumptionManip(nutrientValue, servingAmount, intake, like_items, portion);
                    }
                    intake = default_potassium;
                    valueConverter(nutrientValue, intake);
                    chartSetting();
                    createChart();
                } else if (value.equalsIgnoreCase("protein")) {
                    label = "Protein";
                    if(checkedServing) {
                        nutrientValue = hashmap.get("protein");
                    } else {
                        nutrientValue = hashmap.get("protein_100");
                        nutrientValue = consumptionManip(nutrientValue, servingAmount, intake, like_items, portion);
                    }
                    intake = default_protein;
                    valueConverter(nutrientValue, intake);
                    chartSetting();
                    createChart();
                } else if (value.equalsIgnoreCase("vitamin_a")) {
                    label = "Vitamin A";
                    if(checkedServing) {
                        nutrientValue = hashmap.get("vitaminA");
                    } else {
                        nutrientValue = hashmap.get("vitaminA_100");
                        nutrientValue = consumptionManip(nutrientValue, servingAmount, intake, like_items, portion);
                    }
                    intake = default_vitamin_A;
                    valueConverter(nutrientValue, intake);
                    chartSetting();
                    createChart();
                } else if (value.equalsIgnoreCase("vitamin_c")) {
                    label = "Vitamin C";
                    if(checkedServing) {
                        nutrientValue = hashmap.get("vitaminC");
                    } else {
                        nutrientValue = hashmap.get("vitaminC_100");
                        nutrientValue = consumptionManip(nutrientValue, servingAmount, intake, like_items, portion);
                    }
                    intake = default_vitamin_C;
                    valueConverter(nutrientValue, intake);
                    chartSetting();
                    createChart();
                } else if (value.equalsIgnoreCase("calcium")) {
                    label = "Calcium";

                    if(checkedServing) {
                        nutrientValue = hashmap.get("calcium");
                    } else {
                        nutrientValue = hashmap.get("calcium_100");
                        nutrientValue = consumptionManip(nutrientValue, servingAmount, intake, like_items, portion);
                    }
                    intake = default_calcium;
                    valueConverter(nutrientValue, intake);
                    chartSetting();
                    createChart();
                } else if (value.equalsIgnoreCase("iron")) {
                    label = "Iron";
                    if(checkedServing) {
                        nutrientValue = hashmap.get("iron");
                    } else {
                        nutrientValue = hashmap.get("iron_100");
                        nutrientValue = consumptionManip(nutrientValue, servingAmount, intake, like_items, portion);

                    }
                    intake = default_iron;
                    valueConverter(nutrientValue, intake);
                    chartSetting();
                    createChart();
                } else if (value.equalsIgnoreCase("calories")) {
                    label = "Calories";
                    if(checkedServing) {
                        nutrientValue = hashmap.get("calories");
                    } else {
                        nutrientValue = hashmap.get("calories_100");
                        nutrientValue = consumptionManip(nutrientValue, servingAmount, intake, like_items, portion);

                    }
                    intake = default_iron;
                    valueConverter(nutrientValue, intake);
                    chartSetting();
                    createChart();
                }
            }
        });
    }

    /**
     * Replaces all spaces with underscores and changes the case to lowercase.
     * @param title - the title of the plan.
     * @return a string that has been converted to lowercase with spaces replaced.
     */
    private String normalizePlanKey(String title) {
        return title.replaceAll("\\s+", "_").toLowerCase();
    }

    /**
     * Populates the spinner with keys from plans in the database.
     */
    private void populateSpinner() {
        // Check if default plan is selected or not.
        String planKey = normalizePlanKey(selected_plan);
        ArrayList<String> nutrientArrayList = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, nutrientArrayList);
        if (!planKey.equalsIgnoreCase("default_plan")) {
            customPlanPopulate(nutrientArrayList);
        } else {
            defaultPlanPopulate(nutrientArrayList);
        }
        nutrientListView.setAdapter(spinnerAdapter);
    }

    /**
     * Populates a list with keys retrieved from the default plan in the database.
     */
    private void defaultPlanPopulate(final ArrayList<String> spinnerList) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUser.getUid()).child("plan").child("default_plan");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equalsIgnoreCase("planTitle")) {
                        spinnerList.add(snapshot.getKey());
                    }
                }
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.wtf(TAG, databaseError.getDetails());
            }
        });
    }

    /**
     * Populates list with only valid keys from a custom plan.
     */
    private void customPlanPopulate(final ArrayList<String> spinnerArray) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users")
                .child(currentUser.getUid()).child("plan");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = (String) snapshot.child("planTitle").getValue();
                    if (!title.equalsIgnoreCase("default plan")) {
                        if (selected_plan.equalsIgnoreCase(title)) {
                            Plan plan = snapshot.getValue(Plan.class);
                            HashMap<String, Double> planNutrients = Objects.requireNonNull(plan).getNutrients();
                            for (String key : planNutrients.keySet()) {
                                if (planNutrients.get(key) > 0) {
                                    spinnerArray.add(key);
                                }
                            }
                            spinnerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.wtf(TAG, databaseError.getDetails());
            }
        });
    }

    private void createChart() {
        List<PieEntry> pieEntries = new ArrayList<>();

        // The name of the chart
        PieDataSet dataSet = new PieDataSet(pieEntries, label);

        // Color array for the pie chart
        final int[] MY_COLORS = {Color.rgb(255,0,0), Color.rgb(255,0,0)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        if(intake == 0) {
            pieEntries.add(new PieEntry(100, label));
            dataSet.setColors(colors);
        } else {
            pieEntries.add(new PieEntry(full, "Intake"));
            pieEntries.add(new PieEntry(percent, label));
            // Color of the chart entries
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        }

        // Displaying the chart data
        PieData data = new PieData(dataSet);
        mChart.setData(data);
        mChart.animateY(1000); // Animation for the chart
        mChart.invalidate(); // refresh
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

    private double consumptionManip(double nutrition, double amount, double intake, int like, double portion) {
        System.out.println("CONSUMPTION MANIP");
        System.out.println("Consumption value: " + nutrition);
        System.out.println("Amount value: " + amount);
        System.out.println("portion value: " + portion);
        System.out.println("Intake value:" + intake);
        System.out.println("Nutrition value: " + nutrition);
        amount /= 100;

        double product = nutrition * amount * portion;
        if(like != 0) {
            product *= like;
        }
        product /= intake;
        product *= 100;
        System.out.println("product value33: " + product);

        return product;
    }

    private double defaultManip(double nutrition, double intake, int like, double portion) {
        double product = nutrition * portion;
        if(like != 0) {
            product *= like;
        }
        product /= intake;
        product *= 100;
        return product;
    }

    private void valueConverter(double value, double remain) {
        percent = (int) value;
        full = 100 - percent;
    }

    private void planChecker(final String plan) {
        System.out.println("PLAN NAME: " + plan);

        final String newPlan = plan.toLowerCase();
        if(!plan.equals("default plan")) {
            DatabaseReference planReference;

            planReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("plan").child(newPlan).child("nutrients");
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
                    default_vitamin_A = dataSnapshot.child("vitamin_a").getValue(Long.class);
                    default_vitamin_C = dataSnapshot.child("vitamin_c").getValue(Long.class);

                    System.out.println("Plan Carbs: " + default_carbohydrates);
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

                    System.out.println("Default Carbs: " + default_carbohydrates);
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
        if(percent <= 100) {
            mChart.setCenterText(percent + "%");
        } else {
            mChart.setCenterText("Warning Exceeded Intake\n" + percent + "%");
        }
        mChart.setCenterTextSize(14f);
        mChart.setCenterTextColor(Color.BLUE);
    }
}