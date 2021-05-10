package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DiceDao {
    @Query("SELECT * FROM Dice WHERE id = :id")
    public Dice getDice(int id);

    @Query("SELECT * FROM Dice WHERE sides = :side")
    public Dice getDiceBySides(int side);

    @Query("SELECT * FROM Dice WHERE modifier = :val")
    public Dice getDiceByMod(int val);

    @Query("SELECT * FROM Dice WHERE character = :ch")
    public Dice getDiceByCharacter(int ch);

    @Query("SELECT * FROM Dice WHERE classes = :val")
    public Dice getDiceByClass(int val);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertDice(Dice die);

    @Update
    public void updateDice(Dice die);

    @Delete
    public void deleteDice(Dice die);
}
