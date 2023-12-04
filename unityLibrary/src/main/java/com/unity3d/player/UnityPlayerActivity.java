package com.unity3d.player;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.os.Process;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UnityPlayerActivity extends Activity implements IUnityPlayerLifecycleEvents
{
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

    // Override this in your custom UnityPlayerActivity to tweak the command line arguments passed to the Unity Android Player
    // The command line arguments are passed as a string, separated by spaces
    // UnityPlayerActivity calls this from 'onCreate'
    // Supported: -force-gles20, -force-gles30, -force-gles31, -force-gles31aep, -force-gles32, -force-gles, -force-vulkan
    // See https://docs.unity3d.com/Manual/CommandLineArguments.html
    // @param cmdLine the current command line arguments, may be null
    // @return the modified command line string or null
    protected String updateUnityCommandLineArguments(String cmdLine)
    {
        return cmdLine;
    }

    // Setup activity layout
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        String cmdLine = updateUnityCommandLineArguments(getIntent().getStringExtra("unity"));
        getIntent().putExtra("unity", cmdLine);

        mUnityPlayer = new UnityPlayer(this, this);
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();
    }

    // When Unity player unloaded move task to background
    @Override public void onUnityPlayerUnloaded() {
        moveTaskToBack(true);
    }

    // Callback before Unity player process is killed
    @Override public void onUnityPlayerQuitted() {
    }

    @Override protected void onNewIntent(Intent intent)
    {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
        mUnityPlayer.newIntent(intent);
    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.destroy();
        super.onDestroy();
    }

    // If the activity is in multi window mode or resizing the activity is allowed we will use
    // onStart/onStop (the visibility callbacks) to determine when to pause/resume.
    // Otherwise it will be done in onPause/onResume as Unity has done historically to preserve
    // existing behavior.
    @Override protected void onStop()
    {
        super.onStop();

        if (!MultiWindowSupport.getAllowResizableWindow(this))
            return;

        mUnityPlayer.pause();
    }

    @Override protected void onStart()
    {
        super.onStart();

        if (!MultiWindowSupport.getAllowResizableWindow(this))
            return;

        mUnityPlayer.resume();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();

        MultiWindowSupport.saveMultiWindowMode(this);

        if (MultiWindowSupport.getAllowResizableWindow(this))
            return;

        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();

        if (MultiWindowSupport.getAllowResizableWindow(this) && !MultiWindowSupport.isMultiWindowModeChangedToTrue(this))
            return;

        mUnityPlayer.resume();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.onKeyUp(keyCode, event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.onKeyDown(keyCode, event); }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.onTouchEvent(event); }
    @Override public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.onGenericMotionEvent(event); }

    public void SendHighestScoreToAndroidApp_Ship(int score) {
        // Handle the score received from Unity
        Log.d("UnityPlayerActivity", "Received Star Ship score from Unity: " + score);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference userNameRef = database.getReference("Users").child(user.getUid()).child("Fullname");

            userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String fullname = dataSnapshot.getValue(String.class);

                    if (fullname != null) {
                        DatabaseReference scoresRef = database.getReference("Leaderboard").child("SpaceShip").child(user.getUid());
                        Map<String, Object> leaderboardData = new HashMap<>();
                        leaderboardData.put("UserID", user.getUid());
                        leaderboardData.put("Fullname", fullname);
                        leaderboardData.put("UserScore", score);

                        scoresRef.setValue(leaderboardData);
                    } else {
                        Log.d("UnityPlayerActivity", "Fullname is null");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                    Log.e("UnityPlayerActivity", "Error reading Fullname from Users", databaseError.toException());
                }
            });
        }
    }

    public void SendHighestScoreToAndroidApp_2048(int score) {
        // Handle the score received from Unity
        Log.d("UnityPlayerActivity", "Received 2048 score from Unity: " + score);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference userNameRef = database.getReference("Users").child(user.getUid()).child("Fullname");

            userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String fullname = dataSnapshot.getValue(String.class);

                    if (fullname != null) {
                        DatabaseReference scoresRef = database.getReference("Leaderboard").child("2048").child(user.getUid());
                        Map<String, Object> leaderboardData = new HashMap<>();
                        leaderboardData.put("UserID", user.getUid());
                        leaderboardData.put("Fullname", fullname);
                        leaderboardData.put("UserScore", score);

                        scoresRef.setValue(leaderboardData);
                    } else {
                        Log.d("UnityPlayerActivity", "Fullname is null");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                    Log.e("UnityPlayerActivity", "Error reading Fullname from Users", databaseError.toException());
                }
            });
        }
    }

    public void SendHighestScoreToAndroidApp_ColorBird(int score) {
        // Handle the score received from Unity
        Log.d("UnityPlayerActivity", "Received ColorBird score from Unity: " + score);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference userNameRef = database.getReference("Users").child(user.getUid()).child("Fullname");

            userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String fullname = dataSnapshot.getValue(String.class);

                    if (fullname != null) {
                        DatabaseReference scoresRef = database.getReference("Leaderboard").child("ColorBird").child(user.getUid());
                        Map<String, Object> leaderboardData = new HashMap<>();
                        leaderboardData.put("UserID", user.getUid());
                        leaderboardData.put("Fullname", fullname);
                        leaderboardData.put("UserScore", score);

                        scoresRef.setValue(leaderboardData);
                    } else {
                        Log.d("UnityPlayerActivity", "Fullname is null");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                    Log.e("UnityPlayerActivity", "Error reading Fullname from Users", databaseError.toException());
                }
            });
        }
    }

    public void SendHighestScoreToAndroidApp_DotRescue(int score) {
        // Handle the score received from Unity
        Log.d("UnityPlayerActivity", "Received Dot rescue score from Unity: " + score);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference userNameRef = database.getReference("Users").child(user.getUid()).child("Fullname");

            userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String fullname = dataSnapshot.getValue(String.class);

                    if (fullname != null) {
                        DatabaseReference scoresRef = database.getReference("Leaderboard").child("DotRescue").child(user.getUid());
                        Map<String, Object> leaderboardData = new HashMap<>();
                        leaderboardData.put("UserID", user.getUid());
                        leaderboardData.put("Fullname", fullname);
                        leaderboardData.put("UserScore", score);

                        scoresRef.setValue(leaderboardData);
                    } else {
                        Log.d("UnityPlayerActivity", "Fullname is null");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                    Log.e("UnityPlayerActivity", "Error reading Fullname from Users", databaseError.toException());
                }
            });
        }
    }

    public void SendHighestScoreToAndroidApp_Orbits(int score) {
        // Handle the score received from Unity
        Log.d("UnityPlayerActivity", "Received Orbit score from Unity: " + score);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference userNameRef = database.getReference("Users").child(user.getUid()).child("Fullname");

            userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String fullname = dataSnapshot.getValue(String.class);

                    if (fullname != null) {
                        DatabaseReference scoresRef = database.getReference("Leaderboard").child("Orbit").child(user.getUid());
                        Map<String, Object> leaderboardData = new HashMap<>();
                        leaderboardData.put("UserID", user.getUid());
                        leaderboardData.put("Fullname", fullname);
                        leaderboardData.put("UserScore", score);

                        scoresRef.setValue(leaderboardData);
                    } else {
                        Log.d("UnityPlayerActivity", "Fullname is null");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                    Log.e("UnityPlayerActivity", "Error reading Fullname from Users", databaseError.toException());
                }
            });
        }
    }

    public void SendHighestScoreToAndroidApp_Pixel(int score) {
        // Handle the score received from Unity
        Log.d("UnityPlayerActivity", "Received Pixel Adventure score from Unity: " + score);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference userNameRef = database.getReference("Users").child(user.getUid()).child("Fullname");

            userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String fullname = dataSnapshot.getValue(String.class);

                    if (fullname != null) {
                        DatabaseReference scoresRef = database.getReference("Leaderboard").child("PixelAdventure").child(user.getUid());
                        Map<String, Object> leaderboardData = new HashMap<>();
                        leaderboardData.put("UserID", user.getUid());
                        leaderboardData.put("Fullname", fullname);
                        leaderboardData.put("UserScore", score);

                        scoresRef.setValue(leaderboardData);
                    } else {
                        Log.d("UnityPlayerActivity", "Fullname is null");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                    Log.e("UnityPlayerActivity", "Error reading Fullname from Users", databaseError.toException());
                }
            });
        }
    }





}
