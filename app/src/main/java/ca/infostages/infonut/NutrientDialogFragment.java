package ca.infostages.infonut;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Creates a dialog fragment that lists 13 nutrients that a user can pick from to monitor in their
 * plans.
 */
public class NutrientDialogFragment extends DialogFragment {

    private ArrayList<Integer> nutrients;
    NutrientDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (NutrientDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NutrientDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        nutrients = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()),
                R.style.AppDialogTheme);
        builder.setTitle(R.string.pick_nutrients)
                .setPositiveButton(R.string.submit_nutrients, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendBackResult();
                    }
                }).setNegativeButton(R.string.cancel_nutrients, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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

    /**
     * Closes the dialog window and sends an ArrayList of indexes to any listeners available.
     */
    private void sendBackResult() {
        listener.onFinishEditDialog(nutrients);
        dismiss();
    }

    /**
     * An interface for activities that want to obtain information from this dialog fragment.
     */
    public interface NutrientDialogListener {
        /**
         * A listener that allows parent activities to retrieve a list of
         * nutrients from this dialog.
         * @param nutrients - an Integer ArrayList that holds indexes of the nutrient_list array.
         */
        void onFinishEditDialog(ArrayList<Integer> nutrients);
    }
}
