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

            String fat = nutrients.getString("fat_value");
            String saturatedFat = nutrients.getString("saturated-fat_serving");
            String transFat = nutrients.getString("trans-fat_serving");
            String cholesterol = nutrients.getString("cholesterol_serving");
            String sodium = nutrients.getString("sodium_serving");
            String carbohydrate = nutrients.getString("carbohydrates_serving");
            String fibre = nutrients.getString("fiber_serving");
            String sugars = nutrients.getString("sugars_serving");
            String protein = nutrients.getString("proteins_serving");
            String vitaminA = nutrients.getString("vitamin-a_serving");
            String vitaminC = nutrients.getString("vitamin-c_serving");
            String calcium = nutrients.getString("calcium_serving");
            String iron = nutrients.getString("iron_serving");

            nutritionHashMap.put("fat",convertNutrition(fat));
            nutritionHashMap.put("saturatedFat",convertNutrition(saturatedFat));
            nutritionHashMap.put("transFat",convertNutrition(transFat));
            nutritionHashMap.put("cholesterol",convertNutrition(cholesterol));
            nutritionHashMap.put("sodium",convertNutrition(sodium));
            nutritionHashMap.put("carbohydrate",convertNutrition(carbohydrate));
            nutritionHashMap.put("fibre",convertNutrition(fibre));
            nutritionHashMap.put("sugars",convertNutrition(sugars));
            nutritionHashMap.put("protein",convertNutrition(protein));
            nutritionHashMap.put("vitaminA",convertNutrition(vitaminA));
            nutritionHashMap.put("vitaminC",convertNutrition(vitaminC));
            nutritionHashMap.put("calcium",convertNutrition(calcium));
            nutritionHashMap.put("iron",convertNutrition(iron));

            dataParsed = "Fat: " + fat + "\n"
                    + "Saturated Fat: " + saturatedFat + "\n"
                    + "Trans Fat: " + transFat + "\n"
                    + "Cholesterol: " + cholesterol + "\n"
                    + "Sodium: " + sodium + "\n"
                    + "Carbohydrate: " + carbohydrate + "\n"
                    + "Fibre: " + fibre + "\n"
                    + "Sugar: " + sugars + "\n"
                    + "Protein: " + protein + "\n"
                    + "Vitamin A: " + vitaminA + "\n"
                    + "Vitamin C: " + vitaminC + "\n"
                    + "Calcium: " + calcium + "\n"
                    + "Iron: " + iron + "\n"
                    + "Quantity: " + quantity + "\n";

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        BarcodeReader.statusMessage.setText(dataParsed);
        //send data here OR pass hashmap

    }

    private double convertNutrition(String nutrition) {
        double dNutrition;
        String str = nutrition;
        str = str.replaceAll("[^\\d.]", "");
        dNutrition = Double.parseDouble(str);
        return dNutrition;
    }
}
