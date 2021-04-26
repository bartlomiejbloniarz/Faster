package com.bloniarz.faster.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Score {

    public Score(Date date, int score){
        this.date = date;
        this.score = score;
    }

    @PrimaryKey
    public Date date;

    @ColumnInfo()
    public int score;
}
