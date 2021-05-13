package com.bloniarz.faster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bloniarz.faster.database.Score;
import com.bloniarz.faster.database.view.ScoreListAdapter;

import java.util.Date;
import java.util.Random;

public class ScoreboardActivity extends AppCompatActivity {

    private ScoreboardViewModel scoreboardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        RecyclerView recyclerView = findViewById(R.id.scoreboard_view);
        final ScoreListAdapter adapter = new ScoreListAdapter(new ScoreListAdapter.ScoreDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scoreboardViewModel = new ViewModelProvider(this).get(ScoreboardViewModel.class);
        scoreboardViewModel.getTopScores().observe(this, adapter::submitList);
    }
}