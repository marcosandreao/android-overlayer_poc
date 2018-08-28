package br.com.uarini.poc.overlay;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.uarini.poc.overlay.service.OverLayerService;

public class MainActivity extends AppCompatActivity {

    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.requestPermissionOrStartService();
    }

    private void startOverLayerWindow() {
        this.startService(new Intent(this, OverLayerService.class));
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                this.startOverLayerWindow();
            } else {
                // TODO:
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionOrStartService() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intentSettings = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intentSettings.setData(Uri.parse("package:" + this.getPackageName()));
            this.startActivityForResult(intentSettings, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
        } else {
            this.startOverLayerWindow();
        }
    }

}