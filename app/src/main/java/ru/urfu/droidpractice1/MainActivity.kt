package ru.urfu.droidpractice1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ru.urfu.droidpractice1.content.MainActivityScreen

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
        const val EXTRA_IS_READ = "extra_is_read"
        private const val KEY_STATE = "key_state"
    }

    private data class UiState(
        val isSecondArticleRead: Boolean = false,
        val likes: Int = 0,
        val dislikes: Int = 0,
    ) {
        fun toBundle(b: Bundle) = b.apply {
            putBoolean(KEY_STATE + "_read", isSecondArticleRead)
            putInt(KEY_STATE + "_likes", likes)
            putInt(KEY_STATE + "_dislikes", dislikes)
        }

        companion object {
            fun from(b: Bundle?) = UiState(
                isSecondArticleRead = b?.getBoolean(KEY_STATE + "_read") ?: false,
                likes = b?.getInt(KEY_STATE + "_likes") ?: 0,
                dislikes = b?.getInt(KEY_STATE + "_dislikes") ?: 0,
            )
        }
    }

    private var uiState by mutableStateOf(UiState())

    private val secondLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            uiState = uiState.copy(
                isSecondArticleRead = result.data
                    ?.getBooleanExtra(EXTRA_IS_READ, false) == true
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        uiState = UiState.from(savedInstanceState)

        setContent {
            MainActivityScreen(
                isSecondArticleRead = uiState.isSecondArticleRead,
                initialLikes = uiState.likes,
                initialDislikes = uiState.dislikes,
                onShare = ::shareArticle,
                onOpenSecond = ::openSecondArticle,
            )
        }
    }

    private fun shareArticle(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.share_article)))
    }

    private fun openSecondArticle(likes: Int, dislikes: Int) {
        uiState = uiState.copy(likes = likes, dislikes = dislikes)
        val intent = Intent(this, SecondActivity::class.java).apply {
            putExtra(EXTRA_IS_READ, uiState.isSecondArticleRead)
        }
        secondLauncher.launch(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        uiState.toBundle(outState)
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }
    override fun onRestart() { super.onRestart(); Log.d(TAG, "onRestart") }
}