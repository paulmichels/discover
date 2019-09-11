package com.cnam.vuzix;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.hud.actionmenu.ActionMenuActivity;

public class MainActivity extends ActionMenuActivity {

    private TextView mainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected boolean onCreateActionMenu(Menu menu) {
        super.onCreateActionMenu(menu);

        getMenuInflater().inflate(R.menu.menu, menu);
        mainText = findViewById(R.id.mainTextView);
        return true;
    }

    //A mettre a false quand on aura les lunettes
    @Override
    protected boolean alwaysShowActionMenu() {
        return true;
    }

    public void showItem1(MenuItem item){
        showToast("Hello World!");
        mainText.setText("Hello World!");
    }

    public void showItem2(MenuItem item){
        showToast("Vuzix!");
        mainText.setText("Vuzix!");
    }

    public void showItem3(MenuItem item){
        showToast("Blade");
        mainText.setText("Blade");
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
}
