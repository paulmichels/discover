package com.cnam.discover;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cnam.discover.interfaces.IIdentified;
import com.cnam.discover.observer.IdentifiedObserver;
import com.cnam.discover.service.DiscoverService;
import com.vuzix.hud.actionmenu.ActionMenuActivity;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends ActionMenuActivity implements Observer {

    private static final int CAMERA_REQUEST_CODE = 100;
    private FrameLayout mFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mFrame = findViewById(R.id.mainFrame);

        IdentifiedObserver.getInstance().addObserver(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            startCameraService();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(MainActivity.this, DiscoverService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, DiscoverService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                startCameraService();
            } else {
                Toast.makeText(this, "Camera permission denied, exiting application", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected boolean onCreateActionMenu(Menu menu) {
        super.onCreateActionMenu(menu);

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected boolean alwaysShowActionMenu() {
        return false;
    }

    private void startCameraService(){
        Intent intent = new Intent(MainActivity.this, DiscoverService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);
    }

    public void pauseItem(MenuItem item){
        showToast("Pause");
        item.setIcon(android.R.drawable.ic_media_pause);
    }

    public void playItem(MenuItem item){
        showToast("Play");
        item.setIcon(android.R.drawable.ic_media_play);
    }

    private void showToast(final String text){
        final Activity activity = this;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void update(Observable observable, Object o) {
        final List<IIdentified> identified = (List<IIdentified>) o;
        final Activity activity = this;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, identified.get(0).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}