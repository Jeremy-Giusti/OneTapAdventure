package fr.giusti.onetapadventure.UI.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import fr.giusti.onetapadventure.R;

public class NumberInputDialog extends Dialog implements View.OnClickListener {

    private NumberInputDialogListener mCallback;
    private NumberPicker mNbPicker;
    private TextView mTitleText;

    public NumberInputDialog(@NonNull Context context, int title, NumberInputDialogListener callback) {
        super(context);
        setContentView(R.layout.dialog_input_number);

        mTitleText = findViewById(R.id.din_title_tv);
        mNbPicker = findViewById(R.id.din_text_nb_picker_et);
        Button yes = findViewById(R.id.din_confirm_bt);
        Button no = findViewById(R.id.din_cancel_bt);

        mTitleText.setText(title);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        mCallback = callback;
    }

    public void setMax(int max) {
        mNbPicker.setMaxValue(max);
    }

    public void setMin(int min) {
        mNbPicker.setMinValue(min);
    }

    public void setValue(int value) {
        mNbPicker.setValue(value);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.din_confirm_bt:
                mNbPicker.clearFocus();
                mCallback.onNumberSet(mNbPicker.getValue());
                NumberInputDialog.this.dismiss();
                break;
            case R.id.din_cancel_bt:
                NumberInputDialog.this.dismiss();
                break;
            default:
                //oops
                break;
        }
    }


    public interface NumberInputDialogListener {
        void onNumberSet(int number);
    }

}
