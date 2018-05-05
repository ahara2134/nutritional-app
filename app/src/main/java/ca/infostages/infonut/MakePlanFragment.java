package ca.infostages.infonut;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MakePlanFragment extends Fragment {

    public static MakePlanFragment newInstance() {
        return new MakePlanFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_make_plan, container, false);
        // Some shit happens
        return view;
    }
}
