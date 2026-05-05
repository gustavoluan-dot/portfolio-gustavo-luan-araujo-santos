package com.fitmeet.app

import android.app.Application
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

/**
 * Classe Application do FitMeet.
 *
 * Responsável por inicializar o Jitsi Meet SDK com as configurações
 * padrão da sala de videoconferência fitness.
 */
class FitMeetApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Configurações padrão do servidor Jitsi Meet
        // Utiliza o servidor público oficial: meet.jit.si
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL("https://meet.jit.si"))
            .setFeatureFlag("welcomepage.enabled", false)
            .setFeatureFlag("add-people.enabled", false)
            .setFeatureFlag("invite.enabled", false)
            .setFeatureFlag("live-streaming.enabled", false)
            .setFeatureFlag("recording.enabled", false)
            .setFeatureFlag("meeting-name.enabled", true)
            .setFeatureFlag("video-share.enabled", false)
            .setFeatureFlag("chat.enabled", true)
            .setFeatureFlag("raise-hand.enabled", true)
            .setFeatureFlag("tile-view.enabled", true)
            .build()

        JitsiMeet.setDefaultConferenceOptions(defaultOptions)
    }
}
