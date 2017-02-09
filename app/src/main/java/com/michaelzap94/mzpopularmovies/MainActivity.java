package com.michaelzap94.mzpopularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.michaelzap94.mzpopularmovies.Utilities.Movie;
import com.michaelzap94.mzpopularmovies.Utilities.MovieAdapter;
import com.michaelzap94.mzpopularmovies.Utilities.NetworkCatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.michaelzap94.mzpopularmovies.R.layout.*;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    JSONArray jsonArray;
    ArrayList<Movie> movieArrayList = new ArrayList<>();
    MovieAdapter movieAdapter;
    GridView myGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMovies("popular");
    }


    private void getMovies(String type) {
        if(NetworkCatcher.isOnline(MainActivity.this)){
            setContentView(R.layout.activity_main);
            URL UrlMovieDb = NetworkCatcher.urlBuilder(type);
            new MainActivity.MyAsync().execute(UrlMovieDb);
        }else{
            Toast.makeText(MainActivity.this,"No internet connection", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.no_internet);

        }


    }



    private class MyAsync extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myGrid = (GridView) findViewById(R.id.grid_id);
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Retreiving movies...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }


        @Override
        protected String doInBackground(URL... params) {

            URL UrlMovieDb = params[0];
            String resultsMovieDB = null;
            try {
                resultsMovieDB = NetworkCatcher.getResponseFromHttpUrl(UrlMovieDb);

                movieAdapter = new MovieAdapter(MainActivity.this, movieArrayList);
                movieAdapter.clear();

                try {
                    JSONObject data = new JSONObject(resultsMovieDB);
                    jsonArray = data.getJSONArray("results");
                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject insideObject = jsonArray.getJSONObject(i);
                        String imgUrl = NetworkCatcher.imgURLBuilder(insideObject.getString("poster_path"));
                        Movie movieObject = new Movie(insideObject.getString("id"), imgUrl);
                        movieAdapter.add(movieObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultsMovieDB;
        }

        @Override
        protected void onPostExecute(String resultsMovieDB) {
            myGrid.setAdapter(movieAdapter);
            pDialog.dismiss();


        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemIdMenu = item.getItemId();

        if(itemIdMenu==R.id.most_popular_btn){
            getMovies("popular");
            return true;
        }else if(itemIdMenu==R.id.highest_rated_btn){
            getMovies("top_rated");
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }

    }

}
