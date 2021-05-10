package edu.wallawalla.cs.thomca.characterlinkrolling;

import android.app.Dialog;
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.namesTitle);

        builder.setView(R.layout.character_select_popup);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.character_select_popup, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = mInflater.inflate(R.layout.character_select_popup, parent, false);
                RecyclerView.ViewHolder dataHolder = new DataHolder(view);
                return dataHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }


        };
        recyclerView.setAdapter(adapter);

        return view;
    }

    public static class DataHolder extends RecyclerView.ViewHolder {
        TextView area;

        public DataHolder(View itemView) {
            super(itemView);
            area = (TextView) itemView.findViewById(R.id.recycler_view);
        }

        public void setData(String text) {
            area.setText(text);
        }
    }

}
