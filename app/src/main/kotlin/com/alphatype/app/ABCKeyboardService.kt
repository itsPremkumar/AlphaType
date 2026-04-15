package com.alphatype.app

import android.inputmethodservice.InputMethodService
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*

class ABCKeyboardService : InputMethodService(), TextToSpeech.OnInitListener {

    private lateinit var keyboardRoot: View
    private lateinit var keyboardContainer: FrameLayout
    private lateinit var learningHint: TextView
    private lateinit var suggestionStrip: LinearLayout
    
    private var tts: TextToSpeech? = null
    private var isCaps = false
    private var currentLayoutId = R.layout.layout_abc
    private var isLearningMode = true // Enabled by default as per objective

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this, this)
    }

    override fun onCreateInputView(): View {
        keyboardRoot = layoutInflater.inflate(R.layout.keyboard_view, null)
        keyboardContainer = keyboardRoot.findViewById(R.id.keyboard_container)
        learningHint = keyboardRoot.findViewById(R.id.learning_hint)
        suggestionStrip = keyboardRoot.findViewById(R.id.suggestion_strip)
        
        loadLayout(currentLayoutId)
        return keyboardRoot
    }

    private fun loadLayout(layoutId: Int) {
        currentLayoutId = layoutId
        keyboardContainer.removeAllViews()
        val layout = layoutInflater.inflate(layoutId, keyboardContainer, false)
        keyboardContainer.addView(layout)
        setupKeys(layout)
    }

    private fun setupKeys(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                if (child is Button) {
                    child.setOnClickListener { onKeyClick(child) }
                } else if (child is ViewGroup) {
                    setupKeys(child)
                }
            }
        }
    }

    private fun onKeyClick(button: Button) {
        button.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
        val text = button.text.toString()
        val ic = currentInputConnection ?: return

        when (text) {
            "⌫" -> ic.deleteSurroundingText(1, 0)
            "⇧" -> toggleCaps()
            getString(R.string.key_enter) -> ic.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_ENTER))
            getString(R.string.key_space) -> ic.commitText(" ", 1)
            getString(R.string.key_abc) -> loadLayout(R.layout.layout_abc)
            getString(R.string.key_123) -> loadLayout(R.layout.layout_numeric)
            "TA" -> loadLayout(R.layout.layout_tamil)
            "QW" -> loadLayout(R.layout.layout_qwerty)
            else -> {
                var code = text
                if (isCaps) code = code.uppercase() else code = code.lowercase()
                ic.commitText(code, 1)
                speakKey(code)
                showLearningHint(code)
            }
        }
    }

    private fun toggleCaps() {
        isCaps = !isCaps
        updateKeyLabels(keyboardContainer)
    }

    private fun updateKeyLabels(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                if (child is Button) {
                    val text = child.text.toString()
                    if (text.length == 1 && text[0].isLetter()) {
                        child.text = if (isCaps) text.uppercase() else text.lowercase()
                    }
                } else if (child is ViewGroup) {
                    updateKeyLabels(child)
                }
            }
        }
    }

    private fun speakKey(text: String) {
        if (isLearningMode) {
            val hintResId = resources.getIdentifier("hint_${text.lowercase()}", "string", packageName)
            val speechText = if (hintResId != 0) getString(hintResId) else text
            tts?.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    private fun showLearningHint(text: String) {
        if (isLearningMode) {
            val hintResId = resources.getIdentifier("hint_${text.lowercase()}", "string", packageName)
            if (hintResId != 0) {
                suggestionStrip.visibility = View.VISIBLE
                learningHint.text = getString(hintResId)
                // Hide after 2 seconds
                learningHint.postDelayed({
                    suggestionStrip.visibility = View.GONE
                }, 2000)
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
