package com.qull.lib.loaddeviceinfo;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.qull.lib.mylib.PermissionManager;
import com.qull.lib.mylib.phonesms.Readsms;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List<AppInfo> lists= ApplicationUtil.newInstance(MainActivity.this).loadAllApp();
                PermissionManager.sharedInstance().requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, PermissionManager.RequestCodeSMS, new PermissionManager.Listener() {
                    @Override
                    public void onGranted(int requestCode) {
                        Readsms readsms=new Readsms();
                        readsms.getSmsFromPhone(MainActivity.this);
                    }

                    @Override
                    public void onDenied(int requestCode) {

                    }

                    @Override
                    public void onAlwaysDenied(int requestCode, List<String> permissions) {

                    }
                });

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
