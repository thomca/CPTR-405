package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.Random;

public class MainActivity extends AppCompatActivity
    implements MainFragment.InteractionListener, SelectCharacterPopUp.PopInteractionListener{

    private int characterClassId = R.string.classNull;
    private String characterName;
    private String saveSettings;
    private final int SAVING_SETTINGS = 0;
    private final String KEY_CHARACTER_CLASS = "Character Class";
    private final String KEY_CHARACTER_NAME = "Character Name";
    private final String KEY_SAVE_STATE = "Save State";
    private final String KEY_DICE_SIDES = "Number of sides";
    private final String KEY_DICE_COUNT = "Number of dice";
    private final String KEY_MODIFIER = "Modifier";
    private int diceSides = 10;
    private int diceCount = 5;
    private int modifier = 0;
    private CharactersDatabase mCharactersDatabase;
    MainFragment mainFragment;

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
        if (savedInstanceState != null && savedInstanceState.getInt(KEY_CHARACTER_CLASS) != 0) {
            characterClassId = savedInstanceState.getInt(KEY_CHARACTER_CLASS);
            diceCount = savedInstanceState.getInt(KEY_DICE_COUNT);
            diceSides = savedInstanceState.getInt(KEY_DICE_SIDES);
            modifier = savedInstanceState.getInt(KEY_MODIFIER);
            mainFragment = mainFragment.newInstance(characterClassId, diceCount, diceSides, modifier);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, mainFragment)
                    .commit();
        }

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    public void onCharacterInteraction(int classId, String charName, String saveSet){
        characterClassId = classId;
        characterName = charName;
        saveSettings = saveSet;
    }

    public void  updateDiceVals(int diceN, int diceS){
        diceSides = diceS;
        diceCount = diceN;
    }

    public void openCharacter(Character character){
        characterClassId = character.getCharClass();
        characterName = character.getName();
        saveSettings = character.getSaveSet();
    }

    // rolling the dice
    public void onRollDiceClick(View view){ rollTheDice(); }

    // roll function
    public void rollTheDice(){
        Random randomNumGenerator = new Random();
        int currentRoll;
        int finalRoll = 0;
        for(int i = 0; i < diceCount; i++){
            currentRoll = randomNumGenerator.nextInt(diceSides) + 1;
            finalRoll = finalRoll + currentRoll;
        }
        String item = String.valueOf(finalRoll);
        Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
    }

    // on save
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CHARACTER_CLASS,characterClassId);
        outState.putString(KEY_CHARACTER_NAME,characterName);
        outState.putString(KEY_SAVE_STATE,saveSettings);
        outState.putInt(KEY_DICE_COUNT, diceCount);
        outState.putInt(KEY_DICE_SIDES, diceSides);
        outState.putInt(KEY_MODIFIER, modifier);
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
        if (item.getItemId() == R.id.rollDiceInMenu) {
            // roll selected
            rollTheDice();
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
            characterClassId = data.getIntExtra(CharacterSettings.CHARACTER_CLASS, R.string.classNull);
            characterName = data.getStringExtra(CharacterSettings.CHARACTER_NAME);
            saveSettings = data.getStringExtra(CharacterSettings.CHARACTER_SAVE_SETTINGS);
        }
        //update main fragment
        mainFragment = mainFragment.newInstance(characterClassId, diceCount, diceSides, modifier);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, mainFragment)
                .commit();
    }


    // character settings button
    public void saveCharacterSettings(View view) {
        Intent intent = new Intent(view.getContext(), CharacterSettings.class);
        intent.putExtra(CharacterSettings.CHARACTER_NAME, characterName);
        intent.putExtra(CharacterSettings.CHARACTER_CLASS, characterClassId);
        intent.putExtra(CharacterSettings.CHARACTER_SAVE_SETTINGS, saveSettings);
        startActivityForResult(intent, SAVING_SETTINGS);
    }



}