package edu.wallawalla.cs.thomca.characterlinkrolling;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Random;

public class MainFragment extends Fragment implements View.OnClickListener {

    private int characterClassId = R.string.classNull;
    private String characterName;
    private String saveSettings;
    private final int SAVING_SETTINGS = 0;
    private final static String KEY_CHARACTER_CLASS = "Character Class";
    private final String KEY_CHARACTER_NAME = "Character Name";
    private final String KEY_SAVE_STATE = "Save State";
    private int diceSides = 10;
    private int diceCount = 5;
    private MainActivity mHost;
    private ViewGroup mRoot;
    private InteractionListener mListener;

    // activity listener
    public interface InteractionListener {
        void onCharacterInteraction(int classId, String charName, String saveSet);
        void updateDiceVals(int diceN, int diceS);
    }

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(int classId) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_CHARACTER_CLASS, classId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mHost = (MainActivity) context;
            mListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainActivity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHost = null;
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Restore state
        if (savedInstanceState != null) {
            characterClassId = savedInstanceState.getInt(KEY_CHARACTER_CLASS);
            characterName = savedInstanceState.getString(KEY_CHARACTER_NAME);
            saveSettings = savedInstanceState.getString(KEY_SAVE_STATE);
        }
        if (getArguments() != null) {
            characterClassId = getArguments().getInt(KEY_CHARACTER_CLASS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mRoot = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        setClassImage();
        // copied from main activity
        SeekBar diceCountSeekBar = mRoot.findViewById(R.id.diceCountBar);
        SeekBar diceSidesSeekBar = mRoot.findViewById(R.id.diceSidesBar);
        Button characterButton = mRoot.findViewById(R.id.setCharacterButton);
        characterButton.setTag(1);
        Button rollButton = mRoot.findViewById(R.id.rollDiceButton);
        rollButton.setTag(2);

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
                diceCount = seekBar.getProgress();
                String item = String.valueOf(diceCount);
                Toast.makeText(mHost, item, Toast.LENGTH_SHORT).show();
                sendDiceVals();
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
                Toast.makeText(mHost, item, Toast.LENGTH_SHORT).show();
                sendDiceVals();
            }
        });

        characterButton.setOnClickListener(this);
        rollButton.setOnClickListener(this);

        return mRoot;
    }
    private void setClassImage(){
        ImageView characterImage = mRoot.findViewById(R.id.classImage);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == android.app.Activity.RESULT_OK && requestCode == SAVING_SETTINGS) {
            characterClassId = data.getIntExtra(CharacterSettings.CHARACTER_CLASS, R.string.classNull);
            characterName = data.getStringExtra(CharacterSettings.CHARACTER_NAME);
            saveSettings = data.getStringExtra(CharacterSettings.CHARACTER_SAVE_SETTINGS);
        }
        setClassImage();
        sendCharacterClassId();
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
        Toast.makeText(mHost, item, Toast.LENGTH_SHORT).show();
    }

    // character settings button
    public void onCharacterSettingsClick(View view) {
        Intent intent = new Intent(view.getContext(), CharacterSettings.class);
        intent.putExtra(CharacterSettings.CHARACTER_NAME, characterName);
        intent.putExtra(CharacterSettings.CHARACTER_CLASS, characterClassId);
        intent.putExtra(CharacterSettings.CHARACTER_SAVE_SETTINGS, saveSettings);
        startActivityForResult(intent, SAVING_SETTINGS);
    }

    @Override
    public void onClick(View view) {
        // characterButton = 1, rollButton = 2
        if (view.getTag() == (Object) 1){
            onCharacterSettingsClick(view);
        }
        if (view.getTag() == (Object) 2){
            onRollDiceClick(view);
        }
    }

    // communicating with main, save class
    public void sendCharacterClassId() {
        mListener.onCharacterInteraction(characterClassId, characterName, saveSettings);
    }

    public void sendDiceVals(){
        mListener.updateDiceVals(diceCount,diceSides);
    }

    // gets settings from MainActivity
    public void setCharacterInfo(int classId, String charName, String saveSet){
        characterClassId = classId;
        characterName = charName;
        saveSettings = saveSet;
    }

}