package com.bloniarz.faster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bloniarz.faster.R;

public class MainActivity extends AppCompatActivity {

    private boolean themeSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!themeSet) {
            SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
            resetTheme(sharedPreferences.getInt("settings_theme", 0));
            themeSet = true;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playGame(View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void showScoreboard(View view){
        Intent intent = new Intent(this, ScoreboardActivity.class);
        startActivity(intent);
    }

    public void showSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public static void resetTheme(int pos){
        if (pos == 0)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        else if (pos == 1)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}