package ca.infostages.infonut;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Generates a view that allow the user to choose diet plans.
 */
public class ChoosePlanFragment extends Fragment {

    private Toolbar toolbar;

    /**
     * Creates a new instance of this class and returns it to the caller.
     * @return a ChoosePlanFragment
     */
    public static ChoosePlanFragment newInstance() {
        return new ChoosePlanFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_choose_plan, container, false);

        //Enable Back button
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

/*        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarId);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });*/

        return view;
    }
}
