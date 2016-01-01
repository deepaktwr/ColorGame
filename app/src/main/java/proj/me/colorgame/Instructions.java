package proj.me.colorgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import static proj.me.colorgame.helper.Utils.*;

/**
 * Created by root on 29/11/15.
 */
public class Instructions extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    private TextView  levelTexts;
    private RadioButton easy, medium, hard;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String levelText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);

        levelTexts = (TextView)findViewById(R.id.game_level_text);
        easy = (RadioButton)findViewById(R.id.radio_easy);
        medium = (RadioButton)findViewById(R.id.radio_medium);
        hard = (RadioButton)findViewById(R.id.radio_hard);

        easy.setOnCheckedChangeListener(this);
        medium.setOnCheckedChangeListener(this);
        hard.setOnCheckedChangeListener(this);

        easy.setTag(1000);
        medium.setTag(800);
        hard.setTag(600);

        preferences = getSharedPreferences("levels", MODE_PRIVATE);

        switch(preferences.getInt("level", 1000)){
            case 1000:
                easy.setChecked(true);
                break;
            case 800:
                medium.setChecked(true);
                break;
            case 600:
                hard.setChecked(true);
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked) return;
        editor = preferences.edit();
        editor.putInt("level", (int) buttonView.getTag());
        editor.apply();

        switch(preferences.getInt("level", 1000)){
            case 1000:
                levelText = String.format(getResources().getString(R.string.game_level_text), "1",
                        ""+ E_POINT, "+"+E_POINT_30_PER_TIME, "300", "+"+E_POINT_20_PER_TIME, "200",
                        ""+MAX_STROKES, "+"+E_POINT_MAX_STROKES);
                break;
            case 800:
                levelText = String.format(getResources().getString(R.string.game_level_text), "0.8",
                        ""+M_POINT, "+"+M_POINT_30_PER_TIME, "240", "+"+M_POINT_20_PER_TIME, "160",
                        ""+MAX_STROKES, "+"+M_POINT_MAX_STROKES);
                break;
            case 600:
                levelText = String.format(getResources().getString(R.string.game_level_text), "0.6",
                        ""+H_POINT, "+"+H_POINT_30_PER_TIME, "180", "+"+H_POINT_20_PER_TIME, "120",
                        ""+MAX_STROKES, "+"+H_POINT_MAX_STROKES);
                break;
        }

        levelTexts.setText(levelText);
    }
}
