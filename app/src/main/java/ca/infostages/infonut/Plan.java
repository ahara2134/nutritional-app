package ca.infostages.infonut;

import java.util.ArrayList;

public class Plan {

    private String planTitle;
    private ArrayList<String> nutrients;
    private ArrayList<String> ingredients;

    public Plan() {}

    public Plan(String planTitle, ArrayList<String> nutrients, ArrayList<String> ingredients) {
        setPlanTitle(planTitle);
        setNutrients(nutrients);
        setIngredients(ingredients);
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        if (planTitle != null && planTitle.length() > 0) {
            this.planTitle = planTitle;
        } else {
            this.planTitle = "Untitled";
        }
    }

    public ArrayList<String> getNutrients() {
        return nutrients;
    }

    public void setNutrients(ArrayList<String> nutrients) {
        if (nutrients != null && nutrients.size() > 0) {
            this.nutrients = nutrients;
        } else {
            this.nutrients = null;
        }
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        if (ingredients != null && ingredients.size() > 0) {
            this.ingredients = ingredients;
        } else {
            this.ingredients = null;
        }
    }
}
