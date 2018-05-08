package fr.giusti.onetapadventure.UI.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.UI.customView.PathDrawingView;
import fr.giusti.onetapengine.repository.PathRepo;

public class PathDrawingDialog extends Dialog implements View.OnClickListener {
    private PointF[] mPath = null;


    private PathDrawingDialog.PathDialogListener mCallback;
    private PathDrawingView mPathView;
    private TextView mTitleText;

    public PathDrawingDialog(@NonNull Context context, int title, PathDialogListener callback) {
        super(context);
        setContentView(R.layout.dialog_path_drawing_dialog);

        mTitleText = findViewById(R.id.dpdd_title_tv);
        mPathView = findViewById(R.id.dpdd_path_drawing_view);
        Button yes = findViewById(R.id.dpdd_confirm_bt);
        Button no = findViewById(R.id.dpdd_cancel_bt);

        mTitleText.setText(title);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        mCallback = callback;

        mPathView.setOnTouchListener(pathMakingTouchListener);
    }

    public void setPath(PointF[] path) {
        this.mPath = path;
        if (mPath != null) {
            mPathView.setPath(mPath);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dpdd_confirm_bt:
                mCallback.onPathDone(mPath);
                PathDrawingDialog.this.dismiss();
                break;
            case R.id.dpdd_cancel_bt:
                PathDrawingDialog.this.dismiss();
                break;
            default:
                //oops
                break;
        }
    }


    public interface PathDialogListener {
        void onPathDone(PointF[] path);
    }


    private View.OnTouchListener pathMakingTouchListener = new View.OnTouchListener() {
        private boolean pathStarted = false;
        private ArrayList<PointF> mMobPattern;
        private PointF mPatternLastPoint;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mMobPattern = new ArrayList<>();
                    mPatternLastPoint = new PointF(event.getX(), event.getY());
                    mMobPattern.add(new PointF(0, 0));
                    break;

                case MotionEvent.ACTION_UP:
                    PointF[] resultPattern = new PointF[mMobPattern.size()];
                    mPath = PathRepo.softenPath(mMobPattern.toArray(resultPattern));
                    v.performClick();
                    break;

                case MotionEvent.ACTION_MOVE:
                    PointF newPoint = new PointF(event.getX() - mPatternLastPoint.x, event.getY() - mPatternLastPoint.y);
                    if (pathStarted || (newPoint.x != 0 || newPoint.y != 0)) {
                        pathStarted = true;
                        mMobPattern.add(newPoint);
                        mPatternLastPoint = new PointF(event.getX(), event.getY());
                    }
                    break;

                default:
                    break;
            }
            return false;
        }
    };


}
