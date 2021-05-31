package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CharacterSettings extends AppCompatActivity {

    public static final String ACTIVE_CHARACTER = "Character Active";
    public static final String CHARACTER_ID = "Character ID";
    public static boolean saveBasedName;
    public static boolean saveBasedClass;
    public static String saveState;
    public static String characterName;
    public static int characterClass = R.string.classNull;
    public static boolean activeCharacter = false;
    public static long characterID;
    private Character character;
    private EditText characterNameEditText;
    private CharactersDatabase mCharactersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_settings);
        mCharactersDatabase = CharactersDatabase.getInstance(this);

        // Get the character info from MainActivity
        Intent intent = getIntent();
        activeCharacter = intent.getBooleanExtra(ACTIVE_CHARACTER,false);
        if(!activeCharacter){
            Button updateButton = (Button) findViewById(R.id.finishSettings);
            updateButton.setVisibility(View.GONE);
        }
        else{
            characterID = intent.getLongExtra(CHARACTER_ID, characterID);
            character = mCharactersDatabase.characterDao().getCharacter(characterID);

            characterName = character.getName();
            saveState = character.getSaveSet();
            characterClass = character.getCharClass();
        }



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

    //click Update
    public void onFinishButtonSelectedUpdate(android.view.View v){
        // Update character info
        UpdateForSave();
        mCharactersDatabase.characterDao().updateCharacter(character);
        returnResults(v);
    }

    //click New Character
    public void onFinishButtonSelectedNew(android.view.View v){
        character = new Character(characterName,characterClass,saveState);
        activeCharacter = true;
        UpdateForSave();
        long newID = mCharactersDatabase.characterDao().insertCharacter(character);
        character.setId(newID);
        characterID = newID;
        returnResults(v);
    }

    public void UpdateForSave(){
        characterName = characterNameEditText.getText().toString();
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
        character.setSaveSet(saveState);
        character.setCharClass(characterClass);
        character.setName(characterName);
    }

    public void returnResults(android.view.View v){
        Intent intent = new Intent();
        intent.putExtra(ACTIVE_CHARACTER,activeCharacter);
        intent.putExtra(CHARACTER_ID,characterID);
        setResult(RESULT_OK, intent);
        finish();
    }

}