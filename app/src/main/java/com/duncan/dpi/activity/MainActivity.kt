package com.duncan.dpi.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.duncan.dpi.R
import com.duncan.dpi.databinding.ActivityMainBinding
import com.duncan.dpi.model.Device
import com.duncan.dpi.util.CalcUtil
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding.widget.RxTextView
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    internal val DEVICE_LIST_REQUEST_CODE = 9999
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val filter = Func1<TextViewTextChangeEvent, Boolean> { event -> event.text().isNotEmpty() }
        val action = Action1<TextViewTextChangeEvent> { event -> attemptCalculation() }
        RxTextView.textChangeEvents(binding.inputWidth.editText!!)
                .debounce(700, TimeUnit.MILLISECONDS)
                .filter(filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action)
        RxTextView.textChangeEvents(binding.inputHeight.editText!!)
                .debounce(700, TimeUnit.MILLISECONDS)
                .filter(filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action)
        RxTextView.textChangeEvents(binding.inputScreenSize.editText!!)
                .debounce(700, TimeUnit.MILLISECONDS)
                .filter(filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action)
    }

    /**
     * Attempt to calculate DPI
     */
    private fun attemptCalculation() {
        val width = try { binding.inputWidth.editText?.text.toString().toInt() } catch (e: NumberFormatException) { 0 }
        val height = try { binding.inputHeight.editText?.text.toString().toInt() } catch (e: NumberFormatException) { 0 }
        val screenSize = try { binding.inputScreenSize.editText?.text.toString().toDouble() } catch (e: NumberFormatException) { 0.0 }
        binding.labelDensity.text = "${CalcUtil.calculateDPI(width, height, screenSize)} dpi"

        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels

        val maxWidth = screenWidth * 0.5
        val maxHeight = screenHeight * 0.6

        val params = binding.imageScreen.layoutParams

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

        binding.imageScreen.layoutParams = params
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("width", binding.inputWidth.editText?.text.toString())
        outState.putString("height", binding.inputHeight.editText?.text.toString())
        outState.putString("screenSize", binding.inputScreenSize.editText?.text.toString())
        outState.putString("result", binding.labelDensity.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.inputWidth.editText?.setText(savedInstanceState.getString("width"))
        binding.inputHeight.editText?.setText(savedInstanceState.getString("height"))
        binding.inputScreenSize.editText?.setText(savedInstanceState.getString("screenSize"))
        binding.labelDensity.text = savedInstanceState.getString("result")
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
            binding.inputWidth.editText?.setText("${device?.width}")
            binding.inputHeight.editText?.setText("${device?.height}")
            binding.inputScreenSize.editText?.setText(String.format("%.2f", device?.screenSize))
            attemptCalculation()
            Snackbar.make(binding.imageScreen, String.format("%s %s", getString(R.string.message_populate), device?.name), Snackbar.LENGTH_LONG)
                    .show()
        }
    }
}
