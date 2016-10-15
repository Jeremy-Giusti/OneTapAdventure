package fr.giusti.onetapadventure.UI.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.commons.GameConstant;
import fr.giusti.onetapadventure.repository.GameRepo;
import fr.giusti.onetapadventure.repository.SpriteRepo;

/**
 * ecran menu
 *
 * @author giusti
 */
public class MainActivity extends PermissionAskerActivity {
    private final static String TAG = MainActivity.class.getName();

    private Button mGameAreaButton, mTestAreaButton, mMobCreationScreenButton, mLoadAllButton, mFlushAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initViews();
        initEvents();

    }

    private void initViews() {
        this.mTestAreaButton = (Button) findViewById(R.id.ma_test_screen_button);
        this.mGameAreaButton = (Button) findViewById(R.id.ma_game_screen_button);
        this.mMobCreationScreenButton = (Button) findViewById(R.id.ma_create_mob_button);
        this.mFlushAllButton = (Button) findViewById(R.id.ma_flush_all_button);

    }

    private void initEvents() {
        this.mTestAreaButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "test area clicked");
                Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
                myIntent.putExtra(GameConstant.LEVEL_NAME, GameRepo.LVL_TEST);
                MainActivity.this.startActivity(myIntent);

            }
        });

        this.mGameAreaButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "game area clicked");
                Intent myIntent = new Intent(MainActivity.this, LvlSelectionActivity.class);
                //myIntent.putExtra(GameConstant.LEVEL_NAME, GameRepo.LVL_1);
                MainActivity.this.startActivity(myIntent);

            }
        });

        this.mMobCreationScreenButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, MobCreationActivity.class);
                MainActivity.this.startActivity(myIntent);

            }
        });

        mFlushAllButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SpriteRepo.flushCache();
            }
        });

    }

}
