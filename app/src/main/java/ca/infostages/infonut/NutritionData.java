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
    String barcode;
    static String dataParsed = "";
    static String singleParsed = "";
    String fiber = "";
    public NutritionData(String barcode) {
        this.barcode = barcode;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject mainJO = new JSONObject(data);
            //JSONArray JA = new JSONArray(data);
            if(mainJO != null){
                JSONArray list = mainJO.getJSONArray("product");
                if(list != null){
                    for(int i = 0; i < list.length();i++){
                        JSONObject elem = list.getJSONObject(i);
                        if(elem != null){
                            JSONArray prods = elem.getJSONArray("nutriments");
                            if(prods != null){
                                for(int j = 0; j < prods.length();j++){
                                    JSONObject innerElem = prods.getJSONObject(j);
                                    if(innerElem != null){
                                        /*int cat_id = innerELem.getInt("cat_id");
                                        int pos = innerElem.getInt("position");
                                        String sku = innerElem.getString("sku");*/
                                        fiber = innerElem.getString("monounsaturated-fat_value");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            /*for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                singleParsed = "Nutrients: " + JO.get("monounsaturated-fat_value") + "\n";

                dataParsed = dataParsed + singleParsed + "\n";
            }*/

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        BarcodeReader.statusMessage.setText("Hello: " +     fiber);
    }
}
