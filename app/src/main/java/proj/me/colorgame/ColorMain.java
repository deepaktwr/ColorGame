package proj.me.colorgame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import proj.me.colorgame.helper.ActivityCallback;
import proj.me.colorgame.helper.FragmentListener;
import proj.me.colorgame.helper.Utils;

public class ColorMain extends AppCompatActivity implements ActivityCallback{

    private View playIcon, playRing, whitePoint;
    private RelativeLayout playBg;
    private AnimatorSet playOn, playOff, scoreText, benefitText;

    private int redC, greenC, blueC, yellowC, greyC;

    private FragmentListener fragmentListener;

    private TextView benefit;

    private TextView score;

    private SharedPreferences pref;
    private Intent instructionIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_main);

        fragmentListener = (FragmentListener)getFragmentManager().findFragmentById(R.id.color_frag);

        pref = getSharedPreferences("levels", MODE_PRIVATE);

        instructionIntent = new Intent(this, Instructions.class);


        redC = getResources().getColor(R.color.red);
        greenC = getResources().getColor(R.color.green);
        yellowC = getResources().getColor(R.color.yellow);
        blueC = getResources().getColor(R.color.blue);
        greyC = getResources().getColor(R.color.grey);

        benefit = (TextView)findViewById(R.id.benefit);

        score = (TextView)findViewById(R.id.score);

        playIcon = findViewById(R.id.play_icon);
        playRing = findViewById(R.id.play_ring);
        whitePoint = findViewById(R.id.center_white);
        playBg = (RelativeLayout)findViewById(R.id.play_bg);

        setUpAnimator();

        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.play_icon && !playOn.isRunning() && !playOn.isStarted())
                    playOn.start();
            }
        });

    }
    private void setUpAnimator(){
        AnimatorSet playOnIcon = new AnimatorSet();
        playOnIcon.playSequentially(AnimatorInflater.loadAnimator(ColorMain.this, R.anim.play_on_anim_up),
                AnimatorInflater.loadAnimator(ColorMain.this, R.anim.play_on));
        playOnIcon.setTarget(playIcon);

        AnimatorSet playOnAlpha = new AnimatorSet();
        playOnAlpha.play(AnimatorInflater.loadAnimator(ColorMain.this, R.anim.play_on_alpha));
        playOnAlpha.setTarget(playIcon);

        AnimatorSet playOnRing = new AnimatorSet();
        playOnRing.playSequentially(AnimatorInflater.loadAnimator(ColorMain.this, R.anim.play_on_anim_up),
                AnimatorInflater.loadAnimator(ColorMain.this, R.anim.play_on));
        playOnRing.setTarget(playRing);

        playOn = new AnimatorSet();
        playOn.playTogether(playOnAlpha, playOnIcon, playOnRing);
        playOn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                score.setText("SCORE");
                score.setTextColor(greyC);
                whitePoint.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                playBg.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                playIcon.setVisibility(View.GONE);
                playRing.setVisibility(View.GONE);
                fragmentListener.resetTimer(true);
                playBg.setClickable(false);
                whitePoint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        AnimatorSet playOffIcon = new AnimatorSet();
        playOffIcon.playSequentially(AnimatorInflater.loadAnimator(ColorMain.this, R.anim.play_off),
                AnimatorInflater.loadAnimator(ColorMain.this, R.anim.play_off_down));
        playOffIcon.setTarget(playIcon);

        AnimatorSet playOffRing = new AnimatorSet();
        playOffRing.playSequentially(AnimatorInflater.loadAnimator(ColorMain.this, R.anim.play_off),
                AnimatorInflater.loadAnimator(ColorMain.this, R.anim.play_off_down));
        playOffRing.setTarget(playRing);

        playOff = new AnimatorSet();
        playOff.playTogether(playOffIcon, playOffRing);

        playOff.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                whitePoint.setVisibility(View.GONE);
                playIcon.setAlpha(1);
                playIcon.setVisibility(View.VISIBLE);
                playRing.setVisibility(View.VISIBLE);
                playBg.setVisibility(View.VISIBLE);
                playBg.setClickable(true);
                playBg.setBackgroundColor(getResources().getColor(R.color.bg));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fragmentListener.resetTimer(false);
                whitePoint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });


        scoreText = new AnimatorSet();
        scoreText.play(AnimatorInflater.loadAnimator(ColorMain.this, R.anim.score_text));
        scoreText.setTarget(score);

        scoreText.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                String lastScore = score.getText().toString();
                totalScore += lastScore.equals("SCORE") ? 0 : Integer.parseInt(lastScore);
                score.setText(""+totalScore);
                score.setTextColor(color);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                score.setTextColor(greyC);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        benefitText = new AnimatorSet();
        benefitText.play(AnimatorInflater.loadAnimator(ColorMain.this, R.anim.benefit_text));
        benefitText.setTarget(benefit);

        benefitText.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                benefit.setText("+"+totalScore);
                benefit.setTextColor(color);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            startActivity(instructionIntent);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //playOff.start();
        benefit.setAlpha(0);

        playIcon.setAlpha(1);
        playIcon.setVisibility(View.VISIBLE);
        playRing.setVisibility(View.VISIBLE);
        playBg.setVisibility(View.VISIBLE);
        playBg.setClickable(true);
        playBg.setBackgroundColor(getResources().getColor(R.color.bg));
        fragmentListener.resetTimer(false);
        whitePoint.setVisibility(View.VISIBLE);


    }

    private int redCount, blueCount, greenCount, yellowCount;
    private int totalScore, color;

    @Override
    public void processResult(int viewId, int interval, boolean result) {
        String lastScore = score.getText().toString();
        if(!result){
            score.setText("Last Score : "+(lastScore.equals("SCORE")?"0":lastScore));
            score.setTextColor(greyC);
            //do additional animations
            resetValues();
            playOff.start();
            return;
        }

        boolean is50Per = false, is30Per = false;

        boolean isMaxStrokes = false;

        switch(viewId){
            case R.id.red:
                color = redC;
                redCount++;
                isMaxStrokes = redCount == Utils.MAX_STROKES;
                if(isMaxStrokes) redCount = 0;
                break;
            case R.id.blue:
                color = blueC;
                blueCount++;
                isMaxStrokes = blueCount == Utils.MAX_STROKES;
                if(isMaxStrokes) blueCount = 0;
                break;
            case R.id.green:
                color = greenC;
                greenCount++;
                isMaxStrokes = greenCount == Utils.MAX_STROKES;
                if(isMaxStrokes) greenCount = 0;
                break;
            case R.id.yellow:
                color = yellowC;
                yellowCount++;
                isMaxStrokes = yellowCount == Utils.MAX_STROKES;
                if(isMaxStrokes) yellowCount = 0;
                break;
        }



        switch(pref.getInt("level", 1000)){
            case 600:
                is50Per = (600 * (3f/10f)) >= interval;
                is30Per = (600 * (2f/10f)) >= interval;
                totalScore = isMaxStrokes ? Utils.H_POINT_MAX_STROKES :
                        is30Per ? Utils.H_POINT_20_PER_TIME :
                                is50Per?Utils.H_POINT_30_PER_TIME : Utils.H_POINT;
                break;
            case 800:
                is50Per = (800 * (3f/10f)) >= interval;
                is30Per = (800 * (2f/10f)) >= interval;
                totalScore = isMaxStrokes ? Utils.M_POINT_MAX_STROKES :
                        is30Per ? Utils.M_POINT_20_PER_TIME :
                                is50Per?Utils.M_POINT_30_PER_TIME : Utils.M_POINT;
                break;
            case 1000:
                is50Per = (1000 * (3f/10f)) >= interval;
                is30Per = (1000 * (2f/10f)) >= interval;
                totalScore = isMaxStrokes ? Utils.E_POINT_MAX_STROKES :
                        is30Per ? Utils.E_POINT_20_PER_TIME :
                                is50Per?Utils.E_POINT_30_PER_TIME : Utils.E_POINT;
                break;
        }

        if(isMaxStrokes || is50Per || is30Per)
            benefitText.start();

        scoreText.start();
    }

    private void resetValues(){
        redCount = 0;
        greenCount = 0;
        yellowCount = 0;
        blueCount = 0;
        totalScore = 0;
        color = greyC;
    }

    @Override
    protected void onPause() {
        super.onPause();
        resetValues();
    }
    /*private enum Levels {
        EASY(1000),
        MEDIUM(800),
        HARD(600);

        private int interval;
        Levels(int interval){
            this.interval = interval;
        }
    }*/
}
