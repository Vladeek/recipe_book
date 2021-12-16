package com.example.harrand.recipebook;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AppCompatActivity {
    String apikey = "71b7e3c6011ce8c487a1e54dbf53eb7d";
    String city = "Minsk";
    String readyurl = "https://api.openweathermap.org/data/2.5/weather?q="
            +city+"&appid="+apikey+"&units=metric&lang=us";
    TextView weatherinfo;
    private ArrayAdapter<User> adapter;
    private EditText nameText, ageText;
    private List<User> users;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        nameText = findViewById(R.id.nameText);
        ageText = findViewById(R.id.ageText);
        listView = findViewById(R.id.list);
        users = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        listView.setAdapter(adapter);

        weatherinfo = findViewById(R.id.weathertv);
        try {
            new GetURLData().execute(readyurl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toRecipe(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void addUser(View view) {
        String name = nameText.getText().toString();
        int age = Integer.parseInt(ageText.getText().toString());
        User user = new User(name, age);
        users.add(user);
        adapter.notifyDataSetChanged();
    }

    public void save(View view) {

        boolean result = JSONHelper.exportToJSON(this, users);
        if (result) {
            Toast.makeText(this, "Save data", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Don't save data", Toast.LENGTH_LONG).show();
        }
    }

    public void open(View view) {
        users = JSONHelper.importFromJSON(this);
        if (users != null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
            listView.setAdapter(adapter);
            Toast.makeText(this, "Save data", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Don't save data", Toast.LENGTH_LONG).show();
        }
    }

    private class GetURLData extends AsyncTask<String, String, String> {

        TextView weatherinfo = findViewById(R.id.weathertv);

        protected void onPreExecute(){
            super.onPreExecute();
            weatherinfo.setText("Download weather data");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line=reader.readLine()) != null)
                    buffer.append(line).append("\n");

                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();

                try {
                    if (reader != null) {
                        reader.close();
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                weatherinfo.setText(jsonObject.getString("name") + ": " + jsonObject.getJSONObject("main").getDouble("temp")+", " + jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
            } catch(Exception e) {

            }
        }
    }
}