package ca.infostages.infonut;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class NewUser2Activity extends AppCompatActivity implements View.OnClickListener{
    //declare database objects
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth firebaseAuth;

    //declare GUI objects
    private Spinner ageSpinner, weightSpinner;
    private EditText weightText;
    private Button submitButton;
    private String age, weightValue;
    private int weight, ageDB;
    private boolean ageIsEmpty, weightIsEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user2);

        ageSpinner = (Spinner)findViewById(R.id.spinner_age);
        age = ageSpinner.getSelectedItem().toString();

        weightSpinner = (Spinner)findViewById(R.id.spinner_weight);
        weightValue = weightSpinner.getSelectedItem().toString();

        submitButton = (Button)findViewById(R.id.button_submit);
        submitButton.setOnClickListener(this);

        weightText = (EditText)findViewById(R.id.editText_weight);
        weight = Integer.parseInt(weightText.getText().toString());
    }

    @Override
    public void onClick(View v) {
        CheckEditTextIsEmptyOrNot();
    }

    //check if EditText is empty or not
    public void CheckEditTextIsEmptyOrNot() {
        if(age.equals(getString(R.string.age_spinner_1))) {
            ageIsEmpty = false;
        }

        if(weight != null) {

    }
}
