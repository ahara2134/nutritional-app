package ca.infostages.infonut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
    Button buttonSignOut, buttonNewUser;
    Spinner planSpinner;
    TextView name, age, email, activity;
    String nameHolder, ageHolder, emailHolder, activityHolder, defaultSpinnerValue;
    List<String> spinnerArray = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference planReference, mDatabase;

    private static final String TAG = "Settings.fragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        buttonSignOut = (Button)view.findViewById(R.id.button_signout);
        buttonNewUser = (Button)view.findViewById(R.id.button_newUser);
        planSpinner = (Spinner)view.findViewById(R.id.spinner_plans);
        name = (TextView)view.findViewById(R.id.textView_username);
        age = (TextView)view.findViewById(R.id.textView_age);
        email = (TextView)view.findViewById(R.id.textView_email);
        activity = (TextView)view.findViewById(R.id.textView_activity);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            updateDisplayName();
            updateEmail();
            updateAge();
            updateActive();
            updateDefaultSpinner();
            getListedPlans();
            planSpinner.setPrompt(defaultSpinnerValue);

            buttonSignOut.setOnClickListener(this);
            buttonNewUser.setOnClickListener(this);

            //Add listener for spinner changed value
            planSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedValue = planSpinner.getSelectedItem().toString();
                    mDatabase.child("users").child(currentUser.getUid()).child("selected_plan").setValue(selectedValue);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d(TAG, ": spinner selected nothing");
                }
            });
        }

        //Enable Back button
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_newUser:
                Intent intent = new Intent(getActivity(), NewUserActivity.class);
                startActivity(intent);
                ((Activity)getActivity()).overridePendingTransition(0,0);
                break;
            case R.id.button_signout:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent (getActivity(), MainActivity.class);
                startActivity(intent2);
                ((Activity)getActivity()).overridePendingTransition(0,0);
                break;
        }
    }

    //Updates Display name textview to reflect the current user's.
    public void updateDisplayName() {
        planReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("display_name");
        planReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameHolder = dataSnapshot.getValue().toString();
                if (!nameHolder.equals("false")) {
                    name.setText(nameHolder);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, ": " + databaseError.getMessage());
            }
        });
    }

    //Updates email textview to reflect the current user's.
    public void updateEmail() {
        emailHolder = currentUser.getEmail();
        email.setText(emailHolder);
    }

    //Updates age textview to reflect the current user's.
    public void updateAge() {
        planReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("age");
        planReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ageHolder = dataSnapshot.getValue().toString();
                if (!ageHolder.equals("false")) {
                    age.setText(ageHolder);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, ": " + databaseError.getMessage());
            }
        });
    }

    //Updates activity level textview to reflect the current user's.
    public void updateActive() {
        planReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("active");
        planReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activityHolder = dataSnapshot.getValue().toString();
                String sedantary = "Sedantary Activity Level";
                String lowActivity = "Low Activity Level";
                String highActivity = "High Activity Level";
                if (!activityHolder.equals("false")) {
                    if(activityHolder.equals("1")) {
                        activity.setText(sedantary);
                    } else if(activityHolder.equals("2")) {
                        activity.setText(lowActivity);
                    } else {
                        activity.setText(highActivity);
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, ": " + databaseError.getMessage());
            }
        });
    }

    //Updates selected default plan to reflect the current user's.
    public void updateDefaultSpinner() {
        planReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("selected_plan");
        planReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                defaultSpinnerValue = dataSnapshot.getValue().toString();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, ": " + databaseError.getMessage());
            }
        });
    }

    //Fetches all of the plans listed under the user
    public void getListedPlans() {
        planReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("plan");
        planReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectPlans((Map<String, Object>) dataSnapshot.getValue());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),
                                android.R.layout.simple_spinner_item,
                                spinnerArray
                        );

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        planSpinner.setAdapter(adapter);
                        for(int i = 0; i < adapter.getCount(); ++i) {
                            if(adapter.getItem(i).toString().equals(defaultSpinnerValue)) {
                                planSpinner.setSelection(i);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, ": " + databaseError.getMessage());
                    }
                }
        );
    }

    public void collectPlans(Map<String, Object> plans) {
        for(Map.Entry<String, Object> entry : plans.entrySet()) {
            Map singlePlan = (Map)entry.getValue();
            spinnerArray.add((String)singlePlan.get("planTitle"));
        }
    }
}
