package com.cnam.discover;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cnam.discover.interfaces.IPerson;
import com.cnam.discover.observer.IdentifiedObserver;
import com.cnam.discover.service.DiscoverService;
import com.squareup.picasso.Picasso;
import com.vuzix.hud.actionmenu.ActionMenuActivity;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends ActionMenuActivity implements Observer {

    private static final int CAMERA_REQUEST_CODE = 100;
    private View popUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FrameLayout mFrame = findViewById(R.id.mainFrame);
        popUp = getLayoutInflater().inflate(R.layout.pop_up, null);
        popUp.setVisibility(View.GONE);
        mFrame.addView(popUp);


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

    /*
    @Override
    public void update(Observable observable, Object o) {
        final List<IPerson> people = (List<IPerson>) o;
        final Activity activity = this;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                popUp.setVisibility(View.VISIBLE);

                ImageView profilePicture = findViewById(R.id.profile_picture);
                Picasso.get().load(people.get(0).getProfilePicUrl()).into(profilePicture);

                TextView lastName = findViewById(R.id.last_name);
                lastName.setText(people.get(0).getLastName());

                TextView firstName = findViewById(R.id.first_name);
                firstName.setText(people.get(0).getFirstName());

                TextView gender = findViewById(R.id.gender_value);
                gender.setText(people.get(0).getGender());

                TextView age = findViewById(R.id.age_value);
                age.setText(people.get(0).getAge());

                TextView description = findViewById(R.id.description_value);
                description.setText(people.get(0).getDescription());
            }
        });
    }
*/

    @Override
    public void update(Observable observable, Object o) {
        final IPerson person = (IPerson) o;
        final Activity activity = this;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                popUp.setVisibility(View.VISIBLE);

                if(person != null) {
                    ImageView profilePicture = findViewById(R.id.profile_picture);
                    Picasso.get().load(person.getProfilePicUrl()).into(profilePicture);

                    TextView lastName = findViewById(R.id.last_name);
                    lastName.setText(person.getLastName());

                    TextView firstName = findViewById(R.id.first_name);
                    firstName.setText(person.getFirstName());

                    TextView gender = findViewById(R.id.gender_value);
                    gender.setText(person.getGender());

                    TextView age = findViewById(R.id.age_value);
                    age.setText(person.getAge());

                    TextView description = findViewById(R.id.description_value);
                    description.setText(person.getDescription());
                } else {
                    popUp.setVisibility(View.GONE);
                }
            }
        });
    }
}