package com.bloniarz.faster.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {
    int top = 10;

    @Query("SELECT * FROM score")
    List<Score> getAll();

    @Query("SELECT * FROM score ORDER BY score DESC LIMIT " + top)
    LiveData<List<Score>> loadTop();

    @Insert
    void insertAll(Score... scores);

    @Delete
    void delete(Score score);

    @Query("DELETE FROM score WHERE score.date IN (SELECT date FROM score ORDER BY score ASC LIMIT MAX(0,(SELECT count(*) FROM score) - " + top + "))")
    void deleteBottom();

    @Query("DELETE FROM score")
    void clear();

}
