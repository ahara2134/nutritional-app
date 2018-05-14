package ca.infostages.infonut;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MakePlanActivity extends AppCompatActivity implements NutrientDialogFragment.NutrientDialogListener {

    private static final String TAG_NUTRIENT_DIALOG = "NUTRIENT_DIALOG";
    private ArrayList<Nutrient> mNutrients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onFinishEditDialog(ArrayList<Integer> nutrients) {
        RecyclerView nutrientIntakeList = findViewById(R.id.nutrients_recycler);
        mNutrients = new ArrayList<>();

        if (nutrients != null && nutrients.size() > 0) {
            for (Integer arrayIndex : nutrients) {
                Nutrient nutrient = new Nutrient(
                        getResources().getStringArray(R.array.nutrient_list)[arrayIndex],
                        0);
                mNutrients.add(nutrient);
            }
        }

        NutrientsAdapter adapter = new NutrientsAdapter(mNutrients);
        nutrientIntakeList.setAdapter(adapter);
        nutrientIntakeList.setLayoutManager(new LinearLayoutManager(this));
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
}
