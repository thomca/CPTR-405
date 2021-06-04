package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Character {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "class")
    private int mCharClass;

    @ColumnInfo(name = "save")
    private boolean mSaveSet;

    public Character(String name, int charClass, boolean save) {
        mName = name;
        mCharClass = charClass;
        mSaveSet = save;
    }
    public Character(){}

    public long getId(){
        return mId;
    }
    public void setId(long id){
        mId = id;
    }
    public String getName(){
        return mName;
    }
    public void setName(String name){
        mName = name;
    }
    public int getCharClass(){
        return mCharClass;
    }
    public void setCharClass(int charClass){
        mCharClass = charClass;
    }
    public boolean getSaveSet(){
        return mSaveSet;
    }
    public void setSaveSet(boolean save){
        mSaveSet = save;
    }
}
