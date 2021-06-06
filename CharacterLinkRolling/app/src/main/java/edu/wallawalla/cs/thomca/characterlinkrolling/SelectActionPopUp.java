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

import java.util.ArrayList;
import java.util.List;

public class SelectActionPopUp extends DialogFragment {
    private CharactersDatabase mCharactersDatabase;
    private RecyclerView mRecyclerView;
    private ActionAdapter actionAdapter;
    private PopDiceInteractionListener mListener;
    private MainActivity mHost;
    private final static String KEY_ACTIVE_ACTION = "Action Bool";
    private final static String KEY_ACTION = "Action";
    private Action mAction;
    private boolean mActionAvailable = false;

    // activity listener
    public interface PopDiceInteractionListener {
        void openAction(Action action);
        void editAction(Action action);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Actions");
        mCharactersDatabase = CharactersDatabase.getInstance(getContext());
        builder.setView(R.layout.selection_menu_popup);
        Character character = mHost.getCharacter();
        if(character != null){
            mRecyclerView = new RecyclerView(getContext());
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            actionAdapter = new ActionAdapter(loadActions(character));
            mRecyclerView.setAdapter(actionAdapter);
            builder.setView(mRecyclerView);
        }
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

    // load actions from database
    private List<Action> loadActions(Character character) {
        List<LinksBase> links = mCharactersDatabase.linksBase().getLinksBase();
        List<Action> actions = new ArrayList<>();
        for(LinksBase x:links){
            if(x.getCharId() == character.getId() || (x.getActClass() == character.getCharClass() && x.getActClass() != R.string.classNull)) {
                actions.add(mCharactersDatabase.actionDao().getDice(x.getActId()));
            }
        }
        return actions;
    }

    private class ActionHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView mTextView;
        private ImageView mTrashCan;
        private ImageView mEdit;

        public ActionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.selection_action, parent, false));
            itemView.setOnClickListener(this);
            mTextView = itemView.findViewById(R.id.itemNameTab);
            mTrashCan = itemView.findViewById(R.id.deleteItem);
            mEdit = itemView.findViewById(R.id.editItem);
            mTrashCan.setOnClickListener(this);
            mEdit.setOnClickListener(this);
        }

        public void bind(Action action, int position) {
            mTextView.setText(action.getActionName());
            mTextView.setTag(action.getId());
            mTrashCan.setTag(action.getId());
            mEdit.setTag(action.getId());
        }

        @Override
        public void onClick(View view) {
            // Return the selected character
            if(view == itemView) {
                TextView textView = view.findViewById(R.id.itemNameTab);
                long actionId = (long) textView.getTag();
                // add find by name
                Action action = mCharactersDatabase.actionDao().getDice(actionId);
                mListener.openAction(action);
            }
            else if(view == mTrashCan){
                //check if linkBase updated
                mAction = mCharactersDatabase.actionDao().getDice((Long) view.getTag());
                mCharactersDatabase.actionDao().deleteDice(mAction);
                actionAdapter.removeAction(mAction);
            }
            else if(view == mEdit){
                mAction = mCharactersDatabase.actionDao().getDice((Long) view.getTag());
                mListener.editAction(mAction);
            }
        }

    }

    private class ActionAdapter extends RecyclerView.Adapter<ActionHolder> {

        private List<Action> mActionList;

        public ActionAdapter(List<Action> actions) {
            mActionList = actions;
        }

        @NonNull
        @Override
        public ActionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ActionHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ActionHolder holder, int position){
            holder.bind(mActionList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mActionList.size();
        }

        public void removeAction(Action action) {
            // Find subject in the list
            int index = -1;
            for(int i = 0; i < this.getItemCount(); i++){
                if(mActionList.get(i).getId() == action.getId()){
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                // Remove the subject
                mActionList.remove(index);

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
            mListener = (PopDiceInteractionListener) context;
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
