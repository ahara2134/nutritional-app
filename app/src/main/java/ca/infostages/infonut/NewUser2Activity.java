package ca.infostages.infonut;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewUser2Activity extends AppCompatActivity implements View.OnClickListener{
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
    private boolean ageIsEmpty, weightIsEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user2);

        //get gender and active level from previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            activeDB = extras.getInt("activeLevel");
            gender = extras.getString("textGender");
        }
        //instantiate database objects
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();

        ageSpinner = (Spinner)findViewById(R.id.spinner_age);
        age = ageSpinner.getSelectedItem().toString();

        weightSpinner = (Spinner)findViewById(R.id.spinner_weight);
        weightValue = weightSpinner.getSelectedItem().toString();

        submitButton = (Button)findViewById(R.id.button_submit);
        submitButton.setOnClickListener(this);

        weightText = (EditText)findViewById(R.id.editText_weight);
        weight = Double.parseDouble(weightText.getText().toString());
    }

    @Override
    public void onClick(View v) {
        CheckEditTextIsEmptyOrNot();
        if(!weightIsEmpty || !ageIsEmpty) {
            Toast.makeText(NewUser2Activity.this, getString(R.string.weight_error), Toast.LENGTH_LONG).show();
        } else {
            //Convert lbs to kg
            if(weightValue.equals(getString(R.string.weight_spinner_2))) {
                weight=  weight/2.205;
            }
            determinePlan();
            //Writing to database
            String uID = mUser.getUid();
            mDatabase.child("users").child(uID).child("age").setValue(age);
            mDatabase.child("users").child(uID).child("lbs").setValue(weight);
        }


    }

    //check if EditText is empty or not
    public void CheckEditTextIsEmptyOrNot() {
        if (age.equals(getString(R.string.age_spinner_1))) {
            ageIsEmpty = false;
        }

        //If the weight is in pounds, and they are either 1) over 500lbs OR 2) under 40lbs
        if (weightValue.equals(getString(R.string.weight_spinner_1))) {
            if (weight >= 500 || weight <= 40) {
                weightIsEmpty = false;
            } else {
                weightIsEmpty = true;
            }
        }

        //If the weight is in kg, and they are either 1) over 227kg OR 2) under 18kg
        if (weightValue.equals(getString(R.string.weight_spinner_2))) {
            if(weight >=227 || weight <= 18) {
                weightIsEmpty = false;
            } else {
                weightIsEmpty = true;
            }
        }
    }

    //Determine what plan to assign to the user
    public void determinePlan() {
        setAgeDB();
        double protein = weight * 0.8;
        int calorie = calculateCalorie();
        double carbs = calculateCarbs(calorie);
        double fats = calculateFat(calorie);
        double badFats = calculateBadFats(calorie);

    }

    //Convert age groups into number groups for switch case
    public void setAgeDB() {
        if(age.equals(getString(R.string.age_spinner_2))) {
            ageDB = 1;
        } else if(age.equals(getString(R.string.age_spinner_3))) {
            ageDB = 2;
        } else if(age.equals(getString(R.string.age_spinner_4))) {
            ageDB = 3;
        } else if(age.equals(getString(R.string.age_spinner_5))) {
            ageDB = 4;
        } else if(age.equals(getString(R.string.age_spinner_6))) {
            ageDB = 5;
        } else if(age.equals(getString(R.string.age_spinner_7))) {
            ageDB = 6;
        } else if(age.equals(getString(R.string.age_spinner_8))) {
            ageDB = 7;
        } else if(age.equals(getString(R.string.age_spinner_9))) {
            ageDB = 8;
        }
    }

    //Determine how many calories they should be taking
    public int calculateCalorie() {
        if(activeDB == 1 && gender.equals(getString(R.string.gender_female))) {  //If activity level = SEDANTARY, and female
            switch(ageDB) {
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
        } else if(activeDB == 1 && gender.equals(getString(R.string.gender_male))) {
            switch(ageDB) {
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
        } else if(activeDB == 2 && gender.equals(getString(R.string.gender_female))) {
            switch(ageDB) {
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
        } else if(activeDB == 2 && gender.equals(getString(R.string.gender_male))) {
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
        }else if(activeDB == 3 && gender.equals(getString(R.string.gender_female))) {
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
        double carbPerCalorie = 300 / 2000;
        return cal * carbPerCalorie;
    }

    //Determine how much fats
    public double calculateFat(int cal){
        double fatPerCalorie = 8 / 12;
        return 100 * fatPerCalorie;
    }

    //Determine how much saturated and transfat is needed
    public double calculateBadFats(int cal) {
        double fatPerCalorie = 3.1 / 15;
        return fatPerCalorie * 100;
    }
}
