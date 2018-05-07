package fr.giusti.onetapadventure.UI.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.UI.adapter.StringSelectionAdapter;

public class StringSelectionDialog extends Dialog implements StringSelectionAdapter.OnStringSelectedListener {

    private StringSelectionDialogListener mCallback;
    private RecyclerView mList;
    private TextView mTitleText;

    public StringSelectionDialog(@NonNull Context context, int title, ArrayList<String> options, StringSelectionDialogListener callback) {
        super(context);
        setContentView(R.layout.dialog_string_selection);

        mTitleText = findViewById(R.id.dss_title_tv);
        mTitleText.setText(title);

        mList = findViewById(R.id.dss_recycler_list);
        mList.setLayoutManager(new LinearLayoutManager(context));

        mCallback = callback;

        initList(options);
    }

    private void initList(ArrayList<String> options) {
        StringSelectionAdapter adapter = new StringSelectionAdapter(options, this);
        mList.setAdapter(adapter);
    }

    @Override
    public void onStringSelected(String selection) {
        mCallback.onSelectionDone(selection);
        dismiss();
    }


    public interface StringSelectionDialogListener {
        void onSelectionDone(String selection);
    }
}