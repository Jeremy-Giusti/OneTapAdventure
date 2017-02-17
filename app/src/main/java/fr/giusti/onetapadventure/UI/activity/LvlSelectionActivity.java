package fr.giusti.onetapadventure.UI.activity;

import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.UI.fragment.AreaFragment;
import fr.giusti.onetapengine.commons.GameConstant;

/**
 * Created by jérémy on 22/09/2016.
 */

public class LvlSelectionActivity extends FragmentActivity {

    private ArrayList<AreaFragment> areaList = new ArrayList<>();
    private android.support.v4.view.ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lvl_selection_activity);
        initAreaFragment();
        initPager();

    }

    private void initAreaFragment() {
        AreaFragment chaosArea = new AreaFragment();
        Bundle args = new Bundle();
        args.putInt(GameConstant.BACKGROUND, GameConstant.AREA_1_BACKGROUND);
        args.putInt(GameConstant.LVL_COUNT, GameConstant.AREA_1_LVL_COUNT);
        args.putInt(GameConstant.AREA, 1);

        chaosArea.setArguments(args);
        areaList.add(chaosArea);

        //TODO next area

    }

    private void initPager() {
        pager = (ViewPager) findViewById(R.id.lsa_pager);
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public android.app.Fragment getItem(int position) {
                return areaList.get(position);
            }

            @Override
            public int getCount() {
                return areaList.size();
            }
        };
        pager.setAdapter(adapter);
    }
}
