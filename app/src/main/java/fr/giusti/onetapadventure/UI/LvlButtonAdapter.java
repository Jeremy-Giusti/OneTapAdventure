package fr.giusti.onetapadventure.UI;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import fr.giusti.onetapadventure.R;

/**
 * Created by jérémy on 22/09/2016.
 */

public class LvlButtonAdapter extends BaseAdapter {
    private final Context context;
    private final int buttonSize;
    private int elements;
    private int area;
    private onButtonClickListener listener;

    public LvlButtonAdapter(int elements, int area, Context context, int buttonSize, onButtonClickListener listener) {
        this.elements = elements;
        this.context = context;
        this.buttonSize = buttonSize;
        this.listener = listener;
        this.area = area;
    }

    @Override
    public int getCount() {
        return elements;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        Button btn;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            btn = (Button) inflater.inflate(R.layout.lvl_button, null);
//
//            btn.getLayoutParams().width = buttonSize;
//            btn.getLayoutParams().height = buttonSize;
        } else {
            btn = (Button) convertView;
        }

        btn.setText("" + area + "x" + (i + 1));
        // filenames is an array of strings
        btn.setTextColor(Color.WHITE);
        btn.setId(i);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onButtonClick(area, i + 1);
            }
        });

        return btn;
    }

    public interface onButtonClickListener {
        void onButtonClick(int area, int level);
    }
}

