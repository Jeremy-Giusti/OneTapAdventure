package fr.giusti.onetapadventure.UI.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import fr.giusti.onetapadventure.R;

public class DoubleNumberInputDialog extends Dialog implements View.OnClickListener {

    private DoubleNumberInputDialogListener mCallback;
    private NumberPicker mNbPicker1;
    private NumberPicker mNbPicker2;
    private TextView mTitleText;

    public DoubleNumberInputDialog(@NonNull Context context, int title, DoubleNumberInputDialogListener callback) {
        super(context);
        setContentView(R.layout.dialog_double_input_number);

        mTitleText = findViewById(R.id.ddin_title_tv);
        mNbPicker1 = findViewById(R.id.ddin_text_nb_picker1_et);
        mNbPicker2 = findViewById(R.id.ddin_text_nb_picker2_et);

        Button yes = findViewById(R.id.ddin_confirm_bt);
        Button no = findViewById(R.id.ddin_cancel_bt);

        mTitleText.setText(title);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        mCallback = callback;
    }

    public void settupNbPicker1(int value, int min, int max) {
        mNbPicker1.setMinValue(min);
        mNbPicker1.setMaxValue(max);
        mNbPicker1.setValue(value);
    }

    public void settupNbPicker2(int value, int min, int max) {
        mNbPicker2.setMinValue(min);
        mNbPicker2.setMaxValue(max);
        mNbPicker2.setValue(value);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ddin_confirm_bt:
                mNbPicker1.clearFocus();
                mNbPicker2.clearFocus();
                mCallback.onNumbersSet(mNbPicker1.getValue(), mNbPicker2.getValue());
                DoubleNumberInputDialog.this.dismiss();
                break;
            case R.id.ddin_cancel_bt:
                DoubleNumberInputDialog.this.dismiss();
                break;
            default:
                //oops
                break;
        }
    }


    public interface DoubleNumberInputDialogListener {
        void onNumbersSet(int number1, int number2);
    }

}
