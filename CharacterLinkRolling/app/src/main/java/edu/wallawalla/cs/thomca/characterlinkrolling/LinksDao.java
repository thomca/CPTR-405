package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LinksDao {
    @Query("SELECT * FROM LinksBase WHERE action_id = :id")
    public LinksBase getActId(long id);

    @Query("SELECT * FROM LinksBase WHERE character_id = :id")
    public LinksBase getCharId(long id);

    @Query("SELECT * FROM LinksBase WHERE actClass = :id")
    public LinksBase getActClass(int id);

    @Query("SELECT * FROM LinksBase ORDER BY action_id")
    public List<LinksBase> getLinksBase();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertLinks(LinksBase link);

    @Update
    public void updateLinks(LinksBase link);

    @Delete
    public void deleteLinks(LinksBase link);
}
