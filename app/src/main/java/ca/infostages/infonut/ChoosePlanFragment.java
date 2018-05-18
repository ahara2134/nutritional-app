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
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Generates a view that allow the user to choose diet plans.
 */
public class ChoosePlanFragment extends Fragment implements View.OnClickListener{

    private Toolbar toolbar;
    private Button defaultPlanDel, plan1Del, plan2Del, plan3Del, plan4Del, plan5Del;
    private TextView defaultText, plan1Text, plan2Text, plan3Text, plan4Text, plan5Text;
    private String plan1Holder, plan2Holder, plan3Holder, plan4Holder, plan5Holder;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference planReference, mDatabase;
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

        defaultPlanDel = (Button)view.findViewById(R.id.del_default);
        plan1Del = (Button)view.findViewById(R.id.del_plan1);
        plan2Del = (Button)view.findViewById(R.id.del_plan2);
        plan3Del = (Button)view.findViewById(R.id.del_plan3);
        plan4Del = (Button)view.findViewById(R.id.del_plan4);
        plan5Del = (Button)view.findViewById(R.id.del_plan5);
        defaultPlanDel.setOnClickListener(this);
        plan1Del.setOnClickListener(this);
        plan2Del.setOnClickListener(this);
        plan3Del.setOnClickListener(this);
        plan4Del.setOnClickListener(this);
        plan5Del.setOnClickListener(this);

        defaultText = (TextView)view.findViewById(R.id.default_plan);
        plan1Text = (TextView)view.findViewById(R.id.plan1);
        plan2Text = (TextView)view.findViewById(R.id.plan2);
        plan3Text = (TextView)view.findViewById(R.id.plan3);
        plan4Text = (TextView)view.findViewById(R.id.plan4);
        plan5Text = (TextView)view.findViewById(R.id.plan5);

        plan1Holder = "";
        plan2Holder = "";
        plan3Holder = "";
        plan4Holder = "";
        plan5Holder = "";

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {

        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.del_default:
                break;
            case R.id.del_plan1:
                break;
            case R.id.del_plan2:
                break;
            case R.id.del_plan3:
                break;
            case R.id.del_plan4:
                break;
            case R.id.del_plan5:
                break;
        }
    }
}
