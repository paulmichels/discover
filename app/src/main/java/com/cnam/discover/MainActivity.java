package com.cnam.discover;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cnam.discover.adapter.PersonAdapter;
import com.cnam.discover.dto.TestDto;
import com.cnam.discover.observer.IdentifiedObserver;
import com.cnam.discover.service.DiscoverService;
import com.vuzix.hud.actionmenu.ActionMenuActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends ActionMenuActivity implements Observer {

    private static final int CAMERA_REQUEST_CODE = 100;
    private List<IPerson> testPerson;
    private List<IPerson> testPersonPrintable = new ArrayList<>();
    private RecyclerView recyclerView;
    private PersonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        testList();
        IdentifiedObserver.getInstance().addObserver(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PersonAdapter(testPersonPrintable);
        recyclerView.setAdapter(adapter);

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
        final List<IPerson> people = (List<IPerson>) o;
        final Activity activity = this;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPersonPrintable.clear();
                for (IPerson person : people){
                    for (IPerson base : testPerson){
                        if(base.compareTo(person) == 0){
                            testPersonPrintable.add(base);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void testList(){
        testPerson = new ArrayList<>();
        TestDto paul = new TestDto();
        paul.setLastName("Michels");
        paul.setPredictedLabel("Paul");
        paul.setAge("25");
        paul.setDescription("CNAM");
        paul.setGender("Homme");
        paul.setProfilePic("https://scontent-mrs2-1.xx.fbcdn.net/v/t1.0-9/71185963_144616026790586_5627281041870815232_n.jpg?_nc_cat=101&_nc_oc=AQkxUoz0yDbdP-oOMcXa_HCnzcN2TIvXS7g4o6gkOOLsHe_QeKUavWZbZazGv28jl7I&_nc_ht=scontent-mrs2-1.xx&oh=94d86df532e9537f6ab4e776abbc5289&oe=5E4E371D");
        TestDto ludo = new TestDto();
        ludo.setLastName("Febvre");
        ludo.setPredictedLabel("Ludovic");
        ludo.setAge("22");
        ludo.setDescription("CNAM");
        ludo.setGender("Homme");
        ludo.setProfilePic("https://media.licdn.com/dms/image/C5603AQERyhE7MRxyLQ/profile-displayphoto-shrink_800_800/0?e=1579737600&v=beta&t=Ccmg1djbKDAi527Fx0FRhUvsSJGLXMG487-pGfJ4MCg");
        TestDto nico = new TestDto();
        nico.setLastName("Andres");
        nico.setPredictedLabel("Nicolas");
        nico.setAge("21");
        nico.setDescription("CNAM");
        nico.setGender("Homme");
        nico.setProfilePic("https://scontent-mrs2-1.cdninstagram.com/vp/e281e3d610e1500c21866c184f138c97/5E8427E3/t51.2885-19/s320x320/72875379_510984696417591_896398376625504256_n.jpg?_nc_ht=scontent-mrs2-1.cdninstagram.com");
        TestDto thomas = new TestDto();
        thomas.setLastName("Arnoux");
        thomas.setPredictedLabel("Thomas");
        thomas.setAge("21");
        thomas.setDescription("CNAM");
        thomas.setGender("Homme");
        thomas.setProfilePic("https://scontent-mrs2-1.xx.fbcdn.net/v/t1.0-9/17457723_1667492696887947_8491364541328773525_n.jpg?_nc_cat=102&_nc_oc=AQkkbJv0vTDPzKNOzTOkizDBLIug2AJwy9Lgw6XT05GFNkiJu7V5POL5rzKLbig96ss&_nc_ht=scontent-mrs2-1.xx&oh=364cee7284049329b98f34dd8cb1c6b0&oe=5E437746");
        testPerson.add(paul);
        testPerson.add(ludo);
        testPerson.add(nico);
        testPerson.add(thomas);
    }
}