package ca.infostages.infonut;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * An adapter for nutrient objects that can be applied to RecyclerViews.
 */
public class NutrientsAdapter extends RecyclerView.Adapter<NutrientsAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        EditText limitEditText;
        ImageButton deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nutrient_name);
            limitEditText = itemView.findViewById(R.id.nutrient_limit);
            deleteButton = itemView.findViewById(R.id.item_delete_btn);
        }
    }

    private List<Nutrient> nutrients;

    NutrientsAdapter(List<Nutrient> nutrients) {
        this.nutrients = nutrients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View nutrientView = inflater.inflate(R.layout.item_nutrient, parent, false);
        return new ViewHolder(nutrientView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Nutrient nutrient = nutrients.get(position);

        TextView textView = holder.nameTextView;
        textView.setText(nutrient.getNutrientName());

        ImageButton imageButton = holder.deleteButton;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nutrients.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return nutrients.size();
    }
}
