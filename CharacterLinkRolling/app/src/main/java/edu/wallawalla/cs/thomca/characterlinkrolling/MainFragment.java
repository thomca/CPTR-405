package edu.wallawalla.cs.thomca.characterlinkrolling;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Modifier;


public class MainFragment extends Fragment implements View.OnClickListener {

    private int characterClassId = R.string.classNull;
    private final static String KEY_CHARACTER_CLASS = "Character Class";
    private final static String KEY_DICE_SIDES = "Number of sides";
    private final static String KEY_DICE_COUNT = "Number of dice";
    private final static String KEY_MODIFIER = "Modifier";
    private int mDiceSides = 10;
    private int mDiceCount = 5;
    private int mModifier = 0;
    private MainActivity mHost;
    private ViewGroup mRoot;
    private InteractionListener mListener;

    // activity listener
    public interface InteractionListener {
        void updateDiceVals(int diceN, int diceS);
        void rollTheDice();
        void saveCharacterSettings(View view);
        void updateModifier(int modifier);
    }

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(int classId, int diceCount, int diceSides, int modifier) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_CHARACTER_CLASS, classId);
        args.putInt(KEY_DICE_COUNT, diceCount);
        args.putInt(KEY_DICE_SIDES, diceSides);
        args.putInt(KEY_MODIFIER, modifier);
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
            mDiceSides = savedInstanceState.getInt(KEY_DICE_SIDES);
            mDiceCount = savedInstanceState.getInt(KEY_DICE_COUNT);
            mModifier = savedInstanceState.getInt(KEY_MODIFIER);
        }
        if (getArguments() != null) {
            characterClassId = getArguments().getInt(KEY_CHARACTER_CLASS);
            mDiceSides = getArguments().getInt(KEY_DICE_SIDES);
            mDiceCount = getArguments().getInt(KEY_DICE_COUNT);
            mModifier = getArguments().getInt(KEY_MODIFIER);
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
        diceCountSeekBar.setProgress(mDiceCount);
        SeekBar diceSidesSeekBar = mRoot.findViewById(R.id.diceSidesBar);
        diceSidesSeekBar.setProgress(mDiceSides);
        Button characterButton = mRoot.findViewById(R.id.setCharacterButton);
        characterButton.setTag(1);
        Button rollButton = mRoot.findViewById(R.id.rollDiceButton);
        rollButton.setTag(2);
        ImageView icon = mRoot.findViewById(R.id.classImage);
        icon.setTag(3);
        TextView modifierView = mRoot.findViewById(R.id.modifierField);
        modifierView.setTag(4);
        modifierView.setText(String.valueOf(mModifier));

        modifierView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    mModifier = Integer.parseInt(s.toString());
                }
                else {
                    mModifier = 0;
                }
                mListener.updateModifier(mModifier);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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
                mDiceCount = seekBar.getProgress();
                String item = String.valueOf(mDiceCount);
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
                mDiceSides = seekBar.getProgress();
                String item = String.valueOf(mDiceSides);
                Toast.makeText(mHost, item, Toast.LENGTH_SHORT).show();
                sendDiceVals();
            }
        });

        characterButton.setOnClickListener(this);
        rollButton.setOnClickListener(this);
        icon.setOnClickListener(this);

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

    public void onCharacterSettingsClick(View view) {
        mListener.saveCharacterSettings(view);
    }

    // animation of character icon
    public void animateIcon(View view){
        // run animation
        ImageView characterIcon = mRoot.findViewById(R.id.classImage);
        Animation characterAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.class_icon_animation);
        characterIcon.startAnimation(characterAnim);
    }

    @Override
    public void onClick(View view) {
        // characterButton = 1, rollButton = 2 icon = 3
        if (view.getTag() == (Object) 1){
            onCharacterSettingsClick(view);
        }
        if (view.getTag() == (Object) 2){
            mListener.rollTheDice();
        }
        if (view.getTag() == (Object) 3){
            animateIcon(view);
        }
    }

    public void sendDiceVals(){
        mListener.updateDiceVals(mDiceCount, mDiceSides);
    }


}