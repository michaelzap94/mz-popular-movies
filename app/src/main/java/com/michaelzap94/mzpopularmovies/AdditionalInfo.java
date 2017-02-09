package com.michaelzap94.mzpopularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.michaelzap94.mzpopularmovies.Utilities.NetworkCatcher;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class AdditionalInfo extends AppCompatActivity {
    private ProgressDialog pDialog;
    private ImageView thumbnail;
    private TextView title;
    private TextView release;
    private TextView rating;
    private TextView plot;
    private Intent intent;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        title = (TextView) findViewById(R.id.movie_title);
        release = (TextView) findViewById(R.id.movie_release);
        rating = (TextView) findViewById(R.id.movie_rating);
        plot = (TextView) findViewById(R.id.movie_plot);
        intent = getIntent();

        if(intent.hasExtra(Intent.EXTRA_TEXT)&&NetworkCatcher.isOnline(AdditionalInfo.this)){
            String movieId = intent.getStringExtra(Intent.EXTRA_TEXT);
            getMovieInfo(movieId);
        }else{
            Toast.makeText(AdditionalInfo.this,"No internet connection", Toast.LENGTH_SHORT).show();

            onBackPressed();
        }
    }

    private void getMovieInfo(String type) {
        URL UrlMovieDb = NetworkCatcher.urlBuilder(type);
        new AdditionalInfo.MyAsync().execute(UrlMovieDb);

    }




    private class MyAsync extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdditionalInfo.this);
            pDialog.setMessage("Retreiving movie's information...");
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
      } catch (IOException e) {
              e.printStackTrace();
          }
          return resultsMovieDB;
      }

      @Override
      protected void onPostExecute(String resultsMovieDB) {

          if (resultsMovieDB != null && !resultsMovieDB.equals("")) {
              try {




                  JSONObject data = new JSONObject(resultsMovieDB);
                  title.setText(data.getString("original_title"));
                  rating.setText(data.getString("vote_average"));
                  release.setText(data.getString("release_date"));
                  plot.setText(data.getString("overview"));

                  String imgId = data.getString("poster_path");
                  imgUrl = NetworkCatcher.imgURLBuilder(imgId);
                  Picasso.with(AdditionalInfo.this).load(imgUrl).into(thumbnail);

              } catch (JSONException e) {
                  e.printStackTrace();
              }
          } else {
              Log.e("json", "error");
          }

          pDialog.dismiss();

      }


  }

}
