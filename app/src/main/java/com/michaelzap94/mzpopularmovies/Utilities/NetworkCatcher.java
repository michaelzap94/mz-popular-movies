package com.michaelzap94.mzpopularmovies.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by zapatacajas on 08/02/2017.
 */

public class NetworkCatcher {
    private static final String PARAM_KEY_VALUE = "[YOUR_API_KEY]";
    private static final String PARAM_KEY = "api_key";
    private static final String BASE_MOVIE_DB = "https://api.themoviedb.org/3/movie/";


    public static String imgURLBuilder(String urlID){
        String baseURL =  "http://image.tmdb.org/t/p/w500/";
        return baseURL+urlID;
    }

   public static boolean isOnline(Context ct) {
        ConnectivityManager cm =
                (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static URL urlBuilder(String pathID) {

        Uri builtUri = Uri.parse(BASE_MOVIE_DB).buildUpon()
                .appendPath(pathID)
                .appendQueryParameter(PARAM_KEY, PARAM_KEY_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

  public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

