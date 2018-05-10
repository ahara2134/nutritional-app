package ca.infostages.infonut;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

/**
 * Creates a dialog fragment that lists 13 nutrients that a user can pick from to monitor in their
 * plans.
 */
public class NutrientDialogFragment extends DialogFragment {

    private ArrayList<Integer> nutrients;
    NutrientDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (NutrientDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Nutrient DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        nutrients = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_nutrients)
                .setPositiveButton(R.string.submit_nutrients, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogPositiveClick(NutrientDialogFragment.this);
                        dismiss();
                    }
                }).setNegativeButton(R.string.cancel_nutrients, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onDialogNegativeClick(NutrientDialogFragment.this);
                dismiss();
            }
        }).setMultiChoiceItems(R.array.nutrient_list, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index, boolean isChecked) {
                        if (isChecked) {
                            nutrients.add(index);
                        } else if (nutrients.contains(index)) {
                            nutrients.remove(Integer.valueOf(index));
                        }
                    }
                });
        return builder.create();
    }

    public interface NutrientDialogListener {
        void onDialogPositiveClick(DialogFragment dialogFragment);
        void onDialogNegativeClick(DialogFragment dialogFragment);
    }


}
