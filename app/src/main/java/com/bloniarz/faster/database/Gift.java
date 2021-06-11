package com.bloniarz.faster.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Gift implements Serializable {
    public Gift(int id, String name/*, boolean firstOwner*/){
        this.id = id;
        this.name = name;
        //this.firstOwner = firstOwner;
    }

    @PrimaryKey
    public int id;

    @ColumnInfo()
    public String name;

    //@ColumnInfo()
    //public boolean firstOwner;
}
