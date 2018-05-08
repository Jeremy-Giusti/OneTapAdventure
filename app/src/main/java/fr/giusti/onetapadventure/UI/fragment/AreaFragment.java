package fr.giusti.onetapadventure.UI.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.UI.adapter.LvlButtonAdapter;
import fr.giusti.onetapadventure.UI.activity.GameActivity;
import fr.giusti.onetapengine.commons.GameConstant;

/**
 * Created by jérémy on 22/09/2016.
 */
public class AreaFragment extends Fragment implements LvlButtonAdapter.onButtonClickListener {

    private int backgroundRessource;
    private int lvlCount;
    private int areaNumber;
    //TODO fond customizable
    // grid view avec boutton pour les lvl


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_area, container, false);
        // Inflate the layout for this fragment
        manageArgs();
        initViews(rootView);
        return rootView;
    }

    private void manageArgs() {
        lvlCount = getArguments().getInt(GameConstant.LVL_COUNT, -1);
        areaNumber = getArguments().getInt(GameConstant.AREA, -1);
        backgroundRessource = getArguments().getInt(GameConstant.BACKGROUND, -1);

    }

    private void initViews(View rootView) {
        if (backgroundRessource != -1) {
            rootView.setBackgroundResource(backgroundRessource);
        }

        GridView gridview = (GridView) rootView.findViewById(R.id.af_gridview);
        //TODO calculate button size
        gridview.setAdapter(new LvlButtonAdapter(lvlCount, areaNumber, getActivity(), 64, this));
        //TODO grid view with button for lvl
    }

    @Override
    public void onButtonClick(int area, int level) {
        Intent intent = new Intent(getActivity(), GameActivity.class);
        intent.putExtra(GameConstant.LEVEL_NAME, GameConstant.getLevelId(area, level));
        startActivity(intent);
    }
}
