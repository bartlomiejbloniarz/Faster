package com.bloniarz.faster.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FasterRepository {
    private FasterDao fasterDao;
    private LiveData<List<Score>> topScores;
    private LiveData<List<Gift>> gifts;

    public FasterRepository(Application application) {
        FasterDatabase database = FasterDatabase.getInstance(application);
        fasterDao = database.scoreDao();
        topScores = fasterDao.loadTopScores();
        gifts = fasterDao.getGifts();
    }

    public LiveData<List<Score>> getTopScores() {
        return topScores;
    }
    public LiveData<List<Gift>> getGifts() {
        return gifts;
    }

    public void insertScore(Score score) {
        FasterDatabase.databaseWriteExecutor.execute(() -> {
            fasterDao.insertAllScores(score);
        });
    }

    public void deleteBottomScores(){
        FasterDatabase.databaseWriteExecutor.execute(() -> {
            fasterDao.deleteBottomScores();
        });
    }

    public void clearScore(){
        FasterDatabase.databaseWriteExecutor.execute(() -> {
            fasterDao.clearScore();
        });
    }

    public void insertGift(Gift gift){
        FasterDatabase.databaseWriteExecutor.execute(() -> {
            fasterDao.insertGift(gift);
        });
    }

    public boolean insertGiftOnCurrentThread(Gift gift){
        return fasterDao.insertGift(gift) != -1;
    }

    public void deleteGift(Gift gift){
        FasterDatabase.databaseWriteExecutor.execute(() -> {
            fasterDao.deleteGift(gift);
        });
    }

}