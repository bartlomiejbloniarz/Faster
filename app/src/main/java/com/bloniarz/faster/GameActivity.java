package com.bloniarz.faster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;

import com.bloniarz.faster.fragments.PauseDialogFragment;
import com.bloniarz.faster.game.GameView;

public class GameActivity extends FragmentActivity {

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.gameView);
    }

    public void pauseButtonClick(View view){
        onBackPressed();
    }

    public void gameOver(){
        startActivity(new Intent(this, GameOverActivity.class));
    }

    @Override
    public void onBackPressed() {
        gameView.pause();
        PauseDialogFragment dialogFragment = new PauseDialogFragment(gameView);
        dialogFragment.show(getSupportFragmentManager(), dialogFragment.getTag());
    }
}
