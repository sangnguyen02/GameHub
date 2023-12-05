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
import com.example.gamehub.R;

import java.util.List;

public class GenderSpinnerAdapter extends ArrayAdapter<GenderSpinner> {

    public GenderSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<GenderSpinner> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gender_selected, parent, false);

        ImageView imgCategory = convertView.findViewById(R.id.img_gender_selected);

        GenderSpinner genderSpinner = this.getItem(position);
        if (genderSpinner != null) {
            imgCategory.setImageResource(
                    parent.getContext().getResources().getIdentifier(
                            genderSpinner.getImageName(),
                            "drawable",
                            parent.getContext().getPackageName()
                    )
            );
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gender_spinner, parent, false);

        TextView tvCategory = convertView.findViewById(R.id.tv_gender);
        ImageView imgCategory = convertView.findViewById(R.id.img_gender);

        GenderSpinner genderSpinner = this.getItem(position);
        if (genderSpinner != null) {
            tvCategory.setText(genderSpinner.getName());
            imgCategory.setImageResource(
                    parent.getContext().getResources().getIdentifier(
                            genderSpinner.getImageName(),
                            "drawable",
                            parent.getContext().getPackageName()
                    )
            );
        }
        return convertView;
    }
}
