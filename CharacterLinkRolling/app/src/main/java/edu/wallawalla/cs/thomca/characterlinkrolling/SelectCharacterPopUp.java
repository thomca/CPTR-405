package edu.wallawalla.cs.thomca.characterlinkrolling;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        void characterDeleted(long characterId);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.namesTitle);
        mCharactersDatabase = CharactersDatabase.getInstance(getContext());

        builder.setView(R.layout.selection_menu_popup);
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
        View view = inflater.inflate(R.layout.selection_menu_popup, container, false);

        // create a layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        return view;
    }

    // load characters from database
    private List<Character> loadCharacters() {
        return mCharactersDatabase.characterDao().getCharacters();
    }
    private class CharacterHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mTextView;
        private ImageView mTrashCan;

        public CharacterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.selection_character, parent, false));
            itemView.setOnClickListener(this);
            mTextView = itemView.findViewById(R.id.itemNameTab);
            mTrashCan = itemView.findViewById(R.id.deleteItem);
            mTrashCan.setOnClickListener(this);
        }

        public void bind(Character character, int position) {
            mTextView.setText(character.getName());
            mTextView.setTag(character.getId());
            mTrashCan.setTag(character.getId());
        }

        @Override
        public void onClick(View view) {
            // Return the selected character
            Character character;
            if(view == itemView) {
                TextView textView = view.findViewById(R.id.itemNameTab);
                long id = (long) textView.getTag();
                character = mCharactersDatabase.characterDao().getCharacter(id);
                mListener.openCharacter(character);
            }
            else if(view == mTrashCan){
                character = mCharactersDatabase.characterDao().getCharacter((Long) view.getTag());
                List<LinksBase> links = mCharactersDatabase.linksBase().getLinksBase();
                for(int i = 0; i < links.size(); i++){
                    // delete associated actions, consider revising with dif logic
                   if (links.get(i).getCharId() == character.getId() ){
                       long actionId = links.get(i).getActId();
                       mCharactersDatabase.actionDao().deleteDice(mCharactersDatabase.actionDao().getDice(actionId));
                   }
                }
                mCharactersDatabase.characterDao().deleteCharacter(character);
                characterAdapter.removeCharacter(character);
                mListener.characterDeleted(character.getId());
            }
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

        public void removeCharacter(Character character) {
            // Find subject in the list
            int index = -1;
            for(int i = 0; i < this.getItemCount(); i++){
                if(mCharacterList.get(i).getId() == character.getId()){
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                // Remove the subject
                mCharacterList.remove(index);

                // Notify adapter of subject removal
                notifyItemRemoved(index);
            }
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
