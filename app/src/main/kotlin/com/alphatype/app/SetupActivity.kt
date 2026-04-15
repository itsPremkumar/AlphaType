package com.alphatype.app

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SetupActivity : AppCompatActivity() {

    private lateinit var statusBadge: TextView
    private lateinit var cardStep1: LinearLayout
    private lateinit var cardStep2: LinearLayout
    private lateinit var cardStep3: LinearLayout
    private lateinit var step1Circle: View
    private lateinit var step2Circle: View
    private lateinit var step3Circle: View
    private lateinit var step1Number: TextView
    private lateinit var step2Number: TextView
    private lateinit var step3Number: TextView
    private lateinit var step1Status: TextView
    private lateinit var step2Status: TextView
    private lateinit var step3Status: TextView
    private lateinit var btnEnable: Button
    private lateinit var btnSetDefault: Button
    private lateinit var testInput: EditText
    private lateinit var bannerDone: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        // Bind views
        statusBadge = findViewById(R.id.status_badge)
        cardStep1 = findViewById(R.id.card_step1)
        cardStep2 = findViewById(R.id.card_step2)
        cardStep3 = findViewById(R.id.card_step3)
        step1Circle = findViewById(R.id.step1_circle)
        step2Circle = findViewById(R.id.step2_circle)
        step3Circle = findViewById(R.id.step3_circle)
        step1Number = findViewById(R.id.step1_number)
        step2Number = findViewById(R.id.step2_number)
        step3Number = findViewById(R.id.step3_number)
        step1Status = findViewById(R.id.step1_status)
        step2Status = findViewById(R.id.step2_status)
        step3Status = findViewById(R.id.step3_status)
        btnEnable = findViewById(R.id.btn_enable_keyboard)
        btnSetDefault = findViewById(R.id.btn_set_default)
        testInput = findViewById(R.id.test_input)
        bannerDone = findViewById(R.id.banner_done)

        // Step 1: Open keyboard enable settings
        btnEnable.setOnClickListener {
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
            startActivity(intent)
        }

        // Step 2: Show input method picker (switch keyboard)
        btnSetDefault.setOnClickListener {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showInputMethodPicker()
        }

        // Step 3: Focus the test input to bring up keyboard
        testInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                refreshStatus()
            }
        }

        refreshStatus()
    }

    override fun onResume() {
        super.onResume()
        refreshStatus()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            refreshStatus()
        }
    }

    private fun refreshStatus() {
        val isEnabled = isKeyboardEnabled()
        val isDefault = isKeyboardDefault()

        // ── Step 1 ──
        if (isEnabled) {
            markStepDone(cardStep1, step1Circle, step1Number, step1Status)
            btnEnable.text = getString(R.string.setup_btn_enabled)
            btnEnable.background = getDrawable(R.drawable.btn_secondary)
            btnEnable.setTextColor(0xFF00E5FF.toInt())
        } else {
            markStepPending(cardStep1, step1Circle, step1Number, step1Status)
            btnEnable.text = getString(R.string.setup_btn_enable)
            btnEnable.background = getDrawable(R.drawable.btn_primary)
            btnEnable.setTextColor(0xFF0D0D1A.toInt())
        }

        // ── Step 2 ──
        if (isDefault) {
            markStepDone(cardStep2, step2Circle, step2Number, step2Status)
            btnSetDefault.text = getString(R.string.setup_btn_active)
            btnSetDefault.background = getDrawable(R.drawable.btn_secondary)
            btnSetDefault.setTextColor(0xFF00E5FF.toInt())
        } else if (isEnabled) {
            markStepActive(step2Circle, step2Number)
            btnSetDefault.text = getString(R.string.setup_btn_switch)
            btnSetDefault.background = getDrawable(R.drawable.btn_primary)
            btnSetDefault.setTextColor(0xFF0D0D1A.toInt())
        } else {
            markStepPending(cardStep2, step2Circle, step2Number, step2Status)
        }

        // ── Step 3 ──
        if (isDefault) {
            markStepActive(step3Circle, step3Number)
            testInput.isEnabled = true
        } else {
            markStepPending(cardStep3, step3Circle, step3Number, step3Status)
            testInput.isEnabled = false
        }

        // ── Overall status ──
        if (isEnabled && isDefault) {
            statusBadge.text = getString(R.string.setup_status_ready)
            statusBadge.setTextColor(0xFF00E5FF.toInt())
            bannerDone.visibility = View.VISIBLE
        } else {
            statusBadge.text = getString(R.string.setup_status_pending)
            statusBadge.setTextColor(0xFFFF6B6B.toInt())
            bannerDone.visibility = View.GONE
        }
    }

    private fun isKeyboardEnabled(): Boolean {
        val enabledIMEs = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_INPUT_METHODS
        ) ?: return false
        return enabledIMEs.contains("$packageName/.ABCKeyboardService") ||
               enabledIMEs.contains("$packageName/com.alphatype.app.ABCKeyboardService")
    }

    private fun isKeyboardDefault(): Boolean {
        val defaultIME = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.DEFAULT_INPUT_METHOD
        ) ?: return false
        return defaultIME.contains("$packageName/.ABCKeyboardService") ||
               defaultIME.contains("$packageName/com.alphatype.app.ABCKeyboardService")
    }

    private fun markStepDone(card: LinearLayout, circle: View, number: TextView, status: TextView) {
        card.setBackgroundResource(R.drawable.setup_card_done)
        circle.setBackgroundResource(R.drawable.circle_step)
        number.text = "✓"
        number.setTextColor(0xFF0D0D1A.toInt())
        status.text = "✓"
        status.setTextColor(0xFF536DFE.toInt()) // Professional Indigo
    }

    private fun markStepActive(circle: View, number: TextView) {
        circle.setBackgroundResource(R.drawable.circle_step)
        number.setTextColor(0xFF0D0D1A.toInt())
    }

    private fun markStepPending(card: LinearLayout, circle: View, number: TextView, status: TextView) {
        card.setBackgroundResource(R.drawable.setup_card_bg)
        circle.setBackgroundResource(R.drawable.circle_step_inactive)
        number.setTextColor(0x80FFFFFF.toInt())
        status.text = "○"
        status.setTextColor(0x80FFFFFF.toInt())
    }
}
