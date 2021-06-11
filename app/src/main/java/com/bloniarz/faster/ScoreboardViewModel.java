package com.bloniarz.faster;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bloniarz.faster.database.Score;
import com.bloniarz.faster.database.FasterRepository;

import java.util.List;

public class ScoreboardViewModel extends AndroidViewModel {

    private final FasterRepository repository;
    private final LiveData<List<Score>> topScores;

    public ScoreboardViewModel(@NonNull Application application) {
        super(application);
        repository = new FasterRepository(application);
        topScores = repository.getTopScores();
    }

    LiveData<List<Score>> getTopScores(){
        return topScores;
    }

    public void insert(Score score){
        repository.insertScore(score);
    }

    public void clear(){
        repository.clearScore();
    }

    public void deleteBottom(){
        repository.deleteBottomScores();
    }
}
