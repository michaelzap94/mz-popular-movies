package com.michaelzap94.mzpopularmovies.Utilities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.michaelzap94.mzpopularmovies.AdditionalInfo;
import com.michaelzap94.mzpopularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by zapatacajas on 09/02/2017.
 */

public class MovieAdapter  extends ArrayAdapter<Movie> {
        private Context ct;
        public MovieAdapter(Context context, ArrayList<Movie> movies) {
            super(context, 0, movies);
            this.ct = context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Movie movie = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_item, parent, false);
            }
            ImageButton imgBtn = (ImageButton) convertView.findViewById(R.id.single_item_img);

            imgBtn.setTag(movie.id);

            Picasso.with(ct).load(movie.imgId).into(imgBtn);

            imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String id = (String) view.getTag();
                    Intent intent = new Intent(ct, AdditionalInfo.class);
                    intent.putExtra(Intent.EXTRA_TEXT, id);
                    ct.startActivity(intent);
                }
            });

            return convertView;
        }

}
