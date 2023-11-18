package com.example.gamehub.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamehub.Interface.ItemClickListener;
import com.example.gamehub.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageUsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CircleImageView imgUser;
    public TextView tvName, tvStatus;
    public ItemClickListener listener;

    public ManageUsersViewHolder(@NonNull View itemView) {
        super(itemView);
        imgUser = itemView.findViewById(R.id.img_user);
        tvName = itemView.findViewById(R.id.tv_fullname);
        tvStatus = itemView.findViewById(R.id.tv_status);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(),false);
    }
}
