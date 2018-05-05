package ca.infostages.infonut;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    // Tags for the fragment manager
    private static final String TAG_CHOOSE_PLAN_FRAGMENT = "TAG_CHOOSE_PLAN_FRAGMENT";
    private static final String TAG_MAKE_PLAN_FRAGMENT = "TAG_MAKE_PLAN_FRAGMENT";

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_camera:
                    mTextMessage.setText("Camera");
                    intent = new Intent(Home.this, BarcodeReader.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_plans:
                    intent = new Intent(Home.this, ChoosePlanActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_settings:
                    mTextMessage.setText("Settings");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Inserts each fragment into the activity

        // ChoosePlanFragment
        ChoosePlanFragment choosePlanFragment = ChoosePlanFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.choose_plan_fragment, choosePlanFragment, TAG_CHOOSE_PLAN_FRAGMENT)
                .commit();

        // MakePlanFragment
        MakePlanFragment makePlanFragment = MakePlanFragment.newInstance();
        fragmentManager.beginTransaction()
                .add(R.id.make_plan_fragment, makePlanFragment, TAG_MAKE_PLAN_FRAGMENT)
                .commit();
    }
}
