package com.bloniarz.faster;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bloniarz.faster.database.FasterRepository;
import com.bloniarz.faster.database.Gift;

import java.util.List;

public class GiftsViewModel extends AndroidViewModel {
    private final FasterRepository repository;

    public GiftsViewModel(@NonNull Application application) {
        super(application);
        repository = new FasterRepository(application);
    }

    public LiveData<List<Gift>> getGifts(){
        return repository.getGifts();
    }

    public boolean insertGift(Gift gift){
        return repository.insertGiftOnCurrentThread(gift);
    }

    public void deleteGift(Gift gift){
        repository.deleteGift(gift);
    }
}
