package com.bloniarz.faster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bloniarz.faster.database.Gift;
import com.bloniarz.faster.database.Score;
import com.bloniarz.faster.fragments.PauseDialogFragment;
import com.bloniarz.faster.game.GameView;

import java.util.Date;
import java.util.Locale;

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
        gameViewModel.insertScore(new Score(new Date(), score));
        if (score>=500)
            gameViewModel.insertGift(new Gift(R.drawable.card_500, "500 points", R.drawable.card_500_thumbnail));
        if (score>=1000)
            gameViewModel.insertGift(new Gift(R.drawable.card_1000, "1000 points", R.drawable.card_1000_thumbnail));
        if (score>=2000)
            gameViewModel.insertGift(new Gift(R.drawable.card_2000, "2000 points", R.drawable.card_2000_thumbnail));
        if (score>=5000)
            gameViewModel.insertGift(new Gift(R.drawable.card_5000, "5000 points", R.drawable.card_5000_thumbnail));
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
