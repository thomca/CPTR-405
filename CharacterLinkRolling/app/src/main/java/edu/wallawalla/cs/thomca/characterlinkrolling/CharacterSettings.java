package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CharacterSettings extends AppCompatActivity {

    public static final String CHARACTER_NAME = "Character Name";
    public static final String CHARACTER_CLASS = "Character Class";
    public static final String CHARACTER_SAVE_SETTINGS = "Save Settings";
    public static boolean saveBasedName;
    public static boolean saveBasedClass;
    public static String saveState;
    public static String characterName;
    public static int characterClass;
    private EditText characterNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_settings);

        // Get the character info from MainActivity
        Intent intent = getIntent();
        characterName = intent.getStringExtra(CHARACTER_NAME);
        saveState = intent.getStringExtra(CHARACTER_SAVE_SETTINGS);
        characterClass = intent.getIntExtra(CHARACTER_CLASS, R.string.classNull);

        Spinner characterClassSpinner = findViewById(R.id.classSpinner);
        SwitchCompat shareBetweenClassSwitch = findViewById(R.id.sharingStatusClassSwitch);
        SwitchCompat saveDiceWCharacterSwitch = findViewById(R.id.saveDiceToCharacterSwitch);
        characterNameEditText = findViewById(R.id.characterNameEditText);

        //preset switches
        if(saveState != null){
            if(saveState.charAt(0) == 'T'){
                saveBasedClass = true;
            }
            else {
                saveBasedClass = false;
            }
            if(saveState.charAt(1) == 'T'){
                saveBasedName = true;
            }
            else {
                saveBasedName = false;
            }
            shareBetweenClassSwitch.setChecked(saveBasedClass);
            saveDiceWCharacterSwitch.setChecked(saveBasedName);
        }
        // set character name
        if(characterName != null){
            characterNameEditText.setText(characterName);
        }


        //setting up spinner for character classes
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.characterClasses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        characterClassSpinner.setAdapter(adapter);
        //preset spinner
        if (characterClass != R.string.classNull){
            characterClassSpinner.setSelection(adapter.getPosition(getResources().getString(characterClass)));
        }
        characterClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                Toast.makeText(CharacterSettings.this, item, Toast.LENGTH_SHORT).show();
                // store for pass back
                if(item.equals("Fighter")){
                    characterClass = R.string.classFighter;
                }
                if(item.equals("Wizard")){
                    characterClass = R.string.classWizard;
                }
                if(item.equals("Cleric")){
                    characterClass = R.string.classCleric;
                }
                if(item.equals("Ranger")){
                    characterClass = R.string.classRanger;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //switch for sharing dice between characters of the same class
        shareBetweenClassSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // on
                    saveBasedClass = true;
                } else {
                    // off
                    saveBasedClass = false;
                }
            }
        });
        //switch to tag dice to this character
        saveDiceWCharacterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // on
                    saveBasedName = true;
                } else {
                    // off
                    saveBasedName = false;
                }
            }
        });
        //add Text Watcher to store character name

    }

    //click finish
    public void onFinishButtonSelected(android.view.View v){
        //set up bool return
        if(saveBasedClass){
            saveState = "T";
        }
        else {
            saveState = "F";
        }
        if(saveBasedName){
            saveState = saveState + "T";
        }
        else{
            saveState = saveState + "F";
        }

        characterName = characterNameEditText.getText().toString();

        Intent intent = new Intent();
        intent.putExtra(CHARACTER_SAVE_SETTINGS,saveState);
        intent.putExtra(CHARACTER_NAME,characterName);
        intent.putExtra(CHARACTER_CLASS,characterClass);
        setResult(RESULT_OK, intent);
        finish();
    }

}