package ca.infostages.infonut;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class HomeFragment extends Fragment {

    private ImageButton settings;
    private ImageButton plans;
    private ImageButton scan;
    private ImageButton info;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initializing the variables
        settings    = (ImageButton) view.findViewById(R.id.imageButton9);
        plans       = (ImageButton) view.findViewById(R.id.imageButton7);
        scan        = (ImageButton) view.findViewById(R.id.imageButton8);
        info        = (ImageButton) view.findViewById(R.id.imageButton5);

        //Enable Back button
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        /**
         * This will connect the user to the SettingsFragment.java fragment from the HomeFragment.java fragment
         */
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SettingsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        /**
         * This will connect the user to the ChoosePlanFragment.java fragment from the
         * HomeFragment.java fragment
         */
        plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ChoosePlanFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        /**
         * This will connect the user to the BarcodeReader activity from the HomeFragment.java
         * fragment
         */
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getActivity(), BarcodeReader.class);
                startActivity(intent2);
            }
        });

        /**
         * This will connect the user to the *insert w/e fragment/activity from the
         * HomeFragment.java fragment
         */
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fill in with your fragment/activity
                // Ask Ryan if you need help with this.
            }
        });

        return view;
    }

}
