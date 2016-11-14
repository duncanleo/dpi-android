package com.duncan.dpi.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.duncan.dpi.R
import com.duncan.dpi.model.Device
import com.duncan.dpi.util.CalcUtil
import com.jakewharton.rxbinding.widget.RxTextView
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent
import kotlinx.android.synthetic.main.activity_main.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    internal val DEVICE_LIST_REQUEST_CODE = 9999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filter = Func1<TextViewTextChangeEvent, Boolean> { event -> event.text().isNotEmpty() }
        val action = Action1<TextViewTextChangeEvent> { event -> attemptCalculation() }
        RxTextView.textChangeEvents(inputWidth.editText!!)
                .debounce(700, TimeUnit.MILLISECONDS)
                .filter(filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action)
        RxTextView.textChangeEvents(inputHeight.editText!!)
                .debounce(700, TimeUnit.MILLISECONDS)
                .filter(filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action)
        RxTextView.textChangeEvents(inputScreenSize.editText!!)
                .debounce(700, TimeUnit.MILLISECONDS)
                .filter(filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action)
    }

    /**
     * Attempt to calculate DPI
     */
    private fun attemptCalculation() {
        val width = try { inputWidth.editText?.text.toString().toInt() } catch (e: NumberFormatException) { 0 }
        val height = try { inputHeight.editText?.text.toString().toInt() } catch (e: NumberFormatException) { 0 }
        val screenSize = try { inputScreenSize.editText?.text.toString().toDouble() } catch (e: NumberFormatException) { 0.0 }
        labelDensity.text = "${CalcUtil.calculateDPI(width, height, screenSize)} dpi"

        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels

        val maxWidth = screenWidth * 0.5
        val maxHeight = screenHeight * 0.6

        val params = imageScreen.layoutParams

        // Attempt maxWidth
        params.width = maxWidth.toInt()
        val intendedHeight = maxWidth / width * height
        if (intendedHeight > maxHeight) {
            // Use maxWidth
            val intendedWidth = maxHeight / height * width
            params.width = intendedWidth.toInt()
            params.height = maxHeight.toInt()
        } else {
            params.height = intendedHeight.toInt()
        }

        imageScreen.layoutParams = params
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("width", inputWidth.editText?.text.toString())
        outState?.putString("height", inputHeight.editText?.text.toString())
        outState?.putString("screenSize", inputScreenSize.editText?.text.toString())
        outState?.putString("result", labelDensity.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        inputWidth.editText?.setText(savedInstanceState?.getString("width"))
        inputHeight.editText?.setText(savedInstanceState?.getString("height"))
        inputScreenSize.editText?.setText(savedInstanceState?.getString("screenSize"))
        labelDensity.text = savedInstanceState?.getString("result")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.deviceList -> {
                //Since seen before, no need to show again.
                val intent = Intent(this@MainActivity, DeviceListActivity::class.java)
                startActivityForResult(intent, DEVICE_LIST_REQUEST_CODE)
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DEVICE_LIST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val device = data?.getParcelableExtra<Device>("device")
            inputWidth.editText?.setText("${device?.width}")
            inputHeight.editText?.setText("${device?.height}")
            inputScreenSize.editText?.setText(String.format("%.2f", device?.screenSize))
            attemptCalculation()
            Snackbar.make(imageScreen, String.format("%s %s", getString(R.string.message_populate), device?.name), Snackbar.LENGTH_LONG)
                    .show()
        }
    }
}
