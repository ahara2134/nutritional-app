package ca.infostages.infonut;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Generates a view that allow the user to choose diet plans.
 */
public class ChoosePlanFragment extends Fragment implements View.OnClickListener{

    private final String TAG = "ChoosePlanFragment.java";
    private Toolbar toolbar;

    private Button delete;
    private ListView planView;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef, delDBRef, mSelectedPlan;
    private Boolean itemSelected = false;
    private int selectedPosition = 0;
    private String selectedPlan, uID;

    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<String> listKeys = new ArrayList<>();
    ArrayAdapter<String> adapter;
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

        planView = (ListView) view.findViewById(R.id.List_plan);
        delete = (Button) view.findViewById(R.id.button_delete);
        delete.setOnClickListener(this);
        delete.setEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            uID = currentUser.getUid();
            mSelectedPlan = FirebaseDatabase.getInstance().getReference().child("users").child(uID).child("selected_plan");
            dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(uID).child("plan");

            adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_single_choice,
                    listItems);
            planView.setAdapter(adapter);
            planView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            planView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener(){
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            selectedPosition = position;
                            itemSelected = true;
                            delete.setEnabled(true);
                        }
                    });

            addChildEventListener();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        String selectedFromList = ((String)planView.getItemAtPosition(selectedPosition));
        if(selectedFromList.equals("default plan")) {
            Toast.makeText(getActivity(), getString(R.string.delete_error), Toast.LENGTH_LONG).show();
        } else {
            planView.setItemChecked(selectedPosition, false);
            deleteFromDatabase(selectedFromList);
            dbRef.child(listKeys.get(selectedPosition)).removeValue();
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), getString(R.string.delete_success), Toast.LENGTH_LONG).show();
        }
    }

    private void addChildEventListener() {
        ChildEventListener childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapter.add(
                        (String) dataSnapshot.child("planTitle").getValue());

                listKeys.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = listKeys.indexOf(key);

                if(index != -1) {
                    listItems.remove(index);
                    listKeys.remove(index);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dbRef.addChildEventListener(childListener);
    }

    private void deleteFromDatabase(String selected) {
        String selectedMod = selected.replaceAll(" ", "_").toLowerCase();
        delDBRef =  FirebaseDatabase.getInstance().getReference().child("users").child(uID).child("plan").child(selectedMod);
        getSelectedPlan(selected);
        delDBRef.setValue(null);
    }

    private void getSelectedPlan(final String selected) {
        mSelectedPlan = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("selected_plan");
        mSelectedPlan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                selectedPlan = dataSnapshot.getValue().toString();
                if(selected.equals(selectedPlan)) {
                    mSelectedPlan.setValue("default plan");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, ": " + databaseError.getMessage());
            }
        });
    }
}
