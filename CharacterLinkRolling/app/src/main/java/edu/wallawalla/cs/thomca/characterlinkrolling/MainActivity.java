package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity
    implements MainFragment.InteractionListener {

    private int characterClassId = R.string.classNull;
    private String characterName;
    private String saveSettings;
    private final int SAVING_SETTINGS = 0;
    private final String KEY_CHARACTER_CLASS = "Character Class";
    private final String KEY_CHARACTER_NAME = "Character Name";
    private final String KEY_SAVE_STATE = "Save State";
    private int diceSides = 10;
    private int diceCount = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Begin a new FragmentTransaction for adding a HelloFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainFragment fragment = (MainFragment) fragmentManager.findFragmentById(R.id.main_fragment_container);

        // Add the MainFragment to the fragment container in the activity layout
        if(fragment == null){
            fragment = new MainFragment();
            fragmentTransaction.add(R.id.main_fragment_container, fragment);
            fragmentTransaction.commit();
        }
        // Replace MainFragment if state saved when going from portrait to landscape
        if (savedInstanceState != null && savedInstanceState.getInt(KEY_CHARACTER_CLASS) != 0) {
            characterClassId = savedInstanceState.getInt(KEY_CHARACTER_CLASS);
            fragment = fragment.newInstance(savedInstanceState.getInt(KEY_CHARACTER_CLASS));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, fragment)
                    .commit();
        }


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    public void onCharacterInteraction(int classId){
        characterClassId = classId;
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
        if (item.getItemId() == R.id.characterSettingsInMenu) {
            // character settings selected
            Intent intent = new Intent(this, CharacterSettings.class);
            intent.putExtra(CharacterSettings.CHARACTER_NAME, characterName);
            intent.putExtra(CharacterSettings.CHARACTER_CLASS, characterClassId);
            intent.putExtra(CharacterSettings.CHARACTER_SAVE_SETTINGS, saveSettings);
            startActivityForResult(intent, SAVING_SETTINGS);
            return true;
        }
        else if (item.getItemId() == R.id.rollDiceInMenu) {
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

        return super.onOptionsItemSelected(item);
    }

}