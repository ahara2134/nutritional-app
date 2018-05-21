package ca.infostages.infonut;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Activity for user plan creation and
 */
public class MakePlanActivity extends AppCompatActivity
        implements NutrientDialogFragment.NutrientDialogListener {

    private static final String TAG_NUTRIENT_DIALOG = "NUTRIENT_DIALOG";
    private EditText title;
    private ArrayList<Nutrient> mNutrients;
    private NutrientsAdapter adapter;
    private DatabaseReference mDatabase;
    private RecyclerView nutrientIntakeList;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        nutrientIntakeList = findViewById(R.id.nutrients_recycler);
        mNutrients = new ArrayList<>();
        adapter = new NutrientsAdapter(mNutrients);
        nutrientIntakeList.setAdapter(adapter);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        nutrientIntakeList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users")
                .child(user.getUid()).child("plan");
        title = findViewById(R.id.plan_title_edit);
    }

    @Override
    public void onFinishEditDialog(ArrayList<Integer> nutrients) {
        if (nutrients != null && nutrients.size() > 0) {
            for (Integer arrayIndex : nutrients) {
                Nutrient nutrient = new Nutrient(
                        getResources().getStringArray(R.array.nutrient_list)[arrayIndex],
                        0);
                mNutrients.add(nutrient);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Shows a dialog which has a list of available nutrients that users can pick from.
     * @param view - view
     */
    public void addNutrientOrIngredient(View view) {
        DialogFragment dialogFragment = new NutrientDialogFragment();
        if (mNutrients != null && mNutrients.size() > 0) {
            Bundle bundle = new Bundle();
            ArrayList<String> preSavedNutrients = new ArrayList<>();
            for (Nutrient nutrient : mNutrients) {
                preSavedNutrients.add(nutrient.getNutrientName());
            }
            bundle.putStringArrayList("nutrients", preSavedNutrients);
            dialogFragment.setArguments(bundle);
        }
        dialogFragment.show(getSupportFragmentManager(), TAG_NUTRIENT_DIALOG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Writes the plan to Firebase and closes the activity.
     * @param view - view
     */
    public void submitPlan(View view) {
        int nutrientCount = nutrientIntakeList.getChildCount();
        if (nutrientCount > 0) {
            HashMap<String, Double> hashMap = new HashMap<>();
            for (int index = 0; index < nutrientIntakeList.getChildCount();++index) {
                NutrientsAdapter.ViewHolder holder =
                        (NutrientsAdapter.ViewHolder) nutrientIntakeList
                                .findViewHolderForAdapterPosition(index);
                hashMap.put(formatString(holder.nameTextView.getText().toString()),
                        Double.parseDouble(holder.limitEditText.getText().toString()));
            }
            updateMapWithMissingValues(hashMap);
            savePlan(hashMap);
            finish();
            Toast uniqueTitleToast = Toast.makeText(MakePlanActivity.this,
                    "Plan title already exists",
                    Toast.LENGTH_LONG);
            uniqueTitleToast.show();
        } else {
            Toast chooseNutrientsToast = Toast.makeText(this,
                    "Please add nutrients to the plan",
                    Toast.LENGTH_LONG);
            chooseNutrientsToast.show();
        }
    }

    /**
     * Populates the hashmap with the remaining values of the nutrient list.
     * @param hashMap - HashMap featuring all values that the user has specified.
     */
    private void updateMapWithMissingValues(HashMap<String, Double> hashMap) {
        String[] stringArray = getResources().getStringArray(R.array.nutrient_list);
        for (String nutrient : stringArray) {
            String match = formatString(nutrient);
            if (!hashMap.containsKey(match)) {
                hashMap.put(match, 0.0);
            }
        }
    }

    /**
     * Saves a plan object to Firebase.
     * @param hashMap - a HashMap of user inputted nutrients.
     */
    private void savePlan(HashMap<String, Double> hashMap) {
        Plan plan = new Plan(title.getText().toString(), hashMap);
        if (user != null) {
            mDatabase.child(formatString(plan.getPlanTitle())).setValue(plan);
        }
        Toast planSavedToast = Toast.makeText(this,
                "Your custom plan has been saved. Go to settings to activate it.",
                Toast.LENGTH_LONG);
        TextView textView = planSavedToast.getView().findViewById(android.R.id.message);
        if (textView != null)
            textView.setGravity(Gravity.CENTER);
        planSavedToast.show();
    }

    /**
     * Replaces all spaces with underscores and makes all characters lowercase.
     * @param string - string gained from user input.
     * @return a String value that can be safely written to the database.
     */
    private String formatString(String string) {
        String regex = "\\s+";
        return string.replaceAll(regex, "_").toLowerCase();
    }
}
