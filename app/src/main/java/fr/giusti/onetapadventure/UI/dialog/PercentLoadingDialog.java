package fr.giusti.onetapadventure.UI.dialog;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import fr.giusti.onetapadventure.R;

/**
 * Created by jérémy on 07/04/2018.<br>
 * used to display any kind of loading as a progress bar (use percentage)
 */

public class PercentLoadingDialog extends DialogFragment {
    private static final String ARG_TITLE = "title";
    private ProgressBar mProgressBar;

    public PercentLoadingDialog() {
        // Empty constructor required for DialogFragment
    }

    public static PercentLoadingDialog newInstance(String title) {
        PercentLoadingDialog frag = new PercentLoadingDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(ARG_TITLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View root = inflater.inflate(R.layout.loading_dialog, null);
        mProgressBar = (ProgressBar) root.findViewById(R.id.ld_percent_progress_bar);
        builder.setTitle(title).setView(root);
        return builder.create();
    }

    public void setProgress(int progressPercent) {
        if (mProgressBar != null) {
            ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", progressPercent);
            animation.setDuration(200);
            animation.setInterpolator(new LinearInterpolator());
            animation.start();
        }
    }
}
