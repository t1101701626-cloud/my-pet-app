package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SoundManager(private val context: Context) {
    private var toneGenerator: ToneGenerator? = null
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vm?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    private val scope = CoroutineScope(Dispatchers.Default)
    var isSoundEnabled: Boolean = true

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 75)
        } catch (e: Exception) {
            // ToneGenerator fallback
        }
    }

    fun playGiggle() {
        if (!isSoundEnabled) return
        vibrate(50)
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
                delay(90)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 100)
                delay(110)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 120)
            } catch (_: Exception) {}
        }
    }

    fun playPurr() {
        if (!isSoundEnabled) return
        vibrate(30)
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 100)
                delay(120)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
            } catch (_: Exception) {}
        }
    }

    fun playMunch() {
        if (!isSoundEnabled) return
        vibrate(40)
        scope.launch {
            try {
                repeat(3) {
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE, 70)
                    delay(120)
                }
            } catch (_: Exception) {}
        }
    }

    fun playSplash() {
        if (!isSoundEnabled) return
        vibrate(40)
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 60)
                delay(80)
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 100)
            } catch (_: Exception) {}
        }
    }

    fun playBubblePop() {
        if (!isSoundEnabled) return
        vibrate(25)
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 50)
            } catch (_: Exception) {}
        }
    }

    fun playBrush() {
        if (!isSoundEnabled) return
        vibrate(20)
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE, 40)
            } catch (_: Exception) {}
        }
    }

    fun playReward() {
        if (!isSoundEnabled) return
        vibrate(80)
        scope.launch {
            try {
                val tones = listOf(
                    ToneGenerator.TONE_CDMA_ONE_MIN_BEEP,
                    ToneGenerator.TONE_PROP_BEEP,
                    ToneGenerator.TONE_PROP_ACK,
                    ToneGenerator.TONE_PROP_PROMPT
                )
                tones.forEach { tone ->
                    toneGenerator?.startTone(tone, 100)
                    delay(110)
                }
            } catch (_: Exception) {}
        }
    }

    fun playCoin() {
        if (!isSoundEnabled) return
        vibrate(30)
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 60)
                delay(70)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 90)
            } catch (_: Exception) {}
        }
    }

    fun playSnore() {
        if (!isSoundEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_LOW_PBX_L, 300)
            } catch (_: Exception) {}
        }
    }

    fun playSneeze() {
        if (!isSoundEnabled) return
        vibrate(100)
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_PIP, 80)
                delay(90)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 160)
            } catch (_: Exception) {}
        }
    }

    private fun vibrate(durationMs: Long) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}
