package ca.infostages.infonut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
    Button buttonSignOut;
    TextView name, age, email, activity;
    String nameHolder, ageHolder, emailHolder, activityHolder;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference planReference;

    private static final String TAG = "Settings.fragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        buttonSignOut = (Button)view.findViewById(R.id.button_signout);
        name = (TextView)view.findViewById(R.id.textView_username);
        age = (TextView)view.findViewById(R.id.textView_age);
        email = (TextView)view.findViewById(R.id.textView_email);
        activity = (TextView)view.findViewById(R.id.textView_activity);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            updateDisplayName();
            updateEmail();
            updateAge();
            updateActive();

            buttonSignOut.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent (getActivity(), MainActivity.class);
        startActivity(intent);
        ((Activity)getActivity()).overridePendingTransition(0,0);
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
}
