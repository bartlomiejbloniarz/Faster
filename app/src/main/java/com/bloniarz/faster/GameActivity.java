package com.bloniarz.faster;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bloniarz.faster.database.Score;
import com.bloniarz.faster.fragments.PauseDialogFragment;
import com.bloniarz.faster.game.GameView;

import java.util.Date;
import java.util.Locale;
import java.util.Observable;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private TextView scoreTextView;
    private GameViewModel gameViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        gameView = findViewById(R.id.gameView);
        scoreTextView = findViewById(R.id.textView);
        //scoreTextView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
    }

    public void pauseButtonClick(View view){
        onBackPressed();
    }

    public void gameOver(int score){
        gameViewModel.insert(new Score(new Date(), score));
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("score", score);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        gameView.pause();
        PauseDialogFragment dialogFragment = new PauseDialogFragment(gameView, this);
        dialogFragment.show(getSupportFragmentManager(), dialogFragment.getTag());
    }

    public void setScore(int score){
        scoreTextView.setText(String.format(Locale.US, "%d", score));
    }


}
