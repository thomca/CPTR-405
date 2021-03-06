package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Action {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @NonNull
    @ColumnInfo(name = "sides")
    private int mNumSides;

    @NonNull
    @ColumnInfo(name = "count")
    private int mDiceCount;

    @ColumnInfo(name = "modifier")
    private int mModifier;

    @NonNull
    @ColumnInfo(name = "name")
    private String mActionName;

    public Action() {}

    public Action(int sides, int count, int mod, String name) {
        mModifier = mod;
        mNumSides = sides;
        mDiceCount = count;
        mActionName = name;
    }

    public long getId(){
        return mId;
    }
    public void setId(long id){
        mId = id;
    }
    public int getModifier(){
        return mModifier;
    }
    public void setModifier(int mod){
        mModifier = mod;
    }
    public int getNumSides(){
        return mNumSides;
    }
    public void setNumSides(int sides){
        mNumSides = sides;
    }
    public int getDiceCount(){
        return mDiceCount;
    }
    public void setDiceCount(int count){
        mDiceCount = count;
    }
    public String getActionName(){
        return mActionName;
    }
    public void setActionName(String name){
        mActionName = name;
    }
}
