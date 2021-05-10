package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Character {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "class")
    private int mCharClass;

    @ColumnInfo(name = "save")
    private String mSaveSet;

    public Character(int id, String name, int charClass, String save) {
        mId = id;
        mName = name;
        mCharClass = charClass;
        mSaveSet = save;
    }
    public Character(){}

    public int getId(){
        return mId;
    }
    public void setId(int id){
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
    public String getSaveSet(){
        return mSaveSet;
    }
    public void setSaveSet(String save){
        mSaveSet = save;
    }
}
