package com.bloniarz.faster.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FasterDao {
    int top = 10;

    @Query("SELECT * FROM score")
    List<Score> getAllScores();

    @Query("SELECT * FROM score ORDER BY score DESC LIMIT " + top)
    LiveData<List<Score>> loadTopScores();

    @Insert
    void insertAllScores(Score... scores);

    @Delete
    void deleteScore(Score score);

    @Query("DELETE FROM score WHERE score.date IN (SELECT date FROM score ORDER BY score ASC LIMIT MAX(0,(SELECT count(*) FROM score) - " + top + "))")
    void deleteBottomScores();

    @Query("DELETE FROM score")
    void clearScore();

    @Query("SELECT * FROM gift ORDER BY name ASC")
    LiveData<List<Gift>> getGifts();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertGift(Gift gift);

    @Delete
    void deleteGift(Gift gift);

}
