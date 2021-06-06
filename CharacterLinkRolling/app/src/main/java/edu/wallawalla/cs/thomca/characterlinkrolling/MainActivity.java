package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.Random;

public class MainActivity extends AppCompatActivity
    implements MainFragment.InteractionListener, SelectCharacterPopUp.PopInteractionListener, SelectActionPopUp.PopDiceInteractionListener{

    private final String KEY_SAVE_STATE = "Save State";
    private final String KEY_ACTIVE_CHARACTER = "Character Active";
    private final String KEY_CHARACTER_ID = "character id";
    private final String KEY_DICE_SIDES = "Number of sides";
    private final String KEY_DICE_COUNT = "Number of dice";
    private final String KEY_MODIFIER = "Modifier";
    private int characterClassId = R.string.classNull;
    private boolean saveSettings = false;
    private final int SAVING_SETTINGS = 0;
    private int diceSides = 10;
    private int diceCount = 5;
    private int mModifier = 0;
    private CharactersDatabase mCharactersDatabase;
    private boolean activeCharacter = false;
    private long characterID = -1;
    private Character mCharacter;
    MainFragment mainFragment;

    // for action activity
    private final static String KEY_ACTIVE_ACTION = "Action Bool";
    private final static String KEY_ACTION = "Action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCharactersDatabase = CharactersDatabase.getInstance(getApplicationContext());

        // Begin a new FragmentTransaction for adding a HelloFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mainFragment = (MainFragment) fragmentManager.findFragmentById(R.id.main_fragment_container);

        // Add the MainFragment to the fragment container in the activity layout
        if(mainFragment == null){
            mainFragment = new MainFragment();
            fragmentTransaction.add(R.id.main_fragment_container, mainFragment);
            fragmentTransaction.commit();
        }

        // Replace MainFragment if state saved when going from portrait to landscape
        if (savedInstanceState != null) {
            activeCharacter = savedInstanceState.getBoolean(KEY_ACTIVE_CHARACTER);
            if(activeCharacter) {
                characterID = savedInstanceState.getLong(KEY_CHARACTER_ID);
                mCharacter = mCharactersDatabase.characterDao().getCharacter(characterID);
                characterClassId = mCharacter.getCharClass();
            }
            diceCount = savedInstanceState.getInt(KEY_DICE_COUNT);
            diceSides = savedInstanceState.getInt(KEY_DICE_SIDES);
            mModifier = savedInstanceState.getInt(KEY_MODIFIER);
            setUpMainFragmentDisplay();
        }

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    public void  updateDiceVals(int diceN, int diceS){
        diceSides = diceS;
        diceCount = diceN;
    }
    public void setUpMainFragmentDisplay(){
        mainFragment = mainFragment.newInstance(characterClassId, diceCount, diceSides, mModifier);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, mainFragment)
                .commit();
    }

    public void openCharacter(Character character){
        mCharacter = character;
        characterID = character.getId();
        activeCharacter = true;

        characterClassId = character.getCharClass();
        saveSettings = character.getSaveSet();
        setUpMainFragmentDisplay();
    }

    public void characterDeleted(long characterId){
        if(activeCharacter){
            if(characterId == mCharacter.getId()){
                activeCharacter = false;
                mCharacter = null;
                characterClassId = R.string.classNull;
                setUpMainFragmentDisplay();
            }
        }
    }

    public void openAction (Action action){
        diceSides = action.getNumSides();
        diceCount = action.getDiceCount();
        mModifier = action.getModifier();
        setUpMainFragmentDisplay();
    }

    public void saveAction(){
        //FIXME add save action, request name and store based on save settings
        FragmentManager manager = getSupportFragmentManager();
        EditOrSaveAction dialog = new EditOrSaveAction();
        Bundle args = new Bundle();
        args.putBoolean(KEY_ACTIVE_ACTION, false);
        dialog.setArguments(args);
        dialog.show(manager, "Save Action");
    }

    public void editAction(Action action){
        //FIXME save changes to action (See if args work)
        FragmentManager manager = getSupportFragmentManager();
        EditOrSaveAction dialog = new EditOrSaveAction();
        Bundle args = new Bundle();
        args.putBoolean(KEY_ACTIVE_ACTION, true);
        args.putLong(KEY_ACTION, action.getId());
        dialog.setArguments(args);
        dialog.show(manager, "Save Action");
    }

    public void updateModifier(int modifier){
        mModifier = modifier;
    }

    // roll function
    public void rollTheDice(){
        Random randomNumGenerator = new Random();
        int currentRoll;
        int finalRoll = 0;
        for(int i = 0; i < diceCount; i++){
            currentRoll = randomNumGenerator.nextInt(diceSides) + 1;
            finalRoll = finalRoll + currentRoll;
        }
        currentRoll = finalRoll + mModifier;
        String item = finalRoll + " + " + mModifier + " = " + currentRoll;
        Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
    }

    // on save
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SAVE_STATE,saveSettings);
        outState.putInt(KEY_DICE_COUNT, diceCount);
        outState.putInt(KEY_DICE_SIDES, diceSides);
        outState.putInt(KEY_MODIFIER, mModifier);
        outState.putBoolean(KEY_ACTIVE_CHARACTER, activeCharacter);
        outState.putLong(KEY_CHARACTER_ID, characterID);
    }

    // options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
    }

    // handling menu calls
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Determine which menu option was chosen
        if (item.getItemId() == R.id.selectDiceInMenu) {
            // select dice
            FragmentManager manager = getSupportFragmentManager();
            SelectActionPopUp dialog = new SelectActionPopUp();
            dialog.show(manager, "selectActionPopUp");
            return true;
        }
        else if (item.getItemId() == R.id.creditsScreen) {
            // Credits selected
            FragmentManager manager = getSupportFragmentManager();
            CreditsPopUp dialog = new CreditsPopUp();
            dialog.show(manager, "creditPopUpDialog");
            return true;
        }
        else if (item.getItemId() == R.id.loadCharacterInMenu){
            // Character selected
            FragmentManager manager = getSupportFragmentManager();
            SelectCharacterPopUp dialog = new SelectCharacterPopUp();
            dialog.show(manager, "selectCharacterPopUp");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == android.app.Activity.RESULT_OK && requestCode == SAVING_SETTINGS) {
            activeCharacter = data.getBooleanExtra(CharacterSettings.ACTIVE_CHARACTER,true);
            characterID = data.getLongExtra(CharacterSettings.CHARACTER_ID,-1);
            mCharacter = mCharactersDatabase.characterDao().getCharacter(characterID);

            characterClassId = mCharacter.getCharClass();
            saveSettings = mCharacter.getSaveSet();
        }
        //update main fragment
        setUpMainFragmentDisplay();
    }

    // character settings button
    public void saveCharacterSettings(View view) {
        Intent intent = new Intent(view.getContext(), CharacterSettings.class);
        intent.putExtra(CharacterSettings.ACTIVE_CHARACTER, activeCharacter);
        intent.putExtra(CharacterSettings.CHARACTER_ID, characterID);
        startActivityForResult(intent, SAVING_SETTINGS);
    }

    public Character getCharacter(){
        return mCharacter;
    }

}