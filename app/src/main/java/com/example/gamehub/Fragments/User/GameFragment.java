package com.example.gamehub.Fragments.User;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamehub.Adapters.BannerViewPager2Adapter;
import com.example.gamehub.Adapters.GameListAdapter;
import com.example.gamehub.Models.Game;
import com.example.gamehub.Models.ImageBanner;
import com.example.gamehub.R;
import com.google.android.material.button.MaterialButton;
import com.unity3d.player.UnityPlayerActivity;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class GameFragment extends Fragment {
    View rootView;
    MaterialButton openGameHub_btn;
    RecyclerView recyclerListGames;
    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCircleIndicator3;
    private List<ImageBanner> mListImageBanner;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mViewPager2.getCurrentItem() == mListImageBanner.size() - 1) {
                mViewPager2.setCurrentItem(0);
            } else {
                mViewPager2.setCurrentItem(mViewPager2.getCurrentItem() + 1);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_game, container, false);
        initUI();
        initListener();
        return rootView;
    }

    private void initUI() {
        openGameHub_btn = rootView.findViewById(R.id.open_game_hub_btn);
        mViewPager2 = rootView.findViewById(R.id.view_pager_2);
        mCircleIndicator3 = rootView.findViewById(R.id.circle_indicator_3);

        mListImageBanner = getListImageBanner();
        BannerViewPager2Adapter adapter = new BannerViewPager2Adapter(mListImageBanner);
        mViewPager2.setAdapter(adapter);

        mCircleIndicator3.setViewPager(mViewPager2);

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 5000);
            }
        });

        recyclerListGames = rootView.findViewById(R.id.recycler_list_games);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerListGames.setLayoutManager(layoutManager);
        List<Game> gameList = createGameItemList();
        GameListAdapter gameAdapter = new GameListAdapter(gameList);
        recyclerListGames.setAdapter(gameAdapter);
    }

    private void initListener() {
        openGameHub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), UnityPlayerActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<ImageBanner> getListImageBanner() {
        List<ImageBanner> list = new ArrayList<>();
        list.add(new ImageBanner(R.drawable.banner_game));
        list.add(new ImageBanner(R.drawable.banner_unity));
        list.add(new ImageBanner(R.drawable.banner_androidstudio));
        list.add(new ImageBanner(R.drawable.banner_firebase));

        return list;
    }
    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 5000);
    }

    private List<Game> createGameItemList() {
        List<Game> gameItemList = new ArrayList<>();

        // Add your game items
        gameItemList.add(new Game(R.drawable.spaceship, "SpaceShip"));
        gameItemList.add(new Game(R.drawable.colorbird, "ColorBird"));
        gameItemList.add(new Game(R.drawable.logo_2048, "2048"));
        gameItemList.add(new Game(R.drawable.pixel, "PixelAdventure"));
        gameItemList.add(new Game(R.drawable.orbit, "Orbit"));
        gameItemList.add(new Game(R.drawable.dotrescue, "DotRescue"));

        return gameItemList;
    }
}