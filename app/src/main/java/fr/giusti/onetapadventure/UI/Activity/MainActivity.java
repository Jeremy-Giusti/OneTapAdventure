package fr.giusti.onetapadventure.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.repository.SpriteRepo;

/**
 * ecran menu
 *
 * @author giusti
 */
public class MainActivity extends Activity {
    private final static String TAG = MainActivity.class.getName();

    private Button mTestAreaButton;
    private Button mMobCreationScreenButton;
    private Button mLoadAllButton;
    private Button mFlushAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initViews();
        initEvents();

    }

    private void initViews() {
        this.mTestAreaButton = (Button) findViewById(R.id.ma_game_screen_button);
        this.mMobCreationScreenButton = (Button) findViewById(R.id.ma_create_mob_button);
        this.mLoadAllButton = (Button) findViewById(R.id.ma_Load_all_button);
        this.mFlushAllButton = (Button) findViewById(R.id.ma_flush_all_button);

    }

    private void initEvents() {
        this.mTestAreaButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG,"test area clicked");
                Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
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

//        mLoadAllButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int mobsLoaded = 0;
//                try {
//                    mobsLoaded = MobRepo.LoadMobsFromDb(MainActivity.this);
//                    Toast.makeText(MainActivity.this, "" + mobsLoaded + " Mobs loaded", Toast.LENGTH_SHORT).show();
//
//                } catch (IOException e) {
//                    Log.e(TAG, "error while charging Db Mobs to cache" + e);
//                    Toast.makeText(MainActivity.this, "erreur au chargement des mobs",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        mFlushAllButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SpriteRepo.flushCache();
            }
        });

    }

}
