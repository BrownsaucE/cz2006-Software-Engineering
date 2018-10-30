package com.example.zhaoyong.softwareengineering;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Display Out a List of Clinic on List View
 * Clinic information is retrieved from the API
 */
public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        new ParseTask().execute();
    }

    /**
     * Get the Clinic information from the API
     */
    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String $url_json = "https://data.gov.sg/api/action/datastore_search?resource_id=b2871270-4eef-44a3-be98-908e2a73b19f";
                URL url = new URL($url_json);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();
                Log.d("FOR_LOG1", resultJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }


        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            final ListView lView = (ListView) findViewById(R.id.lvMain);

            String[] from = {"name"};
            int[] to = {R.id.name_item};
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> hashmap;

            try {
                JSONObject json = new JSONObject(strJson);
                Log.d(TAG, "onPostExecute: " + json.getString("result"));
                JSONObject newO = json.getJSONObject("result");
                JSONArray JA = newO.getJSONArray("records");
                for (int i = 0; i < JA.length(); i++) {
                    JSONObject clinic = JA.getJSONObject(i);
                    String nameOS = clinic.getString("name");
                    String addressOS = clinic.getString("address");
                    String contactOS = clinic.getString("tel_office_1");
                    Log.d("FOR_LOG2", nameOS+"  "+addressOS + " " + contactOS);
                    hashmap = new HashMap<String, String>();

                    hashmap.put("name", "" + nameOS + "\n" + "Address : "+ addressOS + "\n" + "Tel no : "+ contactOS);
                    arrayList.add(hashmap);
                }

                final SimpleAdapter adapter = new SimpleAdapter(ListActivity.this, arrayList, R.layout.listitems, from, to);
                lView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
