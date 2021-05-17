package edu.wallawalla.cs.thomca.characterlinkrolling;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SelectCharacterPopUp  extends DialogFragment {
    private LayoutInflater mInflater;
    private List<Integer> data;
    private CharactersDatabase mCharactersDatabase;
    private int[] mSubjectColors;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.namesTitle);
        mCharactersDatabase = CharactersDatabase.getInstance(getContext());
        mSubjectColors = getResources().getIntArray(R.array.subjectColors);

        builder.setView(R.layout.character_select_popup);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.character_select_popup, container, false);

        // create a layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // set up recycler
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_character_popup);
        recyclerView.setLayoutManager(linearLayoutManager);

        CharacterAdapter characterAdapter = new CharacterAdapter(loadCharacters());
        recyclerView.setAdapter(characterAdapter);

        return view;

    }

    // load characters from database
    private List<Character> loadCharacters() {
        return mCharactersDatabase.characterDao().getCharacters();
    }
    private class CharacterHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Character mCharacter;
        private TextView mTextView;

        public CharacterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.character_name, parent, false));
            itemView.setOnClickListener(this);
            mTextView = itemView.findViewById(R.id.characterNameTab);
        }

        public void bind(Character character, int position) {
            mCharacter = character;
            mTextView.setText(character.getName());

            // Make the background color dependent on the length of the subject string
            int colorIndex = character.getName().length() % mSubjectColors.length;
            mTextView.setBackgroundColor(mSubjectColors[colorIndex]);
        }

        @Override
        public void onClick(View view) {
            // Return the selected character
        }
    }

    private class CharacterAdapter extends RecyclerView.Adapter<CharacterHolder> {

        private List<Character> mCharacterList;

        public CharacterAdapter(List<Character> characters) {
            mCharacterList = characters;
        }

        @NonNull
        @Override
        public CharacterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CharacterHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CharacterHolder holder, int position){
            holder.bind(mCharacterList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mCharacterList.size();
        }
    }
}
