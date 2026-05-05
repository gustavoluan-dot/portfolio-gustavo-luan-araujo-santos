# ============================================================
# ProGuard Rules — FitMeet
# ============================================================

# Regras padrão do Android
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

# ─── Jitsi Meet SDK ──────────────────────────────────────────
# Mantém todas as classes do Jitsi Meet SDK
-keep class org.jitsi.meet.** { *; }
-keep class org.jitsi.meet.sdk.** { *; }
-keep interface org.jitsi.meet.sdk.** { *; }

# React Native (usado internamente pelo Jitsi SDK)
-keep class com.facebook.react.** { *; }
-keep class com.facebook.hermes.** { *; }
-keep class com.facebook.jni.** { *; }

# WebRTC (usado pelo Jitsi para vídeo/áudio)
-keep class org.webrtc.** { *; }

# ─── FitMeet App ─────────────────────────────────────────────
-keep class com.fitmeet.app.** { *; }

# ─── AndroidX / Material ─────────────────────────────────────
-keep class androidx.** { *; }
-keep class com.google.android.material.** { *; }

# ─── Kotlin ──────────────────────────────────────────────────
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }

# Suprime avisos de classes não encontradas
-dontwarn org.jitsi.**
-dontwarn com.facebook.**
-dontwarn org.webrtc.**
