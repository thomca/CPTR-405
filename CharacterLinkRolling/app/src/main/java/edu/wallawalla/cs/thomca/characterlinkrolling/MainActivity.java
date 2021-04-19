package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

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

        // Restore state
        if (savedInstanceState != null) {
                characterClassId = savedInstanceState.getInt(KEY_CHARACTER_CLASS);
                characterName = savedInstanceState.getString(KEY_CHARACTER_NAME);
                saveSettings = savedInstanceState.getString(KEY_SAVE_STATE);
        }
        setClassImage();

        SeekBar diceCountSeekBar = findViewById(R.id.diceCountBar);
        SeekBar diceSidesSeekBar = findViewById(R.id.diceSidesBar);

        //tracking seek bar for dice count
        diceCountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // progress ranges from 0 to max
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                diceCount= seekBar.getProgress();
                String item = String.valueOf(diceCount);
                Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });

        //tracking seek bar for dice sides
        diceSidesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // progress ranges from 0 to max
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                diceSides = seekBar.getProgress();
                String item = String.valueOf(diceSides);
                Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // rolling the dice
    public void onRollDiceClick(View view){
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

    // character settings button
    public void onCharacterSettingsClick(View view) {
        Intent intent = new Intent(this, CharacterSettings.class);
        intent.putExtra(CharacterSettings.CHARACTER_NAME, characterName);
        intent.putExtra(CharacterSettings.CHARACTER_CLASS, characterClassId);
        intent.putExtra(CharacterSettings.CHARACTER_SAVE_SETTINGS, saveSettings);
        startActivityForResult(intent, SAVING_SETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SAVING_SETTINGS) {
            characterClassId = data.getIntExtra(CharacterSettings.CHARACTER_CLASS, R.string.classNull);
            characterName = data.getStringExtra(CharacterSettings.CHARACTER_NAME);
            saveSettings = data.getStringExtra(CharacterSettings.CHARACTER_SAVE_SETTINGS);
        }
        setClassImage();
    }

    public void setClassImage(){
        ImageView characterImage = findViewById(R.id.classImage);
        if (characterClassId == R.string.classCleric){
            characterImage.setImageResource(R.drawable.clericgraphic);}
        else if (characterClassId == R.string.classFighter){
            characterImage.setImageResource(R.drawable.fightergraphic);}
        else if (characterClassId == R.string.classRanger){
            characterImage.setImageResource(R.drawable.rangergraphic);}
        else if (characterClassId == R.string.classWizard){
            characterImage.setImageResource(R.drawable.wizardgraphic);}
        else{
            characterImage.setImageResource(R.drawable.diceicon);
        }
    }

    //on save
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CHARACTER_CLASS,characterClassId);
        outState.putString(KEY_CHARACTER_NAME,characterName);
        outState.putString(KEY_SAVE_STATE,saveSettings);
    }
}