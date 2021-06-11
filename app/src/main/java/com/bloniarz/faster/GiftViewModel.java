package com.bloniarz.faster;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bloniarz.faster.database.FasterRepository;
import com.bloniarz.faster.database.Gift;
import com.bloniarz.faster.database.Score;

import java.util.List;

public class GiftViewModel extends AndroidViewModel {
    private final FasterRepository repository;

    public GiftViewModel(@NonNull Application application) {
        super(application);
        repository = new FasterRepository(application);
    }

    public LiveData<List<Gift>> getGifts(){
        return repository.getGifts();
    }

    public void insertGift(Gift gift){
        repository.insertGift(gift);
    }

    public void deleteGift(Gift gift){
        repository.deleteGift(gift);
    }
}
