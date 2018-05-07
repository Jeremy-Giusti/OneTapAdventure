package fr.giusti.onetapadventure.UI.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fr.giusti.onetapadventure.R;

public class StringSelectionAdapter extends RecyclerView.Adapter<StringSelectionAdapter.StringSelectionHolder> {

    private final ArrayList<String> mStringOptionList;
    private OnStringSelectedListener mListener;

    public StringSelectionAdapter(ArrayList<String> stringOptionList, OnStringSelectedListener listener) {
        this.mStringOptionList = stringOptionList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public StringSelectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_string_selection, parent, false);
        return new StringSelectionHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StringSelectionHolder holder, int position) {
        holder.text.setText(mStringOptionList.get(position));
        holder.rootView.setOnClickListener(v -> mListener.onStringSelected(mStringOptionList.get(position)));
    }

    @Override
    public int getItemCount() {
        return mStringOptionList.size();
    }

    public static class StringSelectionHolder extends RecyclerView.ViewHolder {
        protected TextView text;
        private View rootView;

        private StringSelectionHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.iss_string_tv);
            rootView = itemView.findViewById(R.id.iss_root);
        }
    }

    public interface OnStringSelectedListener {
        void onStringSelected(String selection);
    }
}
