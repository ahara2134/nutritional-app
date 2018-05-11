package ca.infostages.infonut;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class NewUser extends AppCompatActivity {
    //declare database objects
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth firebaseAuth;

    //declare GUI objects
    private EditText displayName;
    private RadioGroup genderRadioGroup;
    private RadioGroup dobRadioGroup;
    private RadioButton genderRadioButton;
    private RadioButton dobRadioButton;
    private Button submit;
    private String name;
    private String gender;
    private int ageGroup;
    private Boolean checkEditTextEmpty;
    private Boolean checkGenderEmpty;
    private Boolean checkAgeEmpty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        //instantiate database objects
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();

        //Instantiate GUI objects
        genderRadioGroup = (RadioGroup)findViewById(R.id.radio_gender);
        dobRadioGroup = (RadioGroup)findViewById(R.id.radio_dob);
        submit = (Button)findViewById(R.id.button_submit);
        displayName = (EditText)findViewById(R.id.editText_name);

        //Button listener for submit button - sets user demographics in database/
        // In this method, there should be testing for the values and then assigning of default plan.
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get demographics that we need
                String uID = mUser.getUid();
                name = displayName.getText().toString();
                CheckEditTextIsEmptyOrNot();
                if(!checkEditTextEmpty || !checkAgeEmpty || !checkGenderEmpty) {
                    Toast.makeText(NewUser.this, getString(R.string.fill_field), Toast.LENGTH_LONG).show();
                } else {
                    int selectedGender = genderRadioGroup.getCheckedRadioButtonId();
                    genderRadioButton = (RadioButton) findViewById(selectedGender);
                    gender = genderRadioButton.getText().toString();

                    int selectedAge = dobRadioGroup.getCheckedRadioButtonId();
                    dobRadioButton = (RadioButton) findViewById(selectedAge);
                    String age = dobRadioButton.getText().toString();

                    //Assign values for age:
                    //  1: 0-2 years old
                    //  2: 3-18 years old
                    //  3: 19 and older
                    if (age.equals(getString(R.string.dob_infant))) {
                        ageGroup = 1;
                    } else if (age.equals(getString(R.string.dob_adolescent))) {
                        ageGroup = 2;
                    } else {
                        ageGroup = 3;
                    }

                    //Writing to database
                    mDatabase.child("users").child(uID).child("display_name").setValue(name);
                    mDatabase.child("users").child(uID).child("gender").setValue(gender);
                    mDatabase.child("users").child(uID).child("age").setValue(ageGroup);

                    //GAREL - this is where you will be setting the default plan, depending on the gender and date of birth
                    mDatabase.child("users").child(uID).child("plan").child("default_plan").setValue(true);

                    Toast.makeText(NewUser.this, getString(R.string.submit_confirmation), Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });
    }

    //check if EditText is empty or not
    public void CheckEditTextIsEmptyOrNot() {
        String nameHolder = displayName.getText().toString().trim();

        //Check if both editText is empty or not
        if(TextUtils.isEmpty(nameHolder)) {
            //if empty, set value as false.
            checkEditTextEmpty = false;
        } else {
            checkEditTextEmpty = true;
        }

        //Check if DOB radiobutton was selected
        if (dobRadioGroup.getCheckedRadioButtonId() == -1) {
            checkAgeEmpty = false;
        } else {
            checkAgeEmpty = true;
        }

        //Check if gender radioButton was selected
        if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
            checkGenderEmpty = false;
        } else {
            checkGenderEmpty = true;
        }
    }
}
