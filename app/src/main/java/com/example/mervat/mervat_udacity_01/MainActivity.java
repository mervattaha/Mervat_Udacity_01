package com.example.mervat.mervat_udacity_01;

/**
 * Created by Mervat on 11/30/2017.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.os.AsyncTask;
import android.util.Log;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView MoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MoviesList = (ListView) findViewById(R.id.list);
        MoviesList.setOnItemClickListener(this);
        Spinner spin=(Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //02015817544181a156d9a2287221ced3
                switch (i) {
                    case 0:
                        new CheckConnectionStatus().execute("https://api.themoviedb.org/3/movie/popular?api_key=02015817544181a156d9a2287221ced3&language=en-US");
                        break;
                    case 1:
                        new CheckConnectionStatus().execute("https://api.themoviedb.org/3/movie/top_rated?api_key=02015817544181a156d9a2287221ced3&language=en-US");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("MOVIE_DETAILS", (MovieInfo)parent.getItemAtPosition(position));
        startActivity(intent);
    }

    class CheckConnectionStatus extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = bufferedReader.readLine();
                bufferedReader.close();

                return s;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            try {

                jsonObject = new JSONObject(s);

                ArrayList<MovieInfo> MovieList = new ArrayList<>();

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i =0; i<jsonArray.length();i++)
                {

                    JSONObject object = jsonArray.getJSONObject(i);
                    MovieInfo MovieInfo = new MovieInfo();
                    MovieInfo.setOriginal_title(object.getString("original_title"));
                    MovieInfo.setVote_average(object.getDouble("vote_average"));
                    MovieInfo.setOverview(object.getString("overview"));
                    MovieInfo.setRelease_date(object.getString("release_date"));
                    MovieInfo.setPoster_path(object.getString("poster_path"));
                    MovieList.add(MovieInfo);

                }

                MoviesArrayAdapter movieArrayAdapter = new MoviesArrayAdapter(MainActivity.this,R.layout.movies_list,MovieList);

                MoviesList.setAdapter(movieArrayAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
