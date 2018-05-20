package ca.infostages.infonut;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Represents the container of all user navigation related tasks.
 */
public class Home extends AppCompatActivity {

    private View view2;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Toolbar toolbar;

    private static final String TAG = "Home.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Sets the home fragment as the current view.
        loadFragment(HomeFragment.newInstance());

        // This is just for changing the background color
        view2 = this.getWindow().getDecorView();

        mAuth = FirebaseAuth.getInstance();

//        toolbar = findViewById(R.id.toolbarId);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

        //Add back navigation in the title bar
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //Checks if the user is logged in. If not, send to Mainactivity.
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            Intent intent = new Intent(Home.this, MainActivity.class);
            startActivity(intent);
        } else {
            //Checks if user's demographics are entered in. If not, send to NewUserActivity.
            DatabaseReference planReference;
            planReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("plan");
            planReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String plan = dataSnapshot.getValue().toString();
                    if (plan.equals("false")) {
                        Intent intent = new Intent (Home.this, NewUserActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, ": " + databaseError.getMessage());
                }
            });
        }
    }

    /**
     * This will take the user back to the previous activity
     * @param item what button is being selected
     * @return super.onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Both navigation bar back press and title bar back press will trigger this method
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    /**
     * Opens the MakePlanActivity upon button press from the ChoosePlanFragment
     * @param view - view
     */
    public void makePlan(View view) {
        Intent intent = new Intent(Home.this, MakePlanActivity.class);
        startActivity(intent);
    }

    /**
     * Replaces the content of the current fragment with a new one.
     * @param fragment - a fragment that has been selected through the bottom navigation.
     */
    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commitAllowingStateLoss();
        }
    }

    public void redButton(View view)
    {
        view2.setBackgroundResource(R.color.red);
    }
    public void greenButton(View view)
    {
        view2.setBackgroundResource(R.color.green);
    }
}
