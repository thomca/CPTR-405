package edu.wallawalla.cs.thomca.characterlinkrolling;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SelectCharacterPopUp  extends DialogFragment {
    private CharactersDatabase mCharactersDatabase;
    private RecyclerView mRecyclerView;
    private CharacterAdapter characterAdapter;
    private SelectCharacterPopUp.PopInteractionListener mListener;
    private MainActivity mHost;

    // activity listener
    public interface PopInteractionListener {
        void openCharacter(Character character);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.namesTitle);
        mCharactersDatabase = CharactersDatabase.getInstance(getContext());

        builder.setView(R.layout.character_select_popup);
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        characterAdapter = new CharacterAdapter(loadCharacters());
        mRecyclerView.setAdapter(characterAdapter);
        builder.setView(mRecyclerView);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.character_select_popup, container, false);

        // create a layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        return view;
    }

//    @Override
//    public void onResume(){
//        // Sets the height and the width of the DialogFragment
//        int width = FrameLayout.LayoutParams.MATCH_PARENT;
//        int height = FrameLayout.LayoutParams.MATCH_PARENT;
//        getDialog().getWindow().setLayout(width, height);
//
//        super.onResume();
//    }

    // load characters from database
    private List<Character> loadCharacters() {
        return mCharactersDatabase.characterDao().getCharacters();
    }
    private class CharacterHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mTextView;

        public CharacterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.character_name, parent, false));
            itemView.setOnClickListener(this);
            mTextView = itemView.findViewById(R.id.characterNameTab);
        }

        public void bind(Character character, int position) {
            mTextView.setText(character.getName());
        }

        @Override
        public void onClick(View view) {
            // Return the selected character
            TextView textView = view.findViewById(R.id.characterNameTab);
            String name = textView.getText().toString();
            Character character = mCharactersDatabase.characterDao().getCharacterByName(name);
            mListener.openCharacter(character);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mHost = (MainActivity) context;
            mListener = (SelectCharacterPopUp.PopInteractionListener) context;
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
}
