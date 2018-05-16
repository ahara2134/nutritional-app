package ca.infostages.infonut;

import android.os.AsyncTask;
import android.widget.SeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class NutritionData extends AsyncTask<Void,Void,Void>{
    String data = "";
    private String barcode;
    private static String dataParsed = "";
    public static HashMap<String, Double> nutritionHashMap = new HashMap<>();
    public NutritionData(String barcode) {
        this.barcode = barcode;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            } bufferedReader.close();

            JSONObject obj = new JSONObject(response.toString());

            JSONObject product = obj.getJSONObject("product");

            JSONObject nutrients = product.getJSONObject("nutriments");

            //productName = obj.getString("product_name");
            String servingSize = product.getString("serving_size");
            String quantity = product.getString("quantity");

            //gets the nutrients by key per serving
            String fat = nutrients.getString("fat_serving");
            String saturatedFat = nutrients.getString("saturated-fat_serving");
            String transFat = nutrients.getString("trans-fat_serving");
            String cholesterol = nutrients.getString("cholesterol_serving");
            String sodium = nutrients.getString("sodium_serving");
            String carbohydrate = nutrients.getString("carbohydrates_serving");
            String fibre = nutrients.getString("fiber_serving");
            String protein = nutrients.getString("proteins_serving");
            String vitaminA = nutrients.getString("vitamin-a_serving");
            String vitaminC = nutrients.getString("vitamin-c_serving");
            String calcium = nutrients.getString("calcium_serving");
            String iron = nutrients.getString("iron_serving");
            String potassium = nutrients.getString("potassium_serving");
            String monoUnsaturatedFat = nutrients.getString("monounsaturated-fat_serving");
            String polyUnsaturatedFat = nutrients.getString("polyunsaturated-fat_serving");
            String goodfat_serving = goodFatCalculator(polyUnsaturatedFat, monoUnsaturatedFat);
            String badfat_serving = badFatCalculator(transFat, saturatedFat);

            //gets the nutrients by key per 100g
            String fat_100 = nutrients.getString("fat_100g");
            String saturatedFat_100 = nutrients.getString("saturated-fat_100g");
            String transFat_100 = nutrients.getString("trans-fat_100g");
            String cholesterol_100 = nutrients.getString("cholesterol_100g");
            String sodium_100 = nutrients.getString("sodium_100g");
            String carbohydrate_100 = nutrients.getString("carbohydrates_100g");
            String fibre_100 = nutrients.getString("fiber_100g");
            String protein_100 = nutrients.getString("proteins_100g");
            String vitaminA_100 = nutrients.getString("vitamin-a_100g");
            String vitaminC_100 = nutrients.getString("vitamin-c_100g");
            String calcium_100 = nutrients.getString("calcium_100g");
            String iron_100 = nutrients.getString("iron_100g");
            String potassium_100 = nutrients.getString("potassium_100g");
            String monoUnsaturatedFat_100 = nutrients.getString("monounsaturated-fat_100g");
            String polyUnsaturatedFat_100 = nutrients.getString("polyunsaturated-fat_100g");

            String badfat_100 = badFatCalculator(transFat_100, saturatedFat_100);
            String goodfat_100 = goodFatCalculator(polyUnsaturatedFat_100, monoUnsaturatedFat_100);

            //add nutrition by serving into hashmap
            nutritionHashMap.put("fat",convertNutrition(fat));
            nutritionHashMap.put("goodFat",convertNutrition(goodfat_serving));
            nutritionHashMap.put("badFat",convertNutrition(badfat_serving));
            nutritionHashMap.put("cholesterol",convertNutrition(cholesterol));
            nutritionHashMap.put("sodium",convertNutrition(sodium));
            nutritionHashMap.put("carbohydrate",convertNutrition(carbohydrate));
            nutritionHashMap.put("fibre",convertNutrition(fibre));
            nutritionHashMap.put("protein",convertNutrition(protein));
            nutritionHashMap.put("vitaminA",convertNutrition(vitaminA));
            nutritionHashMap.put("vitaminC",convertNutrition(vitaminC));
            nutritionHashMap.put("calcium",convertNutrition(calcium));
            nutritionHashMap.put("iron",convertNutrition(iron));
            nutritionHashMap.put("potassium",convertNutrition(potassium));

            //add nutrition by 100g into hashmap
            nutritionHashMap.put("fat_100",convertNutrition(fat_100));
            nutritionHashMap.put("goodFat_100", convertNutrition(goodfat_100));
            nutritionHashMap.put("badFat_100",convertNutrition(badfat_100));
            nutritionHashMap.put("cholesterol_100",convertNutrition(cholesterol_100));
            nutritionHashMap.put("sodium_100",convertNutrition(sodium_100));
            nutritionHashMap.put("carbohydrate_100",convertNutrition(carbohydrate_100));
            nutritionHashMap.put("fibre_100",convertNutrition(fibre_100));
            nutritionHashMap.put("protein_100",convertNutrition(protein_100));
            nutritionHashMap.put("vitaminA_100",convertNutrition(vitaminA_100));
            nutritionHashMap.put("vitaminC_100",convertNutrition(vitaminC_100));
            nutritionHashMap.put("calcium_100",convertNutrition(calcium_100));
            nutritionHashMap.put("iron_100",convertNutrition(iron_100));
            nutritionHashMap.put("potassium_100",convertNutrition(potassium_100));


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //BarcodeReader.statusMessage.setText("FINISHED!");
        //send data here OR pass hashmap
    }

    /**
     * Calculates the badFats by adding
     * transfat and saturated fats
     * @param transFat
     * @param saturatedFat
     * @return
     */
    private String badFatCalculator(String transFat, String saturatedFat) {
        double transfat = Double.parseDouble(transFat);
        double saturated = Double.parseDouble(saturatedFat);

        double badFatD = transfat + saturated;
        return "" + badFatD;
    }

    private String goodFatCalculator(String poly, String mono) {
        double polyFat = Double.parseDouble(poly);
        double monoFat = Double.parseDouble(mono);

        double goodFatD = polyFat + monoFat;
        return "" + goodFatD;
    }

    /**
     * Converts the nutrition from a string value to a double value
     * @param nutrition
     * @return
     */
    private double convertNutrition(String nutrition) {
        double dNutrition;
        String str = nutrition;
        str = str.replaceAll("[^\\d.]", "");
        dNutrition = Double.parseDouble(str);
        return dNutrition;
    }
}
