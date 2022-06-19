package com.example.androidtv;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends FragmentActivity {
    TextView txt;
    Button btn;
    String futureJokeString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.txtJoke);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(view -> new  JokeLoader().execute());
    }
    private class JokeLoader extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids){
            String jsonString = getJson("https://api.chucknorris.io/jokes/random");

            try{
                JSONObject jsonObject = new JSONObject(jsonString);
                futureJokeString = jsonObject.getString("value");
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            futureJokeString = "";
            txt.setText("Loading...");
        }
        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);

            if(!futureJokeString.equals("")){
                txt.setText(futureJokeString);
            }
        }

        private String getJson(String link){
            String data = "";
            try{
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
                            "utf-8"));
                    data = r.readLine();
                    urlConnection.disconnect();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            return data;
        }
    }
}