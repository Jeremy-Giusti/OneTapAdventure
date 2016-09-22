package fr.giusti.onetapadventure.UI.Activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import fr.giusti.onetapadventure.gameObject.moves.SpecialMove;
import fr.giusti.onetapadventure.gameObject.moves.TouchedMove;
import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.repository.PathRepo;
import fr.giusti.onetapadventure.repository.entities.EntityRepo;
import fr.giusti.onetapadventure.repository.SpecialMoveRepo;
import fr.giusti.onetapadventure.repository.SpriteRepo;
import fr.giusti.onetapadventure.repository.TouchedMoveRepo;
import fr.giusti.onetapadventure.UI.CustomView.PathDrawingView;
import fr.giusti.onetapadventure.UI.CustomView.SpriteView;
import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.commons.FileUtils;

/**
 * gere une grande partie de ce qui a traité la creation de mob
 * ( skin, position initial, pattern de mouvement)
 *
 * @author giusti
 */
public class MobCreationActivity extends Activity {
    protected static final int SELECT_PHOTO = 95070;
    private static final String TAG = MobCreationActivity.class.getName();
    //private Button mValidateButton;
    private EditText mMobNameEdit;
    private Spinner mMobSpecialMoveSpinner;
    private Spinner mMobTouchedMoveSpinner;
    private EditText mXpositionEdit;
    private EditText mYpositionEdit;
    private EditText mWidthEdit;
    private EditText mHeightEdit;
    private EditText mHealthEdit;
    private ImageView mSkinImage;
    private Button mSkinSelectionButton;
    private PathDrawingView mPatternSurface;
    private GameMob mMobCreating = new GameMob("", 0, 0, 2, 2, null, null, null, null, 1, 1);
    protected PointF mPatternLastPoint;
    private ArrayList<PointF> mMobPattern;
    private int mobHealth;


    protected void onCreate(android.os.Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mob_creation_activity);
        initViews();
        initEvents();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mob_creation_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                validateMobAndReset();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void initViews() {
        mHealthEdit = (EditText) findViewById(R.id.mca_healthEdit);
        mMobNameEdit = (EditText) findViewById(R.id.mca_nameEdit);

        mXpositionEdit = (EditText) findViewById(R.id.mca_positionxEdit);
        mYpositionEdit = (EditText) findViewById(R.id.mca_positionyEdit);

        mWidthEdit = (EditText) findViewById(R.id.mca_widthEdit);
        mHeightEdit = (EditText) findViewById(R.id.mca_heightEdit);

        mMobSpecialMoveSpinner = (Spinner) findViewById(R.id.mca_speMoveSpinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, new SpecialMoveRepo().getMoveIdList());
        mMobSpecialMoveSpinner.setAdapter(adapter);

        mMobTouchedMoveSpinner = (Spinner) findViewById(R.id.mca_touchMoveSpinner);
        if (mMobTouchedMoveSpinner != null) {
            ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, new TouchedMoveRepo().getMoveIdList());
            mMobTouchedMoveSpinner.setAdapter(adapter2);
        }

