package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface CharacterDao {
    @Query("SELECT * FROM Character WHERE id = :id")
    public Character getCharacter(long id);

    @Query("SELECT * FROM Character WHERE name = :name")
    public Character getCharacterByName(String name);

    @Query("SELECT * FROM Character WHERE class= :charClass")
    public Character getCharacterByClass(String charClass);

    @Query("SELECT * FROM Character ORDER BY name")
    public List<Character> getCharacters();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertCharacter(Character character);

    @Update
    public void updateCharacter(Character character);

    @Delete
    public void deleteCharacter(Character character);
}
