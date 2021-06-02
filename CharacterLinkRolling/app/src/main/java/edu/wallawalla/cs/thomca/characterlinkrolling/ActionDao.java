package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ActionDao {
    @Query("SELECT * FROM `Action` WHERE id = :id")
    public Action getDice(long id);

    @Query("SELECT * FROM `Action` WHERE sides = :side")
    public Action getDiceBySides(int side);

    @Query("SELECT * FROM `Action` WHERE modifier = :val")
    public Action getDiceByMod(int val);

    @Query("SELECT * FROM `Action` WHERE modifier = :name")
    public Action getActionByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertAction(Action die);

    @Update
    public void updateDice(Action die);

    @Delete
    public void deleteDice(Action die);
}
