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

public class MainActivity extends AppCompatActivity {

    private int characterClassId;
    private final int SAVING_SETTINGS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar diceCountSeekBar = findViewById(R.id.diceCountBar);
        SeekBar diceSidesSeekBar = findViewById(R.id.diceSidesBar);
        characterClassId = R.string.classNull;

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
                int itemInt = seekBar.getProgress();
                String item = String.valueOf(itemInt);
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
                int itemInt = seekBar.getProgress();
                String item = String.valueOf(itemInt);
                Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // character settings button

    public void onCharacterSettingsClick(View view) {
        Intent intent = new Intent(this, CharacterSettings.class);
        //intent.putExtra(CharacterSettings.CHARACTER_NAME, characterClassId);
        //startActivityForResult(intent, SAVING_SETTINGS);
        startActivity(intent);
    }


}