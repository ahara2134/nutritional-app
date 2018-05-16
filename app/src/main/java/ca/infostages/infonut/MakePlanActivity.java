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
        nutrientIntakeList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference();
        title = findViewById(R.id.plan_title_edit);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
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
        HashMap<String, Double> hashMap = new HashMap<>();
        int nutrientCount = nutrientIntakeList.getChildCount();
        if (nutrientCount > 0) {
            for (int index = 0; index < nutrientIntakeList.getChildCount();++index) {
                NutrientsAdapter.ViewHolder holder =
                        (NutrientsAdapter.ViewHolder) nutrientIntakeList
                        .findViewHolderForAdapterPosition(index);
                hashMap.put(holder.nameTextView.getText().toString().replaceAll("\\s+",
                        "_").toLowerCase(),
                        Double.parseDouble(holder.limitEditText.getText().toString()));
            }
            Plan plan = new Plan(title.getText().toString(), hashMap, false);
            if (user != null) {
                mDatabase.child("users")
                        .child(user.getUid())
                        .child("plan")
                        .push()
                        .setValue(plan);
            }
            Toast planSavedToast = Toast.makeText(this,
                    "Your custom plan has been saved. Go to settings to activate it.",
                    Toast.LENGTH_LONG);
            TextView textView = planSavedToast.getView().findViewById(android.R.id.message);
            if (textView != null)
                textView.setGravity(Gravity.CENTER);
            planSavedToast.show();
            finish();
        } else {
            Toast chooseNutrientsToast = Toast.makeText(this,
                    "Please add nutrients to the plan",
                    Toast.LENGTH_LONG);
            chooseNutrientsToast.show();
        }
    }
}
