package proj.me.colorgame.fragments;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import proj.me.colorgame.R;
import proj.me.colorgame.helper.ActivityCallback;
import proj.me.colorgame.helper.FragmentCallback;
import proj.me.colorgame.helper.FragmentListener;
import proj.me.colorgame.helper.ViewTouchListener;

/**
 * Created by root on 28/11/15.
 */
public class ColorFrag extends Fragment implements FragmentCallback, FragmentListener {
    private View rootView;

    private Timer timer;

    private SharedPreferences preferences;

    private int currentGreyViewId = 0, currentColorNumber = 0;
    private View redV, yellowV, greenV, blueV;
    private ActivityCallback activityCallback;
    private Activity context;

    private long startedTime, tappedTime;
    private boolean isTapped;
    private int currentTappedId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return rootView = inflater.inflate(R.layout.frag_color, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        redV = rootView.findViewById(R.id.red);
        yellowV = rootView.findViewById(R.id.yellow);
        greenV = rootView.findViewById(R.id.green);
        blueV = rootView.findViewById(R.id.blue);

        AnimatorSet tapAnim = new AnimatorSet();
        tapAnim.play(AnimatorInflater.loadAnimator(context, R.anim.taps_anim));

        redV.setOnTouchListener(new ViewTouchListener(this, tapAnim));
        yellowV.setOnTouchListener(new ViewTouchListener(this, tapAnim));
        greenV.setOnTouchListener(new ViewTouchListener(this, tapAnim));
        blueV.setOnTouchListener(new ViewTouchListener(this, tapAnim));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        preferences = context.getSharedPreferences("levels", Context.MODE_PRIVATE);
        try {
            activityCallback = (ActivityCallback) activity;
        }catch(ClassCastException exp){}

    }

    private void performResult(){
        if(startedTime != 0 && !isTapped){
            setCurrentTappedId(0);
            return;
        }
        isTapped = false;
        startedTime = System.currentTimeMillis();
        Random random = new Random();
        int val = random.nextInt(4);
        val = val == currentColorNumber ? (val >= 2 ? val - 1 : val + 1) : val;
        switch(currentGreyViewId){
            case R.id.red:
                redV.setBackgroundColor(context.getResources().getColor(R.color.red));
                break;
            case R.id.yellow:
                yellowV.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                break;
            case R.id.green:
                greenV.setBackgroundColor(context.getResources().getColor(R.color.green));
                break;
            case R.id.blue:
                blueV.setBackgroundColor(context.getResources().getColor(R.color.blue));
                break;
        }
        switch(val){
            case 0:
                //red
                redV.setBackgroundColor(context.getResources().getColor(R.color.grey));
                currentGreyViewId = R.id.red;
                break;
            case 1:
                //yellow
                yellowV.setBackgroundColor(context.getResources().getColor(R.color.grey));
                currentGreyViewId = R.id.yellow;
                break;
            case 2:
                //green
                greenV.setBackgroundColor(context.getResources().getColor(R.color.grey));
                currentGreyViewId = R.id.green;
                break;
            case 3:
                //blue
                blueV.setBackgroundColor(context.getResources().getColor(R.color.grey));
                currentGreyViewId = R.id.blue;
                break;
        }
        currentColorNumber = val;
    }

    @Override
    public void onResume() {
        super.onResume();
        redV.setBackgroundColor(context.getResources().getColor(R.color.red));
        greenV.setBackgroundColor(context.getResources().getColor(R.color.green));
        yellowV.setBackgroundColor(context.getResources().getColor(R.color.yellow));
        blueV.setBackgroundColor(context.getResources().getColor(R.color.blue));
    }

    @Override
    public void onPause() {
        super.onPause();
        if(timer != null)timer.cancel();
        resetValues();
    }

    @Override
    public void setCurrentTappedId(int viewId) {
        currentTappedId = viewId;
        isTapped = true;
        tappedTime = System.currentTimeMillis();
        Log.e("started time", "\n"+startedTime);
        Log.e("tapped time", ""+tappedTime);
        Log.e("total time", "" + (tappedTime - startedTime));
        if(currentGreyViewId == viewId) {
            Log.e("Result:-", "success\n");
            activityCallback.processResult(currentGreyViewId, (int)(tappedTime - startedTime), true);
            return;
        }
        /*switch(currentGreyViewId){
            case R.id.red:
                Toast.makeText(context, "red failed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.blue:
                Toast.makeText(context, "blue failed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.yellow:
                Toast.makeText(context, "yellow failed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.green:
                Toast.makeText(context, "green failed", Toast.LENGTH_SHORT).show();
                break;
        }*/
        timer.cancel();

        redV.setBackgroundColor(context.getResources().getColor(R.color.red));
        greenV.setBackgroundColor(context.getResources().getColor(R.color.green));
        yellowV.setBackgroundColor(context.getResources().getColor(R.color.yellow));
        blueV.setBackgroundColor(context.getResources().getColor(R.color.blue));

        activityCallback.processResult(currentGreyViewId, (int) (tappedTime - startedTime), false);
        resetValues();
    }

    @Override
    public int getCurrentTappedId() {
        return currentTappedId;
    }

    @Override
    public int getCurrentGreyId() {
        return currentGreyViewId;
    }

    @Override
    public void resetTimer(boolean shouldStart) {
        if(shouldStart) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            performResult();
                        }
                    });

                }
            };
            timer.scheduleAtFixedRate(timerTask, 1000, preferences.getInt("level", 1000));
        }else if(timer != null)
            timer.cancel();
    }

    private void resetValues(){
        currentGreyViewId = 0;
        currentColorNumber = 0;
        startedTime = 0;
        tappedTime = 0;
        isTapped = false;
        currentTappedId = 0;
    }
}
