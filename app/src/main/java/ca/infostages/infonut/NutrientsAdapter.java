package ca.infostages.infonut;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class NutrientsAdapter extends RecyclerView.Adapter<NutrientsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public EditText limitEditText;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nutrient_name);
            limitEditText = itemView.findViewById(R.id.nutrient_limit);
        }
    }

    private List<Nutrient> nutrients;

    public NutrientsAdapter(List<Nutrient> nutrients) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Nutrient nutrient = nutrients.get(position);

        TextView textView = holder.nameTextView;
        textView.setText(nutrient.getNutrientName());
    }

    @Override
    public int getItemCount() {
        return nutrients.size();
    }
}
