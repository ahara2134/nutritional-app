import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import ca.infostages.infonut.R;

public class NutrientDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_nutrients)
                .setPositiveButton(R.string.submit_nutrients, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User submitted their
                    }
                }).setNegativeButton(R.string.cancel_nutrients, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // user cancelled their request
            }
        }).setMultiChoiceItems(R.array.nutrient_list, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index, boolean isChecked) {
                        if (isChecked) {
                            // add nutrient to array
                        }
                    }
                });
        builder.create();
        return super.onCreateDialog(savedInstanceState);
    }
}
