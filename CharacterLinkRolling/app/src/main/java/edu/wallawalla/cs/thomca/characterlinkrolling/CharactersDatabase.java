package edu.wallawalla.cs.thomca.characterlinkrolling;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Character.class, Dice.class}, version = 1)
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
    public abstract DiceDao diceDao();

    private void addStarterData() { // Add a few subjects and questions if database is empty
        if (characterDao().getCharacters().size() == 0) {

            // Execute code on a background thread
            runInTransaction(new Runnable() {
                @Override
                public void run() {
                    Character character = new Character(0, "Steven West", R.string.classFighter, "FF");
                    Dice dice = new Dice(0, 6, 2, 3, R.string.classNull,0);
                    diceDao().insertDice(dice);
                    characterDao().insertCharacter(character);

                    character = new Character(1, "Lena Verin", R.string.classCleric, "FF");
                    dice = new Dice(1, 20, 1, 5, R.string.classCleric,-1);
                    diceDao().insertDice(dice);
                    characterDao().insertCharacter(character);
                }
            });
        }
    }
}
