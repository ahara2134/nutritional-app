package ca.infostages.infonut;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    GoogleApiClient mGoogleApiClient;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseUser currentUser;

    private static final String TAG_NUTRIENT_DIALOG = "NUTRIENT_DIALOG";
    private static final String TAG = "Home.java";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return loadFragment(HomeFragment.newInstance());
                case R.id.navigation_camera:
                    intent = new Intent(Home.this, BarcodeReader.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_plans:
                    Intent intent4 = new Intent(Home.this, Statistics.class);
                    startActivity(intent4);
                    return true;
                case R.id.navigation_settings:
                    loadFragment(Results.newInstance());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Sets the home fragment as the current view.
        loadFragment(HomeFragment.newInstance());

        // This is just for changing the background color
        view2 = this.getWindow().getDecorView();

        mAuth = FirebaseAuth.getInstance();

        //Checks if user's demographics are entered in. If not, send to NewUserActivity.
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference planReference;
        planReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("plan");
        planReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String plan = dataSnapshot.getValue().toString();
                if (plan.equals("false")) {
                    Intent intent = new Intent (Home.this, NewUser.class);
                    startActivity(intent);
                } else {
                    loadFragment(HomeFragment.newInstance());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, ": " + databaseError.getMessage());
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void newUser(View view) {
        Intent intent = new Intent (Home.this, NewUser.class);
        startActivity(intent);
    }

    /**
     * Replaces the content of the current fragment with a new one.
     * @param fragment - a fragment that has been selected through the bottom navigation.
     * @return a boolean indicating success or failure.
     */
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    /**
     * Opens the MakePlanFragment upon button press from the ChoosePlanFragment
     * @param view - view
     */
    public void makePlan(View view) {
        loadFragment(MakePlanFragment.newInstance());
    }

    /**
     * Shows a dialog which has a list of available nutrients that users can pick from.
     * @param view - view
     */
    public void addNutrientOrIngredient(View view) {
        DialogFragment dialogFragment = new NutrientDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), TAG_NUTRIENT_DIALOG);
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
