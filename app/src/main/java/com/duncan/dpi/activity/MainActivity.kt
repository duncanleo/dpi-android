package com.duncan.dpi.activity


import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView

import com.duncan.dpi.util.CalcUtil
import com.duncan.dpi.model.Device
import com.duncan.dpi.view.DynamicViewController
import com.duncan.dpi.R

class MainActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var controller: DynamicViewController
    lateinit var widthContainer: TextInputLayout
    lateinit var heightContainer: TextInputLayout
    lateinit var screenSizeContainer: TextInputLayout
    lateinit var width: EditText
    lateinit var height: EditText
    lateinit var screenSize: EditText
    lateinit var answer: TextView
    internal val DEVICE_LIST_REQUEST_CODE = 9999
    internal var viewedDeviceListDialog = false

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Vars
        toolbar = findViewById(R.id.toolbar) as Toolbar

        this.controller = DynamicViewController(findViewById(R.id.dynamicView))
        this.answer = findViewById(R.id.answer) as TextView

        this.widthContainer = findViewById(R.id.widthContainer) as TextInputLayout
        this.heightContainer = findViewById(R.id.heightContainer) as TextInputLayout
        this.screenSizeContainer = findViewById(R.id.screenSizeContainer) as TextInputLayout

        this.width = this.widthContainer.editText!!
        this.height = this.heightContainer.editText!!
        this.screenSize = this.screenSizeContainer.editText!!

        //Setup
        setSupportActionBar(toolbar)
        /*this.widthContainer.setErrorEnabled(true);
        this.heightContainer.setErrorEnabled(true);
        this.screenSizeContainer.setErrorEnabled(true);*/

        setupTextWatchers()
    }

    /**
     * Setup the view_dynamic view to reflect texts as they are typed
     */
    private fun setupTextWatchers() {
        val attemptCalcOnTextChange = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                attemptCalculation()
            }

            override fun afterTextChanged(s: Editable) {
            }
        }
        this.width.addTextChangedListener(attemptCalcOnTextChange)
        this.height.addTextChangedListener(attemptCalcOnTextChange)
        this.screenSize.addTextChangedListener(attemptCalcOnTextChange)
    }

    /**
     * Attempt to calculate DPI
     */
    private fun attemptCalculation() {
        val w: Int
        val h: Int
        val ss: Double
        try {
            w = Integer.parseInt(width.text.toString())
            h = Integer.parseInt(height.text.toString())
            ss = java.lang.Double.parseDouble(screenSize.text.toString())
            controller.setWidth(w)
            controller.setHeight(h)
            controller.setScreenSize(ss)
            answer.text = CalcUtil.calculateDPI(w, h, ss).toString() + " dpi"
        } catch (e: Exception) {
            controller.setWidth(-1)
            controller.setHeight(-1)
            controller.setScreenSize(-1.0)
            answer.text = "- dpi"
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deviceList) {
            if (!viewedDeviceListDialog) {
                val builder = AlertDialog.Builder(this@MainActivity, R.style.DialogTheme)
                builder.setMessage(R.string.dialog_content)
                        .setTitle(R.string.device_list)
                        .setPositiveButton(R.string.dialog_agree) { dialogInterface, i ->
                            viewedDeviceListDialog = true
                            val intent = Intent(this@MainActivity, DeviceListActivity::class.java)
                            startActivityForResult(intent, DEVICE_LIST_REQUEST_CODE)
                        }
                        .setNegativeButton(R.string.dialog_cancel, null)
                val dialog = builder.create()
                dialog.show()
            } else {
                //Since seen before, no need to show again.
                val intent = Intent(this@MainActivity, DeviceListActivity::class.java)
                startActivityForResult(intent, DEVICE_LIST_REQUEST_CODE)
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DEVICE_LIST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val device = data.getParcelableExtra<Device>("device")
            width.setText("" + device.screenWidth)
            height.setText("" + device.screenHeight)
            screenSize.setText(String.format("%.2f", device.screenSize))
            attemptCalculation()
            Snackbar.make(width, String.format("%s %s", getString(R.string.message_populate), device.title), Snackbar.LENGTH_LONG)
                    .show()
            //Toast.makeText(MainActivity.this, "Inserted data from " + device.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }
}
