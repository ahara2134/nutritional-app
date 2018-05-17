package ca.infostages.infonut;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
    References :
    1) https://www.canada.ca/en/health-canada/services/food-nutrition/
    healthy-eating/dietary-reference-intakes/tables/reference-values-elements-dietary-
    reference-intakes-tables-2005.html

    2) https://www.canada.ca/en/health-canada/services/food-nutrition/healthy-eating/
    dietary-reference-intakes/tables/reference-values-vitamins-dietary-reference-
    intakes-tables-2005.html

    3) https://www.canada.ca/en/health-canada/services/food-nutrition/healthy-eating/
    dietary-reference-intakes/tables/reference-values-macronutrients-dietary-reference-
    intakes-tables-2005.html
 */
public class NewUser2Fragment extends Fragment implements View.OnClickListener{

    public interface NewUser2FragmentListener {
        void submitForm();
    }

    private NewUser2FragmentListener listener;

    //declare database objects
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth firebaseAuth;

    //declare GUI objects
    private Spinner ageSpinner, weightSpinner;
    private EditText weightText;
    private Button submitButton;
    private String age, weightValue, gender;
    private int activeDB, ageDB;
    private double weight;
    private boolean ageIsValid, weightIsValid;

    /**
     * Creates a new instance of this class and returns it to the caller.
     * @return a NewUser2Fragment
     */
    public static NewUser2Fragment newInstance() {
        return new NewUser2Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_new_user2, container, false);

        //get gender and active level from previous activity
        Bundle extras = getArguments();
        if (extras != null) {
            activeDB = extras.getInt("activeLevel");
            gender = extras.getString("textGender");
        }
        //instantiate database objects
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();

        ageSpinner = view.findViewById(R.id.spinner_age);

