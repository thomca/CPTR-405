package edu.wallawalla.cs.thomca.characterlinkrolling;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class EditOrSaveAction extends DialogFragment {
    private final String KEY_DICE_SIDES = "Number of sides";
    private final String KEY_DICE_COUNT = "Number of dice";
    private final String KEY_MODIFIER = "Modifier";
    private final static String KEY_ACTIVE_ACTION = "Action Bool";
    private final static String KEY_ACTION = "Action";
    private CharactersDatabase mCharactersDatabase;
    private MainActivity mHost;
    private ViewGroup mRoot;
    private boolean saveSettings = false;
    private Character mCharacter;
    private int mDiceSides = 10;
    private int mDiceCount = 5;
    private int mModifier = 0;
    private String mActionName;
    private boolean mLoadAction = false;
    private Action mAction;
    private ActionSaveListener mListener;

    // activity listener
    public interface ActionSaveListener {
        void updateActionValues(int diceSides, int diceCount, int modifier);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCharactersDatabase = CharactersDatabase.getInstance(getContext());
        if (getArguments() != null) {
            mDiceSides = getArguments().getInt(KEY_DICE_SIDES);
            mDiceCount = getArguments().getInt(KEY_DICE_COUNT);
            mModifier = getArguments().getInt(KEY_MODIFIER);
            mLoadAction = getArguments().getBoolean(KEY_ACTIVE_ACTION, false);
            if(mLoadAction){
                mAction = (mCharactersDatabase.actionDao().getDice(getArguments().getLong(KEY_ACTION)));
            }
        }
        mCharacter = mHost.getCharacter();
        saveSettings = mCharacter.getSaveSet();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Save Action");
        mCharactersDatabase = CharactersDatabase.getInstance(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.save_action_popup, null);

        TextView actionNameView = view.findViewById(R.id.ActionNameField);
        if(mLoadAction){
            actionNameView.setText(mAction.getActionName());
        }
        SeekBar diceCountSeekBar = view.findViewById(R.id.ActionCountBar);
        diceCountSeekBar.setProgress(mDiceCount - 1);
        SeekBar diceSidesSeekBar = view.findViewById(R.id.ActionSidesBar);
        diceSidesSeekBar.setProgress(mDiceSides - 1);
        TextView modifierView = view.findViewById(R.id.ActionModifierField);
        modifierView.setText(String.valueOf(mModifier));
        final Button saveButton = view.findViewById(R.id.ActionSaveActionButton);

        //listener for name
        actionNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mActionName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // listener for modifier
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
                mDiceCount = seekBar.getProgress() + 1; // range = [1,1 + max]
                String item = String.valueOf(mDiceCount);
                Toast.makeText(mHost, item, Toast.LENGTH_SHORT).show();
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
                mDiceSides = seekBar.getProgress() + 1;
                String item = String.valueOf(mDiceSides);
                Toast.makeText(mHost, item, Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save activity
                mListener.updateActionValues(mDiceSides, mDiceCount, mModifier);
                if(mLoadAction){
                    //update action
                    updateForSave();
                    mCharactersDatabase.actionDao().updateDice(mCharactersDatabase.actionDao().getDice(mAction.getId()));
                }
                else {
                    // new action
                    int characterClass = R.string.classNull;
                    if(saveSettings){
                        characterClass = mCharacter.getCharClass();
                    }
                    mAction = new Action(mDiceSides,mDiceCount,mModifier,mActionName);
                    long newId = mCharactersDatabase.actionDao().insertAction(mAction);
                    LinksBase link = new LinksBase(newId, mCharacter.getId(), characterClass);
                    mCharactersDatabase.linksBase().insertLinks(link);
                    mAction.setId(newId);
                    mLoadAction = true;
                }
            }
        });

        builder.setView(view);
        return builder.create();
    }

    private void updateForSave(){
        mAction.setActionName(mActionName);
        mAction.setDiceCount(mDiceCount);
        mAction.setNumSides(mDiceSides);
        mAction.setModifier(mModifier);
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mListener = (EditOrSaveAction.ActionSaveListener) context;
            mHost = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainActivity");
        }
    }

    @Override
    public void onDetach() {
        mListener.updateActionValues(mDiceSides, mDiceCount, mModifier);
        super.onDetach();
        mListener = null;
        mHost = null;
    }
}
