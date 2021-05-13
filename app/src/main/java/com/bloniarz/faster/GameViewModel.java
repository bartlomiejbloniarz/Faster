package com.bloniarz.faster;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.bloniarz.faster.database.Score;
import com.bloniarz.faster.database.ScoreRepository;

public class GameViewModel extends AndroidViewModel {
    private final ScoreRepository repository;

    public GameViewModel(@NonNull Application application) {
        super(application);
        repository = new ScoreRepository(application);
    }

    public void insert(Score score){
        repository.insert(score);
    }
}
