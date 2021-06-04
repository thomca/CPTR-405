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
    Character getCharacter(long id);

    @Query("SELECT * FROM Character WHERE name = :name")
    Character getCharacterByName(String name);

    @Query("SELECT * FROM Character ORDER BY name")
    List<Character> getCharacters();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCharacter(Character character);

    @Update
    void updateCharacter(Character character);

    @Delete
    void deleteCharacter(Character character);
}
