package ca.infostages.infonut;

import java.util.ArrayList;
import java.util.HashMap;

public class Plan {

    private String planTitle;
    private HashMap<String, Long> nutrients;
    private HashMap<String, Long> ingredients;

    public Plan() {}

    public Plan(String planTitle, HashMap<String, Long> nutrients, HashMap<String, Long> ingredients) {
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

    public HashMap<String, Long> getNutrients() {
        return nutrients;
    }

    public void setNutrients(HashMap<String, Long> nutrients) {
        if (nutrients != null && nutrients.size() > 0) {
            this.nutrients = nutrients;
        } else {
            this.nutrients = null;
        }
    }

    public HashMap<String, Long> getIngredients() {
        return ingredients;
    }

    public void setIngredients(HashMap<String, Long> ingredients) {
        if (ingredients != null && ingredients.size() > 0) {
            this.ingredients = ingredients;
        } else {
            this.ingredients = null;
        }
    }
}
