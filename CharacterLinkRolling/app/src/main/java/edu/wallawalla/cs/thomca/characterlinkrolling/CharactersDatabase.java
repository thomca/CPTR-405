package edu.wallawalla.cs.thomca.characterlinkrolling;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Character.class, Action.class, LinksBase.class}, version = 1)
public abstract class CharactersDatabase extends RoomDatabase{

    private static final String DATABASE_NAME = "character.db";

    private static CharactersDatabase mCharactersDatabase;

    // Singleton
    public static CharactersDatabase getInstance(Context context) {
        if (mCharactersDatabase == null) {
            mCharactersDatabase = Room.databaseBuilder(context, CharactersDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
            mCharactersDatabase.addStarterData();
        }
        return mCharactersDatabase;
    }

    public abstract CharacterDao characterDao();
    public abstract ActionDao actionDao();
    public abstract LinksDao linksBase();

    private void addStarterData() { // Add a few subjects and questions if database is empty
        if (characterDao().getCharacters().size() == 0) {

            // Execute code on a background thread
            runInTransaction(new Runnable() {
                @Override
                public void run() {
                    Character character = new Character("Steven West", R.string.classFighter, "FF");
                    Action action = new Action(6, 2, 3);
                    long actionId = actionDao().insertAction(action);
                    long charId = characterDao().insertCharacter(character);
                    LinksBase link = new LinksBase(actionId, charId, R.string.classNull);
                    linksBase().insertLinks(link);

                    character = new Character("Lena Verin", R.string.classCleric, "FF");
                    action = new Action(20, 1, 5);
                    actionId = actionDao().insertAction(action);
                    charId = characterDao().insertCharacter(character);
                    link = new LinksBase(actionId, charId, R.string.classCleric);
                    linksBase().insertLinks(link);
                }
            });
        }
    }
}
