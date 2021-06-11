package com.bloniarz.faster;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.bloniarz.faster.database.Gift;
import com.bloniarz.faster.database.Score;
import com.bloniarz.faster.database.FasterRepository;

public class GameViewModel extends AndroidViewModel {
    private final FasterRepository repository;

    public GameViewModel(@NonNull Application application) {
        super(application);
        repository = new FasterRepository(application);
    }

    public void insertScore(Score score){
        repository.insertScore(score);
    }

    public void insertGift(Gift gift){
        repository.insertGift(gift);
    }
}
