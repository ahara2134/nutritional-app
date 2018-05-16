package ca.infostages.infonut;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NewUserActivity extends AppCompatActivity
        implements NewUserFragment.NewUserFragmentListener,
        NewUser2Fragment.NewUser2FragmentListener {

    private NewUserFragment newUserFragment;
    private NewUser2Fragment newUser2Fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // Instantiate fragment instances
        newUserFragment = NewUserFragment.newInstance();
        newUser2Fragment = NewUser2Fragment.newInstance();
        loadFragment(newUserFragment);
    }

    /**
     * Replaces the content of the current fragment with a new one.
     * @param fragment - a fragment that has been selected through the bottom navigation.
     */
    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nw_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void getGenderAndActive(String gender, int activeLevel) {
        Bundle bundle = new Bundle();
        bundle.putString("textGender", gender);
        bundle.putInt("activeLevel", activeLevel);
        newUser2Fragment.setArguments(bundle);
        loadFragment(newUser2Fragment);
    }

    @Override
    public void submitForm() {
        finish();
    }
}
