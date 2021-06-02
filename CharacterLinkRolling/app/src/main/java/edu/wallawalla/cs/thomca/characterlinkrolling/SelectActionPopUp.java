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

    // activity listener
    public interface PopDiceInteractionListener {
        void openAction(Action action);
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

        public ActionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.selection_item, parent, false));
            itemView.setOnClickListener(this);
            mTextView = itemView.findViewById(R.id.itemNameTab);
            mTrashCan = itemView.findViewById(R.id.deleteItem);
            mTrashCan.setOnClickListener(this);
        }

        public void bind(Action action, int position) {
            mTextView.setText(action.getActionName());
            mTrashCan.setTag(action.getId());
        }

        @Override
        public void onClick(View view) {
            // Return the selected character
            if(view == itemView) {
                TextView textView = view.findViewById(R.id.itemNameTab);
                String name = textView.getText().toString();
                // add find by name
                Action action = mCharactersDatabase.actionDao().getActionByName(name);
                mListener.openAction(action);
            }
            else if(view == mTrashCan){
                //FIXME delete item
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
            int index = mActionList.indexOf(action);
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
