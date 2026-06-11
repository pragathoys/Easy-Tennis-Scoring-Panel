package com.pragathoys.easytennisscoringpanel.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class SpeechManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var ready = false

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

            val result = tts?.setLanguage(Locale.US)

            ready = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }

    fun speak(text: String) {
        if (!ready || text.isEmpty()) return

        tts?.speak(
            text,
            TextToSpeech.QUEUE_ADD,
            null,
            "score_update"
        )
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}