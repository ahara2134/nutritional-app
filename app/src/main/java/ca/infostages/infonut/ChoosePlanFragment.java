package ca.infostages.infonut;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Generates a view that allow the user to choose diet plans.
 */
public class ChoosePlanFragment extends Fragment {
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
        // ... stuff happens
        return view;
    }
}
