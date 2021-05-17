package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = Action.class,
                parentColumns = "id",
                childColumns = "action_id",
                onDelete = CASCADE
        ),
        @ForeignKey(
                entity = Character.class,
                parentColumns = "id",
                childColumns = "character_id",
                onDelete = CASCADE
        )
}, primaryKeys = {"action_id","character_id"})public class LinksBase {
    @ColumnInfo(name = "action_id")
    private long actId;

    @ColumnInfo(name = "character_id")
    private long charId;

    @ColumnInfo(name = "actClass")
    private int actClass;

    public LinksBase(long actionId, long characterId, int actionClass) {
        actId = actionId;
        charId = characterId;
        actClass = actionClass;
    }
    public LinksBase(){}

    public long getActId(){
        return actId;
    }
    public void setActId(long id){
        actId = id;
    }
    public long getCharId(){
        return charId;
    }
    public void setCharId(long id){
        charId = id;
    }
    public int getActClass(){
        return actClass;
    }
    public void setActClass(int newClass){
        actClass = newClass;
    }
}
