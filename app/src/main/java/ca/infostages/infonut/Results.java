package ca.infostages.infonut;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Results extends Fragment {

    public static Results newInstance() {
        return new Results();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        // Some shit happens
        return view;
    }

    public void redButton(View view)
    {
        view.setBackgroundResource(R.color.red);
    }
    public void greenButton(View view)
    {
        view.setBackgroundResource(R.color.green);
    }
}
