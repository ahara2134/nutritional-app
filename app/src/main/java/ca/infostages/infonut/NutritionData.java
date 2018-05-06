package ca.infostages.infonut;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class NutritionData extends AsyncTask<Void,Void,Void>{
    String data = "";
    private String barcode;
    private static String dataParsed = "";
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
            StringBuffer response = new StringBuffer();
            while((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            } bufferedReader.close();

            JSONObject obj = new JSONObject(response.toString());

            JSONObject product = obj.getJSONObject("product");

            JSONObject obj2 = product.getJSONObject("nutriments");

            dataParsed = "" + obj2;

            fat = obj2.getString("fat_value");
            saturatedFat = obj2.getString("saturated-fat_serving");
            transFat = obj2.getString("trans-fat_serving");
            cholesterol = obj2.getString("cholesterol_serving");
            sodium = obj2.getString("sodium_serving");
            carbohydrate = obj2.getString("carbohydrates_serving");
            fibre = obj2.getString("fiber_serving");
            sugars = obj2.getString("sugars_serving");
            protein = obj2.getString("proteins_serving");
            vitaminA = obj2.getString("vitamin-a_serving");
            vitaminC = obj2.getString("vitamin-c_serving");
            calcium = obj2.getString("calcium_serving");
            iron = obj2.getString("iron_serving");

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
                    + "Vitamin C" + vitaminC + "\n"
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
    }
}
