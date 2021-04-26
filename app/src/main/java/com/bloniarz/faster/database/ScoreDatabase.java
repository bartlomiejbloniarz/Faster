package com.bloniarz.faster.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Score.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class ScoreDatabase extends RoomDatabase {
    public abstract ScoreDao scoreDao();

    private static volatile ScoreDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ScoreDatabase getInstance(final Context context){
        if (INSTANCE == null){
            synchronized (ScoreDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ScoreDatabase.class, "score_database").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
