package fr.giusti.onetapadventure.UI.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.UI.dialog.DoubleNumberInputDialog;
import fr.giusti.onetapadventure.UI.dialog.NumberInputDialog;
import fr.giusti.onetapadventure.UI.dialog.StringSelectionDialog;
import fr.giusti.onetapadventure.UI.dialog.TextInputDialog;
import fr.giusti.onetapadventure.UI.viewmodel.MobCreationViewModel;
import fr.giusti.onetapadventure.databinding.ActivityMobCreationBinding;
import fr.giusti.onetapengine.commons.GameConstant;
import fr.giusti.onetapengine.repository.SpecialMoveRepo;
import fr.giusti.onetapengine.repository.TouchedMoveRepo;

/**
 * gere une grande partie de ce qui a traité la creation de mob
 * ( skin, position initial, pattern de mouvement)
 *
 * @author giusti
 */
public class MobCreationActivity extends AppCompatActivity {
    private static final String TAG = MobCreationActivity.class.getName();
    private MobCreationViewModel mobViewModel;


    protected void onCreate(android.os.Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActivityMobCreationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_mob_creation);
        mobViewModel = ViewModelProviders.of(this).get(MobCreationViewModel.class);
        binding.setGameMobBuilder(mobViewModel.getGameMob());
    }


    public void onClickedSpriteView(View v) {
        //TODO custom spritesheet ?
    }

    public void onClickedNameView(View v) {
        new TextInputDialog(this, R.string.set_name, text -> mobViewModel.getGameMob().setIdName(text)).show();
    }

    public void onClickedPosView(View v) {
        DoubleNumberInputDialog nbDialog = new DoubleNumberInputDialog(this, R.string.set_pos, (number1, number2) -> mobViewModel.getGameMob().setPositionXY(number1, number2));
        //TODO magic numbers
        nbDialog.settupNbPicker1((int) mobViewModel.getGameMob().getX(), 0, 1024);
        nbDialog.settupNbPicker2((int) mobViewModel.getGameMob().getY(), 0, 512);
        nbDialog.show();
    }

    public void onClickedPathView(View v) {

    }

    public void onClickedHealthView(View v) {
        NumberInputDialog nbDialog = new NumberInputDialog(this, R.string.set_health, number -> mobViewModel.getGameMob().setDefaultHealth(number));
        nbDialog.setValue(mobViewModel.getGameMob().getDefaultHealthValue());
        nbDialog.setMin(1);
        nbDialog.setMax(GameConstant.MAX_MOB_HEALTH);
        nbDialog.show();
    }

    public void onClickedAlignmentView(View v) {
        NumberInputDialog nbDialog = new NumberInputDialog(this, R.string.set_alignement, number -> mobViewModel.getGameMob().setAlignement(number));
        nbDialog.setValue(mobViewModel.getGameMob().getDefaultHealthValue());
        nbDialog.setMin(0);
        nbDialog.setMax(GameConstant.MAX_MOB_ALIGNEMENT);
        nbDialog.show();

    }

    public void onClickedSpeMoveView(View v) {
        new StringSelectionDialog(this,
                R.string.set_spe_move,
                SpecialMoveRepo.getMoveIdList(),
                selection -> mobViewModel.getGameMob().setSpecialMove(SpecialMoveRepo.getMoveById(selection)))
                .show();
    }

    public void onClickedTouchMoveView(View v) {
        new StringSelectionDialog(this, R.string.set_touch_move, TouchedMoveRepo.getMoveIdList(), selection -> mobViewModel.getGameMob().setTouchedMove(TouchedMoveRepo.getMoveById(selection))).show();
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

    /**
     * check if all info are completed then
     * save the mob and clean the activity
     */
    private void validateMobAndReset() {
        //TODO save current mob and reset screen

    }

}

//
//    /**
//     * recense le spriteSheet dans le repo dedié et genere les proportion du mob en fonction de la taille de l'image
//     *
//     * @param spriteSheetUrl
//     */
//    private void manageSpriteSheetImport(String spriteSheetUrl) {
//        try {
//            File spriteSheetFile = new File(spriteSheetUrl);
//            String spriteSheetId = spriteSheetFile.getName();
//            Point singleSpriteDimens = SpriteRepo.saveAndLoadFile(this, spriteSheetUrl, spriteSheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT,true);
//
//            mMobCreating.setBitmapId(spriteSheetId);
//
//            // generation de l'epaisseur/hauteur du mob selon son skin
//            mWidthEdit.setText("" + singleSpriteDimens.x);
//            mHeightEdit.setText("" + singleSpriteDimens.y);
//            if (mSkinImage instanceof SpriteView) {
//                ((SpriteView) mSkinImage).setSpriteSheet(spriteSheetId);
//            } else {
//                mSkinImage.setImageBitmap(SpriteRepo.getPicture(spriteSheetId));
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "error while loading spritesheet: " + e);
//            Toast.makeText(this, "error while loading spritesheet", Toast.LENGTH_SHORT);
//        }
//
//    }
//
//    OnTouchListener pathMakingTouchListener = new OnTouchListener() {
//        boolean pathStarted = false;
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getActionMasked()) {
//                case MotionEvent.ACTION_DOWN:
//                    mMobPattern = new ArrayList<>();
//                    mPatternLastPoint = new PointF( event.getX(),  event.getY());
//                    mMobPattern.add(new PointF(0, 0));
//                    break;
//
//                case MotionEvent.ACTION_UP:
//                    PointF[] resultPattern = new PointF[mMobPattern.size()];
//                    mMobCreating.setMovePattern(PathRepo.softenPath(mMobPattern.toArray(resultPattern)));
//                    v.performClick();
//                    break;
//
//                case MotionEvent.ACTION_MOVE:
//                    PointF newPoint = new PointF( event.getX() - mPatternLastPoint.x,  event.getY() - mPatternLastPoint.y);
//                    if (pathStarted || (newPoint.x != 0 || newPoint.y != 0)) {
//                        pathStarted = true;
//                        mMobPattern.add(newPoint);
//                        mPatternLastPoint = new PointF(event.getX(),  event.getY());
//                    }
//                    break;
//
//                default:
//                    break;
//            }
//            return false;
//        }
//    };
