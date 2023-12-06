package com.example.gamehub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gamehub.Models.GenderSpinner;
import com.example.gamehub.Models.RatingSpinner;
import com.example.gamehub.R;

import java.util.List;

public class RatingSpinnerAdapter  extends ArrayAdapter<RatingSpinner> {
    public RatingSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<RatingSpinner> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating_selected, parent, false);

        TextView tvRatingSelected = convertView.findViewById(R.id.tv_rating_selected);

        RatingSpinner ratingSpinner = this.getItem(position);
        if (ratingSpinner != null) {
            tvRatingSelected.setText(ratingSpinner.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating_spinner, parent, false);

        TextView tvRatingGame = convertView.findViewById(R.id.tv_rating_game);

        RatingSpinner ratingSpinner = this.getItem(position);
        if (ratingSpinner != null) {
            tvRatingGame.setText(ratingSpinner.getName());
        }
        return convertView;
    }
}
