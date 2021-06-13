package com.bloniarz.faster.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Gift implements Serializable {
    public Gift(int id, String name, @NonNull Integer thumbnailId){
        this.id = id;
        this.name = name;
        this.thumbnailId = thumbnailId;
    }

    @PrimaryKey
    public int id;

    @ColumnInfo()
    public String name;

    @NonNull
    @ColumnInfo()
    public Integer thumbnailId;
}
