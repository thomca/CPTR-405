package edu.wallawalla.cs.thomca.characterlinkrolling;

import java.util.List;

public class CharacterDiceDirectory {

    static private CharacterDiceDirectory mDirectory;
    private List<Dice> mDice;
    private List<Character> mCharacters;
    static private int characterId = 0;
    static private int diceId = 0;

    public enum SubjectSortOrder { ALPHABETIC, ID };

    public static CharacterDiceDirectory getInstance() {
        if (mDirectory == null) {
            mDirectory = new CharacterDiceDirectory();
        }
        return mDirectory;
    }
    private CharacterDiceDirectory(){
        //load up existing characters
        //load up existing dice
    }
    public void addCharacter(Character character) {
        mCharacters.add(character);
    }
    public Character getCharacter(int characterId) {
        for (Character characters: mCharacters) {
            if (characters.getId() == characterId) {
                return characters;
            }
        }
        return null;
    }
    public void addDice(Dice dice){
        mDice.add(dice);
    }
    public Dice getDiceFromId(int characterId){
        for (Dice dice: mDice) {
            if (dice.getId() == characterId) {
                return dice;
            }
        }
        return null;
    }
    public List<Dice> getDice(int charClass, int characterId){
        List<Dice> charactersDice = null;
        // add dice based on character

        // add dice based on class

        return charactersDice;
    }

}
