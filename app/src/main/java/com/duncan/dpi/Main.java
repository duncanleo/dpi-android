package com.duncan.dpi;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Main extends ActionBarActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private List<TextView> choices = new ArrayList<TextView>();
    private static ConvToDPIFragment convToDPIFragment;
    private static ConvFromDPIFragment convFromDPIFragment;
    private static MyDeviceFragment myDeviceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        choices.add((TextView)drawerLayout.findViewById(R.id.choice_todpi));
        choices.add((TextView)drawerLayout.findViewById(R.id.choice_fromdpi));
        choices.add((TextView)drawerLayout.findViewById(R.id.choice_common));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,  drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.app_name
        );
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
        initFragments();

        //Clickers
        setupChoiceClickers();
        choices.get(0).performClick();
    }

    private void initFragments() {
        convFromDPIFragment = new ConvFromDPIFragment();
        convToDPIFragment = new ConvToDPIFragment();
        myDeviceFragment = new MyDeviceFragment();
    }

    private void setupChoiceClickers() {
        for (int a = 0; a < choices.size(); a++) {
            final TextView tv = choices.get(a);
            final int index = a;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv.setTextColor(Color.parseColor("#03A9F4"));
                    for (int i = 0; i < choices.size(); i++) {
                        if (i != index) {
                            choices.get(i).setTextColor(Color.BLACK);
                        }
                    }
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    toolbar.setTitle(tv.getText());
                    FragmentManager fm = getFragmentManager();
                    final FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right, 0, 0);
                    ft.addToBackStack("tag");
                    if (index == 0) {
                        ft.replace(R.id.content_frame, convToDPIFragment);
                    } else if (index == 1) {
                        ft.replace(R.id.content_frame, convFromDPIFragment);
                    } else if (index == 2) {
                        ft.replace(R.id.content_frame, myDeviceFragment);
                    }
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            try {
                                Thread.sleep(200);
                            } catch (Exception g) {}
                            tv.post(new Runnable() {
                                @Override
                                public void run() {
                                    ft.commit();
                                }
                            });
                        }
                    });
                    thread.start();
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
