package com.bloniarz.faster.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Score.class, Gift.class}, version = 14)
@TypeConverters({Converters.class})
public abstract class FasterDatabase extends RoomDatabase {
    public abstract FasterDao scoreDao();

    private static volatile FasterDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static FasterDatabase getInstance(final Context context){
        if (INSTANCE == null){
            synchronized (FasterDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FasterDatabase.class, "score_database").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
