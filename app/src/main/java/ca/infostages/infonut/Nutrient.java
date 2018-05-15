package ca.infostages.infonut;

/**
 * A class representing nutrients to be stored in a RecyclerView.
 */
public class Nutrient {

    private String nutrientName;
    private double intakeLimitInGrams;

    public Nutrient(String nutrientName, double intakeLimitInGrams) {
        setIntakeLimitInGrams(intakeLimitInGrams);
        setNutrientName(nutrientName);
    }

    /**
     * Accesses the intake limit for this nutrient.
     * @return
     */
    public double getIntakeLimitInGrams() {
        return intakeLimitInGrams;
    }

    /**
     * Sets an intake limit for this specific nutrient.
     * @param intakeLimitInGrams - a double value representing how much (in grams) a user can take
     *                           of this nutrient.
     */
    public void setIntakeLimitInGrams(double intakeLimitInGrams) {
        if (intakeLimitInGrams >= 0) {
            this.intakeLimitInGrams = intakeLimitInGrams;
        } else {
            this.intakeLimitInGrams = 0;
        }
    }

    /**
     * Accesses the name of this nutrient.
     * @return a String value representing the nutrient name.
     */
    public String getNutrientName() {
        return nutrientName;
    }

    /**
     * Sets the name of this nutrient.
     * @param nutrientName - a string value that will be obtained from a checklist the user
     *                     fills out.
     */
    public void setNutrientName(String nutrientName) {
        this.nutrientName = nutrientName;
    }
}
