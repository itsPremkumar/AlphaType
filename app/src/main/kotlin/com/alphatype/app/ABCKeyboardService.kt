package com.alphatype.app

import android.inputmethodservice.InputMethodService
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
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
    private var isLearningMode = false // Disabled for professional/private mode
    
    private var previewPopup: PopupWindow? = null
    private var previewText: TextView? = null
    private val handler = Handler(Looper.getMainLooper())
    private var isDeleting = false
    private val deleteRunnable = object : Runnable {
        override fun run() {
            if (isDeleting) {
                currentInputConnection?.deleteSurroundingText(1, 0)
                handler.postDelayed(this, 50)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this, this)
    }

    override fun onCreateInputView(): View {
        keyboardRoot = layoutInflater.inflate(R.layout.keyboard_view, null)
        keyboardContainer = keyboardRoot.findViewById(R.id.keyboard_container)
        learningHint = keyboardRoot.findViewById(R.id.learning_hint)
        suggestionStrip = keyboardRoot.findViewById(R.id.suggestion_strip)
        
        // Initialize Popup Preview
        val previewView = layoutInflater.inflate(R.layout.key_preview, null)
        previewText = previewView.findViewById(R.id.key_preview_text)
        previewPopup = PopupWindow(previewView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        previewPopup?.isTouchable = false
        previewPopup?.elevation = 8f

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
                if (child is Button || child is android.widget.ImageButton) {
                    child.setOnTouchListener { v, event ->
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                handleKeyDown(v)
                                true
                            }
                            MotionEvent.ACTION_UP -> {
                                handleKeyUp(v)
                                v.performClick()
                                true
                            }
                            MotionEvent.ACTION_CANCEL -> {
                                handleKeyCancel(v)
                                true
                            }
                            else -> false
                        }
                    }
                } else if (child is ViewGroup) {
                    setupKeys(child)
                }
            }
        }
    }

    private fun handleKeyDown(view: View) {
        if (SettingsManager.isVibrationEnabled(this)) {
            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
        }
        view.isPressed = true
        val text = if (view is Button) view.text.toString() else view.tag?.toString() ?: return

        // Continuous Delete
        if (text == "⌫") {
            isDeleting = true
            handler.post(deleteRunnable)
        }
        
        // Key Preview
        if (text.length == 1) {
            var code = text
            if (isCaps && code[0].isLetter()) code = code.uppercase() else code = code.lowercase()
            previewText?.text = code
            val loc = IntArray(2)
            view.getLocationInWindow(loc)
            val popup = previewPopup ?: return
            if (popup.isShowing) {
                popup.update(loc[0], loc[1] - view.height - 40, -1, -1)
            } else {
                popup.showAtLocation(view, android.view.Gravity.NO_GRAVITY, loc[0], loc[1] - view.height - 40)
            }
        }
    }

    private fun handleKeyCancel(view: View) {
        view.isPressed = false
        isDeleting = false
        handler.removeCallbacks(deleteRunnable)
        previewPopup?.dismiss()
    }

    private fun handleKeyUp(view: View) {
        view.isPressed = false
        isDeleting = false
        handler.removeCallbacks(deleteRunnable)
        previewPopup?.dismiss()

        val text = if (view is Button) view.text.toString() else view.tag?.toString() ?: return
        val ic = currentInputConnection ?: return

        when (text) {
            "⌫" -> { /* Deletion is handled continuously in ACTION_DOWN */ }
            "⇧" -> toggleCaps()
            "ENTER", getString(R.string.key_enter) -> ic.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_ENTER))
            "SPACE", getString(R.string.key_space) -> ic.commitText(" ", 1)
            "ABC", getString(R.string.key_abc) -> loadLayout(R.layout.layout_abc)
            "123", getString(R.string.key_123) -> loadLayout(R.layout.layout_numeric)
            "TA" -> loadLayout(R.layout.layout_tamil)
            "QW" -> loadLayout(R.layout.layout_qwerty)
            "GLOBE" -> cycleLayout()
            else -> {
                var code = text
                if (isCaps && code.length == 1 && code[0].isLetter()) code = code.uppercase() else code = code.lowercase()
                ic.commitText(code, 1)
                speakKey(code)
                showLearningHint(code)
            }
        }
    }

    private fun cycleLayout() {
        when (currentLayoutId) {
            R.layout.layout_abc -> loadLayout(R.layout.layout_qwerty)
            R.layout.layout_qwerty -> loadLayout(R.layout.layout_tamil)
            else -> loadLayout(R.layout.layout_abc)
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
        if (!SettingsManager.isVoiceEnabled(this)) return
        
        if (tts == null) {
            tts = TextToSpeech(this, this)
        }

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
