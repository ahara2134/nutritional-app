package ca.infostages.infonut;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Calendar;

public class NewUser extends AppCompatActivity {
    private DatePickerDialog picker;
    private EditText displayName, dobText;
    private RadioGroup radioGroup;
    private Button submit;
    private int mob;
    private int dob;
    private int yob;
    private String name;
    private String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        radioGroup = (RadioGroup)findViewById(R.id.radio_gender);

        submit = (Button)findViewById(R.id.button_submit);
        displayName = (EditText)findViewById(R.id.editText_name);
        dobText = (EditText)findViewById(R.id.editText_dob);
        dobText.setInputType(InputType.TYPE_NULL);

        //When edit Text for DOB is tapped, it will open a new dialogue
        dobText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //date picker dialog
                picker = new DatePickerDialog(NewUser.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dobText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                yob = year;
                                mob = month;
                                dob = dayOfMonth;
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        //Button listener for submit button - sets user demographics in database
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = displayName.getText().toString();
                Integer selectedGender = radioGroup.getCheckedRadioButtonId();
                gender = selectedGender.toString();

            }
        });
    }
}
