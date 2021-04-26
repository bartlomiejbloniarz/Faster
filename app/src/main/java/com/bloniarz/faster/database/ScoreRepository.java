package com.bloniarz.faster.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ScoreRepository {
    private ScoreDao scoreDao;
    private LiveData<List<Score>> topScores;

    public ScoreRepository(Application application) {
        ScoreDatabase database = ScoreDatabase.getInstance(application);
        scoreDao = database.scoreDao();
        topScores = scoreDao.loadTop();
    }

    public LiveData<List<Score>> getTopScores() {
        return topScores;
    }

    public void insert(Score score) {
        ScoreDatabase.databaseWriteExecutor.execute(() -> {
            scoreDao.insertAll(score);
        });
    }

    public void deleteBottom(){
        ScoreDatabase.databaseWriteExecutor.execute(() -> {
            scoreDao.deleteBottom();
        });
    }

    public void clear(){
        ScoreDatabase.databaseWriteExecutor.execute(() -> {
            scoreDao.clear();
        });
    }
}