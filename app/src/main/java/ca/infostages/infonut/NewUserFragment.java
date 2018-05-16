package ca.infostages.infonut;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewUserFragment extends Fragment {

    /**
     * Creates a new instance of this class and returns it to the caller.
     * @return a NewUserFragment
     */
    public static NewUserFragment newInstance() {
        return new NewUserFragment();
    }

    public interface NewUserFragmentListener {
        void getGenderAndActive(String gender, int activeLevel);
    }

    //declare database objects
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth firebaseAuth;

    //declare GUI objects
    private EditText displayName;
    private RadioGroup genderRadioGroup;
    private RadioGroup activeRadioGroup;
    private RadioButton genderRadioButton;
    private RadioButton activeRadioButton;
    private Button submit;
    private String name;
    private String gender;
    private int activeLevel;
    private Boolean checkEditTextEmpty;
    private Boolean checkGenderEmpty;
    private Boolean checkActiveEmpty;

    // Fragment related objects
    private View currentView;
    private NewUserFragmentListener listener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstance) {
        currentView = inflater.inflate(R.layout.fragment_new_user, container, false);
        //instantiate database objects
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();

        //Instantiate GUI objects
        genderRadioGroup = currentView.findViewById(R.id.radio_gender);
        activeRadioGroup = currentView.findViewById(R.id.radio_active);
        submit = currentView.findViewById(R.id.button_next);
        displayName = currentView.findViewById(R.id.editText_name);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get demographics that we need
                String uID = mUser.getUid();
                name = displayName.getText().toString();
                CheckEditTextIsEmptyOrNot();
                if(!checkEditTextEmpty || !checkActiveEmpty || !checkGenderEmpty) {
                    Toast.makeText(getContext(), getString(R.string.fill_field), Toast.LENGTH_LONG).show();
                } else {
                    int selectedGender = genderRadioGroup.getCheckedRadioButtonId();
                    genderRadioButton = currentView.findViewById(selectedGender);
                    gender = genderRadioButton.getText().toString();

                    int selectedAge = activeRadioGroup.getCheckedRadioButtonId();
                    activeRadioButton = currentView.findViewById(selectedAge);
                    String activity = activeRadioButton.getText().toString();

                    //Assign values for activity level:
                    //  1: Sedentary
                    //  2: Low
                    //  3: High
                    if (activity.equals(getString(R.string.active_sedentary))) {
                        activeLevel = 1;
                    } else if (activity.equals(getString(R.string.active_low))) {
                        activeLevel = 2;
                    } else {
                        activeLevel = 3;
                    }

                    //Writing to database
                    mDatabase.child("users").child(uID).child("display_name").setValue(name);
                    mDatabase.child("users").child(uID).child("gender").setValue(gender);
                    mDatabase.child("users").child(uID).child("active").setValue(activeLevel);
                    listener.getGenderAndActive(gender, activeLevel);
                }
            }
        });
        return currentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (NewUserFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement " +
                    "NewUserFragmentListener");
        }
    }

    //check if EditText is empty or not
    private void CheckEditTextIsEmptyOrNot() {
        String nameHolder = displayName.getText().toString().trim();

        //Check if both editText is empty or not
        if(TextUtils.isEmpty(nameHolder)) {
            //if empty, set value as false.
            checkEditTextEmpty = false;
        } else {
            checkEditTextEmpty = true;
        }

        //Check if DOB radiobutton was selected
        if (activeRadioGroup.getCheckedRadioButtonId() == -1) {
            checkActiveEmpty = false;
        } else {
            checkActiveEmpty = true;
        }

        //Check if gender radioButton was selected
        if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
            checkGenderEmpty = false;
        } else {
            checkGenderEmpty = true;
        }
    }
}
