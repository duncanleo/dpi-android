package com.duncan.dpi;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;


public class MainActivity extends ActionBarActivity {
    Toolbar toolbar;
    DynamicViewController controller;
    MaterialEditText width, height, screenSize;
    TextView answer;
    final int DEVICE_LIST_REQUEST_CODE = 9999;
    boolean viewedDeviceListDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Vars
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        this.controller = new DynamicViewController(findViewById(R.id.dynamicView));
        this.width = (MaterialEditText)findViewById(R.id.width);
        this.height = (MaterialEditText)findViewById(R.id.height);
        this.screenSize = (MaterialEditText)findViewById(R.id.screenSize);
        this.answer = (TextView)findViewById(R.id.answer);

        //Setup
        setSupportActionBar(toolbar);

        setupTextWatchers();
    }

    /**
     * Setup the view_dynamic view to reflect texts as they are typed
     */
    private void setupTextWatchers() {
        TextWatcher attemptCalcOnTextChange = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                attemptCalculation();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        this.width.addTextChangedListener(attemptCalcOnTextChange);
        this.height.addTextChangedListener(attemptCalcOnTextChange);
        this.screenSize.addTextChangedListener(attemptCalcOnTextChange);
    }

    /**
     * Attempt to calculate DPI
     */
    private void attemptCalculation() {
        int w, h;
        double ss;
        try {
            w = Integer.parseInt(width.getText().toString());
            h = Integer.parseInt(height.getText().toString());
            ss = Double.parseDouble(screenSize.getText().toString());
            controller.setWidth(w);
            controller.setHeight(h);
            controller.setScreenSize(ss);
            answer.setText(CalcUtil.calculateDPI(w, h, ss) + " dpi");
        } catch (Exception e) {
            controller.setWidth(-1);
            controller.setHeight(-1);
            controller.setScreenSize(-1);
            answer.setText("- dpi");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.deviceList) {
            if (!viewedDeviceListDialog) {
                new MaterialDialog.Builder(this).title(R.string.device_list).content(R.string.dialog_content)
                        .positiveText(R.string.dialog_agree).negativeText(R.string.dialog_cancel).callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        Intent intent = new Intent(MainActivity.this,  DeviceListActivity.class);
                        startActivityForResult(intent, DEVICE_LIST_REQUEST_CODE);
                    }
                }).show();
                viewedDeviceListDialog = true;
            } else {
                //Since seen before, no need to show again.
                Intent intent = new Intent(MainActivity.this,  DeviceListActivity.class);
                startActivityForResult(intent, DEVICE_LIST_REQUEST_CODE);
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DEVICE_LIST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Device device = data.getParcelableExtra("device");
            width.setText("" + device.getScreenWidth());
            height.setText("" + device.getScreenHeight());
            screenSize.setText(String.format("%.2f", device.getScreenSize()));
            attemptCalculation();
            Toast.makeText(MainActivity.this, "Inserted data from " + device.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }
}
