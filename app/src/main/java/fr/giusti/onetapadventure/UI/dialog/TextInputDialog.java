package fr.giusti.onetapadventure.UI.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.giusti.onetapadventure.R;

/**
 * Created by jérémy on 06/05/2018.
 */

public class TextInputDialog extends Dialog implements View.OnClickListener {
    private TextInputDialogListener mCallback;
    private EditText mEditText;
    private TextView mTitleText;

    public TextInputDialog(@NonNull Context context, int title, TextInputDialogListener callback) {
        super(context);
        setContentView(R.layout.dialog_input_text);

        mTitleText = findViewById(R.id.dit_title_tv);
        mEditText = findViewById(R.id.dit_text_field_et);
        Button yes = findViewById(R.id.dit_confirm_bt);
        Button no = findViewById(R.id.dit_cancel_bt);

        mTitleText.setText(title);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        mCallback = callback;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dit_confirm_bt:
                mCallback.onTextSet(mEditText.getText().toString());
                TextInputDialog.this.dismiss();
                break;
            case R.id.dit_cancel_bt:
                TextInputDialog.this.dismiss();
                break;
            default:
                //oops
                break;
        }
    }


    public interface TextInputDialogListener {
        void onTextSet(String text);
    }

}
