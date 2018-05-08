package ca.infostages.infonut;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NutritionData extends AsyncTask<Void,Void,Void>{
    String data = "";
    private String barcode;
    private static String dataParsed = "";
    private String productName;
    private String fat = "0";
    private String saturatedFat = "0";
    private String transFat = "0";
    private String cholesterol = "0";
    private String sodium = "0";
    private String carbohydrate = "0";
    private String fibre = "0";
    private String sugars = "0";
    private String protein = "0";
    private String vitaminA = "0";
    private String vitaminC = "0";
    private String calcium = "0";
    private String iron = "0";
    private String servingSize = "";
    private String quantity = "";

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

            productName = obj.getString("product_name");
            servingSize = product.getString("serving_size");
            quantity = product.getString("quantity");

            fat = nutrients.getString("fat_value");
            saturatedFat = nutrients.getString("saturated-fat_serving");
            transFat = nutrients.getString("trans-fat_serving");
            cholesterol = nutrients.getString("cholesterol_serving");
            sodium = nutrients.getString("sodium_serving");
            carbohydrate = nutrients.getString("carbohydrates_serving");
            fibre = nutrients.getString("fiber_serving");
            sugars = nutrients.getString("sugars_serving");
            protein = nutrients.getString("proteins_serving");
            vitaminA = nutrients.getString("vitamin-a_serving");
            vitaminC = nutrients.getString("vitamin-c_serving");
            calcium = nutrients.getString("calcium_serving");
            iron = nutrients.getString("iron_serving");


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
                    + "Iron: " + iron + "\n";

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        BarcodeReader.statusMessage.setText(dataParsed);
        //send data here

    }
}
