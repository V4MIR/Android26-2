package ru.urfu.droidpractice1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import coil.load
import ru.urfu.droidpractice1.databinding.ActivitySecondBinding

class SecondActivity : ComponentActivity() {

    companion object {
        private const val TAG = "SecondActivity"
        private const val IMAGE_URL =
            "https://rock-history.ru/upload/000/u1/34/68/scorpions-istorija-gruppy-photo-big.jpg"
    }

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setReadResult(binding.switchRead.isChecked)
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        })

        binding.articleImage.load(IMAGE_URL)

        val isRead = savedInstanceState
            ?.getBoolean(MainActivity.EXTRA_IS_READ)
            ?: intent.getBooleanExtra(MainActivity.EXTRA_IS_READ, false)

        binding.switchRead.isChecked = isRead
        binding.switchRead.setOnCheckedChangeListener { _, checked ->
            setReadResult(checked)
        }
        setReadResult(isRead)
    }

    private fun setReadResult(isRead: Boolean) {
        setResult(
            RESULT_OK,
            Intent().putExtra(MainActivity.EXTRA_IS_READ, isRead),
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(MainActivity.EXTRA_IS_READ, binding.switchRead.isChecked)
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }
    override fun onRestart() { super.onRestart(); Log.d(TAG, "onRestart") }
}