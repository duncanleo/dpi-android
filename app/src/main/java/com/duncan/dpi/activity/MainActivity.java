package com.duncan.dpi.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.duncan.dpi.util.CalcUtil;
import com.duncan.dpi.model.Device;
import com.duncan.dpi.view.DynamicViewController;
import com.duncan.dpi.R;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    DynamicViewController controller;
    TextInputLayout widthContainer, heightContainer, screenSizeContainer;
    EditText width, height, screenSize;
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
        this.answer = (TextView)findViewById(R.id.answer);

        this.widthContainer = (TextInputLayout)findViewById(R.id.widthContainer);
        this.heightContainer = (TextInputLayout)findViewById(R.id.heightContainer);
        this.screenSizeContainer = (TextInputLayout)findViewById(R.id.screenSizeContainer);

        this.width = this.widthContainer.getEditText();
        this.height = this.heightContainer.getEditText();
        this.screenSize = this.screenSizeContainer.getEditText();

        //Setup
        setSupportActionBar(toolbar);
        /*this.widthContainer.setErrorEnabled(true);
        this.heightContainer.setErrorEnabled(true);
        this.screenSizeContainer.setErrorEnabled(true);*/

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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogTheme);
                builder.setMessage(R.string.dialog_content)
                        .setTitle(R.string.device_list)
                        .setPositiveButton(R.string.dialog_agree, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                viewedDeviceListDialog = true;
                                Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);
                                startActivityForResult(intent, DEVICE_LIST_REQUEST_CODE);
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();
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
            Snackbar.make(width, String.format("%s %s", getString(R.string.message_populate), device.getTitle()), Snackbar.LENGTH_LONG)
                    .show();
            //Toast.makeText(MainActivity.this, "Inserted data from " + device.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }
}
