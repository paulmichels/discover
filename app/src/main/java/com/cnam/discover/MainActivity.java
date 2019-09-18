package com.cnam.discover;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.cnam.discover.api.ApiRequest;
import com.cnam.discover.api.ApiResponseParser;
import com.cnam.discover.api.ApiSingleton;
import com.cnam.discover.dto.IdentificationDto;
import com.cnam.discover.service.CameraService;
import com.vuzix.hud.actionmenu.ActionMenuActivity;

import org.json.JSONObject;


public class MainActivity extends ActionMenuActivity{

    private static final int CAMERA_REQUEST_CODE = 100;
    public static ImageView imageView;

    private DataUpdateReceiver dataUpdateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            startCameraService();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dataUpdateReceiver == null) {
            RequestQueue requestQueue = ApiSingleton.getInstance(getContext()).getRequestQueue();
            ApiRequest apiRequest = new ApiRequest(requestQueue, getContext());
            dataUpdateReceiver = new DataUpdateReceiver(apiRequest, requestQueue);
        }
        IntentFilter intentFilter = new IntentFilter(CameraService.REFRESH_DATA_INTENT);
        registerReceiver(dataUpdateReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dataUpdateReceiver != null) {
            unregisterReceiver(dataUpdateReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, CameraService.class));
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

    //TODO afficher seulement sur commande de l'utilisateur, à true pour le test, mettre à false pour le deploiement
    @Override
    protected boolean alwaysShowActionMenu() {
        return false;
    }

    public void settingsItem(MenuItem item){
        showToast("Settings");
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

    private void startCameraService(){
        Intent intent = new Intent(MainActivity.this, CameraService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);
    }

    private class DataUpdateReceiver extends BroadcastReceiver {

        private ApiRequest apiRequest;
        private RequestQueue requestQueue;

        public DataUpdateReceiver(ApiRequest apiRequest, RequestQueue requestQueue) {
            this.apiRequest = apiRequest;
            this.requestQueue = requestQueue;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CameraService.REFRESH_DATA_INTENT)) {
                String image = intent.getStringExtra(CameraService.BASE_64_BITMAP);
                apiRequest.apiPostImage(ApiRequest.GET_IDENTIFICATION, image, new ApiRequest.apiCallback() {
                    @Override
                    public void onSuccess(Context context, JSONObject jsonObject) {
                        IdentificationDto identificationDto = ApiResponseParser.parseIdentification(jsonObject);
                    }

                    @Override
                    public void onError(Context context, String message) {
                        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}