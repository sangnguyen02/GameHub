package com.example.gamehub.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamehub.Activities.User.ViewAllRatingActivity;
import com.example.gamehub.Models.Game;
import com.example.gamehub.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {
    private static List<Game> gameItemList;

    public GameListAdapter(List<Game> gameItemList) {
        this.gameItemList = gameItemList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_list, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameItemList.get(position);
        holder.bind(game);
    }

    @Override
    public int getItemCount() {
        return gameItemList.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView imageView;
        private TextView textView;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.circle_image_view);
            textView = itemView.findViewById(R.id.text_view_game_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position =getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Game gameClicked = gameItemList.get(position);

                        String gameName = gameClicked.getGameName();

                        Intent intent = new Intent(itemView.getContext(), ViewAllRatingActivity.class);

                        intent.putExtra("GameListClickedName", gameName);

                        itemView.getContext().startActivity(intent);
                    }
                }
            });
        }

        public void bind(Game game) {
            imageView.setImageResource(game.getImageResource());
            textView.setText(game.getGameName());
        }

        @Override
        public void onClick(View view) {

        }
    }
}
