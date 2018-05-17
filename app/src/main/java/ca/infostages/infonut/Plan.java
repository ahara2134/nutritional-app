package ca.infostages.infonut;

import java.util.HashMap;

/**
 * This class represents user plans that keep track of ingredients and nutrients as well as any
 * limits that users have concerning the intake of those
 */
public class Plan {

    private String planTitle;
    private HashMap<String, Double> nutrients;
    private HashMap<String, Double> ingredients;

    public Plan() {}

    public Plan(String planTitle, HashMap<String, Double> nutrients) {
        setPlanTitle(planTitle);
        setNutrients(nutrients);
    }

    public Plan(String planTitle, HashMap<String, Double> nutrients,
                HashMap<String, Double> ingredients) {
        setPlanTitle(planTitle);
        setNutrients(nutrients);
        setIngredients(ingredients);
    }

    /**
     * Returns the title of this plan.
     * @return planTitle
     */
    public String getPlanTitle() {
        return planTitle;
    }

    /**
     * Sets the title of this plan object. If the title given is null, then it is set to untitled.
     * @param planTitle - the string title of the plan object.
     */
    public void setPlanTitle(String planTitle) {
        if (planTitle != null && planTitle.length() > 0) {
            this.planTitle = planTitle;
        } else {
            this.planTitle = "Untitled";
        }
    }

    /**
     * Returns a map of nutrients and their values.
     * @return nutrients
     */
    public HashMap<String, Double> getNutrients() {
        return nutrients;
    }

    /**
     * Sets a map of nutrients and intake limits in long form.
     * @param nutrients - a HashMap of nutrients that are gained from user input.
     */
    public void setNutrients(HashMap<String, Double> nutrients) {
        if (nutrients != null && nutrients.size() > 0) {
            this.nutrients = nutrients;
        } else {
            this.nutrients = null;
        }
    }

    /**
     * Returns a map of ingredients and their values
     * @return ingredients
     */
    public HashMap<String, Double> getIngredients() {
        return ingredients;
    }

    /**
     * Sets a map of ingredients that are paired with max intake values.
     * @param ingredients - a hashmap of blacklisted ingredients
     */
    public void setIngredients(HashMap<String, Double> ingredients) {
        if (ingredients != null && ingredients.size() > 0) {
            this.ingredients = ingredients;
        } else {
            this.ingredients = null;
        }
    }
}
