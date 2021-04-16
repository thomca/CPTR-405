package edu.wallawalla.cs.thomca.characterlinkrolling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class CharacterSettings extends AppCompatActivity {

    public static final String CHARACTER_NAME = "John Smith";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_settings);

        // Get the character ID from MainActivity
        // Intent intent = getIntent();
        // int characterId = intent.getIntExtra(CHARACTER_NAME, R.string.classNull);


        Spinner characterClassSpinner = findViewById(R.id.classSpinner);
        SwitchCompat shareBetweenClassSwitch = findViewById(R.id.sharingStatusClassSwitch);
        SwitchCompat saveDiceWCharacterSwitch = findViewById(R.id.saveDiceToCharacterSwitch);

        //setting up spinner for character classes
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.characterClasses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        characterClassSpinner.setAdapter(adapter);
        characterClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                Toast.makeText(CharacterSettings.this, item, Toast.LENGTH_SHORT).show();
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
                    int i = 1;
                } else {
                    // off
                    int i = 0;
                }
            }
        });
        //switch to tag dice to this character
        saveDiceWCharacterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // on
                    int i = 1;
                } else {
                    // off
                    int i = 0;
                }
            }
        });
    }

}