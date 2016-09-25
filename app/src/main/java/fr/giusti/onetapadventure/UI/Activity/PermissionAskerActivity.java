package fr.giusti.onetapadventure.UI.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import fr.giusti.onetapadventure.R;

/**
 * Created by jérémy on 25/09/2016.
 */

public abstract class PermissionAskerActivity extends Activity {
    private final static int PERMISSION_ASKING = 634;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askForPermission();
    }

    private void askForPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int permissionCheck1 = this.checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionCheck2 = this.checkSelfPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permissionCheck1 == PackageManager.PERMISSION_DENIED || 2 == PackageManager.PERMISSION_DENIED) {
                this.requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_ASKING);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        boolean allPermissionGranted = true;
        switch (requestCode) {
            case PERMISSION_ASKING: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result == PackageManager.PERMISSION_DENIED) {
                            allPermissionGranted = false;
                        }
                    }
                } else {
                    allPermissionGranted = false;
                }
            }
        }
        if (!allPermissionGranted) {
            Toast.makeText(this, R.string.all_permissions_needed, Toast.LENGTH_LONG).show();
        }
    }
}
