package edu.wallawalla.cs.thomca.characterlinkrolling;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EditOrSaveAction extends DialogFragment {
    private CharactersDatabase mCharactersDatabase;
    private MainActivity mHost;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Save Action");
        mCharactersDatabase = CharactersDatabase.getInstance(getContext());
        builder.setView(R.layout.save_action_popup);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mHost = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainActivity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHost = null;
    }
}
