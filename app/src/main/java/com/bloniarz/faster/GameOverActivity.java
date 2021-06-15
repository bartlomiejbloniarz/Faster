package com.bloniarz.faster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bloniarz.faster.R;

import java.util.Locale;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        TextView textView = findViewById(R.id.score_text_view);
        textView.setText(String.format(Locale.US, "%d", getIntent().getIntExtra("score", 0)));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void backClick(View view) {
        onBackPressed();
    }

    public void playAgain(View view){
        startActivity(new Intent(this, GameActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}