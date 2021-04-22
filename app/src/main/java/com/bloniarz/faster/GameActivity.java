package com.bloniarz.faster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.fragment.app.FragmentActivity;

import com.bloniarz.faster.R;

public class GameActivity extends FragmentActivity {

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.gameView);
    }

    public void pauseButtonClick(View view){
        onBackPressed();
    }

    public void goBackToMenu(){
        startActivity(new Intent(this, GameOverActivity.class));
    }

    @Override
    public void onBackPressed() {
        gameView.pause();
        PauseDialogFragment dialogFragment = new PauseDialogFragment(gameView);
        dialogFragment.show(getSupportFragmentManager(), dialogFragment.getTag());
    }
}