        mSkinImage = (ImageView) findViewById(R.id.mca_mobSkinImage);
        mSkinSelectionButton = (Button) findViewById(R.id.mca_skinSelectionButton);
        mPatternSurface = (PathDrawingView) findViewById(R.id.mca_mobPatternSurface);
        // mValidateButton = (Button) findViewById(R.id.mca_validateButton);

    }

    private void initEvents() {
        // selection de l'image qui servira de skin
        mSkinSelectionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        // confirmation de la creation du mob actuel
//        mValidateButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                validateMob();
//                resetMobCreating();
//            }
//        });

        // enregistrement du pattern de mouvement
        mPatternSurface.setOnTouchListener(pathMakingTouchListener);

    }

    /**
     * check if all info are completed then
     * save the mob and clean the activity
     */
    private void validateMobAndReset() {
        boolean isValid = true;
        if (!TextUtils.isEmpty(mXpositionEdit.getText().toString()) &&
                !TextUtils.isEmpty(mYpositionEdit.getText().toString()) &&
                !TextUtils.isEmpty(mHealthEdit.getText().toString()) &&
                !TextUtils.isEmpty(mMobNameEdit.getText().toString()) &&
                mMobCreating.getMovePattern() != null) {

            SpecialMove specialMove = new SpecialMoveRepo().getMoveById(mMobSpecialMoveSpinner.getSelectedItem().toString());
            mMobCreating.setmSpecialMove1(specialMove);

            if (mMobTouchedMoveSpinner != null) {
                TouchedMove touchedMove = new TouchedMoveRepo().getMoveById(mMobTouchedMoveSpinner.getSelectedItem().toString());
                mMobCreating.setmTouchedMove(touchedMove);
            }
            int mobXposition = Integer.parseInt(mXpositionEdit.getText().toString());
            int mobYposition = Integer.parseInt(mYpositionEdit.getText().toString());

            int mobWidth = Integer.parseInt(mWidthEdit.getText().toString());
            int mobHeight = Integer.parseInt(mHeightEdit.getText().toString());

            mobHealth = Integer.parseInt(mHealthEdit.getText().toString());
            mMobCreating.setIdName(mMobNameEdit.getText().toString());
            mMobCreating.setPosition(new RectF(mobXposition, mobYposition, mobXposition + mobWidth, mobYposition + mobHeight));
            mMobCreating.setHealth(mobHealth);

            EntityRepo.saveGameMob(this, mMobCreating, null);

            Toast.makeText(this, "Mob créer:\n path lenght: " + mMobCreating.getMovePattern().length + "\n skin id: " + mMobCreating.getBitmapId(), Toast.LENGTH_LONG).show();
            resetMobCreating();

        } else {
            Toast.makeText(this, "Please complete the mob before validating.", Toast.LENGTH_SHORT);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri uri = imageReturnedIntent.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    String stringUri = FileUtils.getPath(this, uri);

                    manageSpriteSheetImport(stringUri);

                }
        }
    }

    /**
     * recense le spriteSheet dans le repo dedié et genere les proportion du mob en fonction de la taille de l'image
     *
     * @param spriteSheetUrl
     */
    private void manageSpriteSheetImport(String spriteSheetUrl) {
        try {
            File spriteSheetFile = new File(spriteSheetUrl);
            String spriteSheetId = spriteSheetFile.getName();
            Point singleSpriteDimens = SpriteRepo.saveAndLoadFile(this, spriteSheetUrl, spriteSheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT,true);

            mMobCreating.setBitmapId(spriteSheetId);

            // generation de l'epaisseur/hauteur du mob selon son skin
            mWidthEdit.setText("" + singleSpriteDimens.x);
            mHeightEdit.setText("" + singleSpriteDimens.y);
            if (mSkinImage instanceof SpriteView) {
                ((SpriteView) mSkinImage).setSpriteSheet(spriteSheetId);
            } else {
                mSkinImage.setImageBitmap(SpriteRepo.getPicture(spriteSheetId));
            }
        } catch (IOException e) {
            Log.e(TAG, "error while loading spritesheet: " + e);
            Toast.makeText(this, "error while loading spritesheet", Toast.LENGTH_SHORT);
        }

    }

    OnTouchListener pathMakingTouchListener = new OnTouchListener() {
        boolean pathStarted = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mMobPattern = new ArrayList<>();
                    mPatternLastPoint = new PointF( event.getX(),  event.getY());
                    mMobPattern.add(new PointF(0, 0));
                    break;

                case MotionEvent.ACTION_UP:
                    PointF[] resultPattern = new PointF[mMobPattern.size()];
                    mMobCreating.setMovePattern(PathRepo.softenPath(mMobPattern.toArray(resultPattern)));
                    v.performClick();
                    break;

                case MotionEvent.ACTION_MOVE:
                    PointF newPoint = new PointF( event.getX() - mPatternLastPoint.x,  event.getY() - mPatternLastPoint.y);
                    if (pathStarted || (newPoint.x != 0 || newPoint.y != 0)) {
                        pathStarted = true;
                        mMobPattern.add(newPoint);
                        mPatternLastPoint = new PointF(event.getX(),  event.getY());
                    }
                    break;

                default:
                    break;
            }
            return false;
        }
    };

    /**
     * remet tout a zero (comme si l'activité venait de s'ouvrir)
     */
    private void resetMobCreating() {
        mMobCreating = new GameMob("", 0, 0, 2, 2, null, null, null, null, 1, 1);
        mPatternSurface.reset();
        mMobNameEdit.setText("");
        mXpositionEdit.setText("");
        mYpositionEdit.setText("");
        mHealthEdit.setText("");
        mSkinImage.setImageResource(android.R.drawable.ic_menu_help);
        mPatternSurface.reset();
        mPatternSurface.invalidate();
    }

    public void onClickGeneratePath(View view) {

    }
}