        weightSpinner = view.findViewById(R.id.spinner_weight);
        weightText = view.findViewById(R.id.editText_weight);
        submitButton = view.findViewById(R.id.button_submit);
        submitButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (NewUser2FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement " +
                    "NewUser2FragmentListener");
        }
    }

    @Override
    public void onClick(View view) {
        age = ageSpinner.getSelectedItem().toString();
        weightValue = weightSpinner.getSelectedItem().toString();
        weight = Double.parseDouble(weightText.getText().toString());
        CheckEditTextIsEmptyOrNot();
        if (!weightIsValid) {
            Toast.makeText(getContext(), getString(R.string.weight_error), Toast.LENGTH_LONG).show();
        } else if(!ageIsValid) {
            Toast.makeText(getContext(), getString(R.string.age_error), Toast.LENGTH_LONG).show();
        } else {
            //Convert lbs to kg
            if (weightValue.equals(getString(R.string.weight_spinner_2))) {
                weight = weight / 2.205;
            }
            determinePlan();
            //Writing to database
            String uID = mUser.getUid();
            mDatabase.child("users").child(uID).child("age").setValue(age);
            mDatabase.child("users").child(uID).child("lbs").setValue(weight);
            listener.submitForm();
        }
    }

    /**
     * Checks if EditText is empty or not
     */
    public void CheckEditTextIsEmptyOrNot() {
        if (age.equals(getString(R.string.age_spinner_1))) {
            ageIsValid = false;
        } else {
            ageIsValid = true;
        }

        //If the weight is in pounds, and they are either 1) over 500lbs OR 2) under 40lbs
        if (weightValue.equals(getString(R.string.weight_spinner_1))) {
            if (weight >= 500.0 || weight <= 40.0) {
                weightIsValid = false;
            } else {
                weightIsValid = true;
            }
        }

        //If the weight is in kg, and they are either 1) over 227kg OR 2) under 18kg
        if (weightValue.equals(getString(R.string.weight_spinner_2))) {
            if (weight >= 227.0 || weight <= 18.0) {
                weightIsValid = false;
            } else {
                weightIsValid = true;
            }
        }
    }

    //Determine what plan to assign to the user
    public void determinePlan() {
        setAgeDB();
        int calorie = calculateCalorie();
        double carbs = calculateCarbs(calorie);
        double fats = calculateFat(calorie);
        double badFats = calculateBadFats(calorie);
        double protein = calculateProtein();
        int sodium = calculateSodium();
        int iron = calculateIron();
        int calcium = calculateCalcium();
        int cholesterol = 300;
        int vitA = calculateVitA();
        int vitC = calculateVitC();
        int fibre = calculateFibre();
        int potassium = calculatePotassium();

        String uID = mUser.getUid();
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("calories").setValue(calorie);
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("good_fats").setValue(fats);
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("bad_fats").setValue(badFats);
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("cholesterol").setValue(cholesterol);
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("sodium").setValue(sodium);
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("potassium").setValue(potassium);
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("carbohydrates").setValue(carbs);
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("fibre").setValue(fibre);
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("protein").setValue(protein);
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("iron").setValue(iron);
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("calcium").setValue(calcium);
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("vitamin_A").setValue(vitA);
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("vitamin_C").setValue(vitC);
        mDatabase.child("users").child(uID).child("plan").child("default_plan").child("planTitle").setValue("default plan");
        mDatabase.child("users").child(uID).child("selected_plan").setValue("default_plan");
    }

    //Convert age groups into number groups for switch case
    public void setAgeDB() {
        if (age.equals(getString(R.string.age_spinner_2))) {
            ageDB = 1;
        } else if (age.equals(getString(R.string.age_spinner_3))) {
            ageDB = 2;
        } else if (age.equals(getString(R.string.age_spinner_4))) {
            ageDB = 3;
        } else if (age.equals(getString(R.string.age_spinner_5))) {
            ageDB = 4;
        } else if (age.equals(getString(R.string.age_spinner_6))) {
            ageDB = 5;
        } else if (age.equals(getString(R.string.age_spinner_7))) {
            ageDB = 6;
        } else if (age.equals(getString(R.string.age_spinner_8))) {
            ageDB = 7;
        } else if (age.equals(getString(R.string.age_spinner_9))) {
            ageDB = 8;
        }
    }

    //Determine how many calories they should be taking
    public int calculateCalorie() {
        if (activeDB == 1 && gender.equals(getString(R.string.gender_female))) {  //If activity level = SEDANTARY, and female
            switch (ageDB) {
                case 1:
                    return 1500;
                case 2:
                    return 1700;
                case 3:
                    return 1750;
                case 4:
                    return 1750;
                case 5:
                    return 1900;
                case 6:
                    return 1800;
                case 7:
                    return 1650;
                case 8:
                    return 1550;
            }
        } else if (activeDB == 1 && gender.equals(getString(R.string.gender_male))) {
            switch (ageDB) {
                case 1:
                    return 1700;
                case 2:
                    return 1900;
                case 3:
                    return 2300;
                case 4:
                    return 2450;
                case 5:
                    return 2500;
                case 6:
                    return 2350;
                case 7:
                    return 2150;
                case 8:
                    return 2000;
            }
        } else if (activeDB == 2 && gender.equals(getString(R.string.gender_female))) {
            switch (ageDB) {
                case 1:
                    return 1800;
                case 2:
                    return 2000;
                case 3:
                    return 2100;
                case 4:
                    return 2100;
                case 5:
                    return 2100;
                case 6:
                    return 2000;
                case 7:
                    return 1850;
                case 8:
                    return 1750;
            }
        } else if (activeDB == 2 && gender.equals(getString(R.string.gender_male))) {
            switch (ageDB) {
                case 1:
                    return 2000;
                case 2:
                    return 2250;
                case 3:
                    return 2700;
                case 4:
                    return 2900;
                case 5:
                    return 2700;
                case 6:
                    return 2600;
                case 7:
                    return 2350;
                case 8:
                    return 2200;
            }
        } else if (activeDB == 3 && gender.equals(getString(R.string.gender_female))) {
            switch (ageDB) {
                case 1:
                    return 2050;
                case 2:
                    return 2250;
                case 3:
                    return 2350;
                case 4:
                    return 2400;
                case 5:
                    return 2350;
                case 6:
                    return 2250;
                case 7:
                    return 2100;
                case 8:
                    return 2000;
            }
        } else {
            switch (ageDB) {
                case 1:
                    return 2300;
                case 2:
                    return 2600;
                case 3:
                    return 3100;
                case 4:
                    return 3300;
                case 5:
                    return 3000;
                case 6:
                    return 2900;
                case 7:
                    return 2650;
                case 8:
                    return 2500;
            }
        }
        return 0;
    }

    //Determine how much carbohydrates
    public double calculateCarbs(int cal) {
        double calPerCarb = cal * 0.45;
        return calPerCarb / 4.0;
    }

    //Determine how much fats
    public double calculateFat(int cal) {
        if(ageDB == 1 || ageDB == 2 || ageDB == 3 || ageDB == 4) {
            double calByFat = (double)cal * 0.25;
            return calByFat / 9;
        } else {
            double calByFat = (double)cal * 0.20;
            return calByFat / 9;
        }
    }

    //Determine how much saturated and transfat is needed
    public double calculateBadFats(int cal) {
        double calByFat = (double) cal * 0.10;
        return calByFat / 9;
    }

    //Determine how much protein is recommended
    public double calculateProtein() {
        if (ageDB == 1 || ageDB == 2) {
            return weight * 0.95;
        } else if (ageDB == 3 || ageDB == 4) {
            return weight * 0.85;
        } else {
            return weight * 0.8;
        }
    }

    public int calculateIron() {
        if (gender.equals(getString(R.string.gender_male))) {
            if (ageDB == 3 || ageDB == 4) {
                return 11;
            } else {
                return 8;
            }
        } else {
            if (gender.equals(getString(R.string.gender_female))) {
                if (ageDB == 5 || ageDB == 6) {
                    return 18;
                } else  if(ageDB == 3 || ageDB == 4){
                    return 15;
                } else {
                    return 8;
                }
            }
        }
        return 0;
    }

    public int calculateSodium() {
        if (ageDB == 1 || ageDB == 2 || ageDB == 3 ||
                ageDB == 4 || ageDB == 5 || ageDB == 6) {
            return 1500;
        } else if (ageDB == 7) {
            return 1300;
        } else {
            return 1200;
        }
    }

    public int calculateCalcium() {
        if (gender.equals(getString(R.string.gender_male))) {
            if (ageDB == 1 || ageDB == 2 || ageDB == 3 || ageDB == 4) {
                return 1300;
            } else if (ageDB == 5 || ageDB == 6 || ageDB == 7) {
                return 1000;
            } else {
                return 1200;
            }
        } else {
            if (gender.equals(getString(R.string.gender_female))) {
                if (ageDB == 1 || ageDB == 2 || ageDB == 3 || ageDB == 4) {
                    return 1300;
                } else if (ageDB == 5 || ageDB == 6) {
                    return 1000;
                } else {
                    return 1200;
                }
            }
        }
        return 0;
    }

    public int calculateVitA() {
        if (gender.equals(getString(R.string.gender_male))) {
            if (ageDB == 1 || ageDB == 2 ) {
                return 600;
            } else {
                return 900;
            }
        } else {
            if (gender.equals(getString(R.string.gender_female))) {
                if (ageDB == 1 || ageDB == 2) {
                    return 600;
                } else {
                    return 700;
                }
            }
        }
        return 0;
    }

    public int calculateVitC() {
        if (gender.equals(getString(R.string.gender_male))) {
            if (ageDB == 1 || ageDB == 2 ) {
                return 45;
            } else if(ageDB == 3 || ageDB == 4) {
                return 75;
            } else{
                return 90;
            }
        } else {
            if (gender.equals(getString(R.string.gender_female))) {
                if (ageDB == 1 || ageDB == 2 ) {
                    return 45;
                } else if(ageDB == 3 || ageDB == 4) {
                    return 65;
                } else{
                    return 75;
                }
            }
        }
        return 0;
    }

    public int calculateFibre() {
        if (gender.equals(getString(R.string.gender_male))) {
            if (ageDB == 1 || ageDB == 2) {
                return 31;
            } else if (ageDB == 8 || ageDB == 7) {
                return 30;
            } else {
                return 38;
            }
        } else {
            if (gender.equals(getString(R.string.gender_female))) {
                if (ageDB == 1 || ageDB == 2 || ageDB == 3 || ageDB == 4) {
                    return 26;
                } else if (ageDB == 5 || ageDB == 6) {
                    return 25;
                } else {
                    return 21;
                }
            }
        }
        return 0;
    }

    public int calculatePotassium() {
        if (ageDB == 1 || ageDB == 2 ) {
            return 4500;
        } else {
            return 4700;
        }
    }
}
