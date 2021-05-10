package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Dice {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "sides")
    private int mNumSides;

    @NonNull
    @ColumnInfo(name = "count")
    private int mDiceCount;

    @ColumnInfo(name = "modifier")
    private int mModifier;

    @ColumnInfo(name = "classes")
    private int mClasses;

    @ColumnInfo(name = "character")
    private int mCharacter;

    public Dice() {}

    public Dice(int id, int sides, int count, int mod, int classes,int characters) {
        mId = id;
        mModifier = mod;
        mNumSides = sides;
        mDiceCount = count;
        mClasses = classes;
        mCharacter = characters;
    }

    public int getId(){
        return mId;
    }
    public void setId(int id){
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
    public int getClasses(){
        return mClasses;
    }
    public void setClasses(int classes){
        mClasses = classes;
    }
    public int getCharacter(){
        return mCharacter;
    }
    public void setCharacter(int character){
        mCharacter = character;
    }
}
