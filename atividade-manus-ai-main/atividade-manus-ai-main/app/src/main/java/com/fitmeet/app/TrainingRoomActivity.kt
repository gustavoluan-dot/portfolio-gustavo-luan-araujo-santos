package com.fitmeet.app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fitmeet.app.databinding.ActivityTrainingRoomBinding
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import org.jitsi.meet.sdk.JitsiMeetView
import org.jitsi.meet.sdk.JitsiMeetViewListener
import java.net.URL

/**
 * TrainingRoomActivity — Sala de Treino com Videoconferência
 *
 * Esta Activity é o coração do FitMeet. Ela combina:
 *
 * 1. PARTE SUPERIOR: JitsiMeetView para videoconferência em tempo real
 *    entre professor e aluno, usando o servidor público meet.jit.si.
 *
 * 2. PARTE INFERIOR: Ferramentas de treino:
 *    - Cronômetro digital (Iniciar / Pausar / Zerar)
 *    - Contador de repetições (+ / −) com registro de séries
 *    - Campo de notas do treino
 */
class TrainingRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainingRoomBinding

    // ─── Jitsi Meet ───────────────────────────────────────────────
    private var jitsiMeetView: JitsiMeetView? = null

    // ─── Cronômetro ───────────────────────────────────────────────
    private val timerHandler = Handler(Looper.getMainLooper())
    private var timerSeconds: Long = 0L
    private var isTimerRunning: Boolean = false

    private val timerRunnable = object : Runnable {
        override fun run() {
            timerSeconds++
            updateTimerDisplay()
            timerHandler.postDelayed(this, 1000L)
        }
    }

    // ─── Contador de Repetições ───────────────────────────────────
    private var repsCount: Int = 0
    private var setsCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera os dados passados pela MainActivity
        val roomName = intent.getStringExtra(MainActivity.EXTRA_ROOM_NAME) ?: "fitmeet-sala"
        val userName = intent.getStringExtra(MainActivity.EXTRA_USER_NAME) ?: "Usuário"
        val isTrainer = intent.getBooleanExtra(MainActivity.EXTRA_IS_TRAINER, false)

        // Configura a barra de status da sala
        binding.tvRoomName.text = if (isTrainer) "🏋️ $roomName (Personal)" else "🎯 $roomName"

        // Inicializa os componentes
        setupJitsiMeet(roomName, userName)
        setupTimerControls()
        setupRepsControls()
        setupEndCallButton()
    }

    // ─────────────────────────────────────────────────────────────
    //  JITSI MEET — Configuração e Inicialização
    // ─────────────────────────────────────────────────────────────

    /**
     * Cria e configura o JitsiMeetView, adicionando-o ao container
     * da parte superior da tela. Entra automaticamente na sala.
     */
    private fun setupJitsiMeet(roomName: String, userName: String) {
        jitsiMeetView = JitsiMeetView(this)

        // Adiciona o view ao container FrameLayout
        binding.jitsiContainer.addView(
            jitsiMeetView,
            0, // índice 0 para ficar atrás da barra de status
            android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        // Configura informações do usuário
        val userInfo = JitsiMeetUserInfo().apply {
            displayName = userName
        }

        // Monta as opções da conferência
        val options = JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL("https://meet.jit.si"))
            .setRoom(roomName)
            .setUserInfo(userInfo)
            .setAudioMuted(false)
            .setVideoMuted(false)
            .setFeatureFlag("welcomepage.enabled", false)
            .setFeatureFlag("add-people.enabled", false)
            .setFeatureFlag("invite.enabled", false)
            .setFeatureFlag("live-streaming.enabled", false)
            .setFeatureFlag("recording.enabled", false)
            .setFeatureFlag("video-share.enabled", false)
            .setFeatureFlag("chat.enabled", true)
            .setFeatureFlag("raise-hand.enabled", true)
            .setFeatureFlag("tile-view.enabled", true)
            .setFeatureFlag("toolbox.alwaysVisible", false)
            .build()

        // Listener para eventos da conferência
        jitsiMeetView?.listener = object : JitsiMeetViewListener {
            override fun onConferenceJoined(extraData: MutableMap<String, Any>?) {
                runOnUiThread {
                    binding.viewStatusDot.setBackgroundResource(R.drawable.bg_status_dot_active)
                    binding.tvRoomName.text = binding.tvRoomName.text.toString()
                        .replace("Conectando...", "")
                    Toast.makeText(
                        this@TrainingRoomActivity,
                        "✅ Conectado à sala de treino!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onConferenceTerminated(extraData: MutableMap<String, Any>?) {
                runOnUiThread {
                    binding.viewStatusDot.setBackgroundResource(R.drawable.bg_status_dot)
                    finish()
                }
            }

            override fun onConferenceWillJoin(extraData: MutableMap<String, Any>?) {
                runOnUiThread {
                    binding.tvRoomName.text = "Conectando..."
                }
            }

            override fun onParticipantJoined(extraData: MutableMap<String, Any>?) {
                runOnUiThread {
                    updateParticipantCount()
                }
            }

            override fun onParticipantLeft(extraData: MutableMap<String, Any>?) {
                runOnUiThread {
                    updateParticipantCount()
                }
            }
        }

        // Entra na sala automaticamente
        jitsiMeetView?.join(options)
    }

    private fun updateParticipantCount() {
        // Atualização visual do contador de participantes
        // O número real é gerenciado internamente pelo Jitsi SDK
        binding.tvParticipants.text = "👥 Online"
    }

    // ─────────────────────────────────────────────────────────────
    //  CRONÔMETRO — Lógica completa
    // ─────────────────────────────────────────────────────────────

    /**
     * Configura os listeners dos três botões do cronômetro:
     * INICIAR, PAUSAR e ZERAR.
     */
    private fun setupTimerControls() {
        // Botão INICIAR
        binding.btnTimerStart.setOnClickListener {
            if (!isTimerRunning) {
                startTimer()
            }
        }

        // Botão PAUSAR
        binding.btnTimerPause.setOnClickListener {
            if (isTimerRunning) {
                pauseTimer()
            }
        }

        // Botão ZERAR
        binding.btnTimerReset.setOnClickListener {
            if (timerSeconds > 0) {
                showResetTimerDialog()
            }
        }
    }

    /** Inicia ou retoma o cronômetro */
    private fun startTimer() {
        isTimerRunning = true
        timerHandler.post(timerRunnable)

        // Atualiza estado dos botões
        binding.btnTimerStart.isEnabled = false
        binding.btnTimerPause.isEnabled = true
        binding.btnTimerStart.alpha = 0.5f
        binding.btnTimerPause.alpha = 1.0f
    }

    /** Pausa o cronômetro sem zerar */
    private fun pauseTimer() {
        isTimerRunning = false
        timerHandler.removeCallbacks(timerRunnable)

        // Atualiza estado dos botões
        binding.btnTimerStart.isEnabled = true
        binding.btnTimerPause.isEnabled = false
        binding.btnTimerStart.alpha = 1.0f
        binding.btnTimerPause.alpha = 0.5f
        binding.btnTimerStart.text = "RETOMAR"
    }

    /** Zera o cronômetro e reseta o estado */
    private fun resetTimer() {
        isTimerRunning = false
        timerHandler.removeCallbacks(timerRunnable)
        timerSeconds = 0L
        updateTimerDisplay()

        // Reseta estado dos botões
        binding.btnTimerStart.isEnabled = true
        binding.btnTimerPause.isEnabled = false
        binding.btnTimerStart.alpha = 1.0f
        binding.btnTimerPause.alpha = 0.5f
        binding.btnTimerStart.text = "INICIAR"
    }

    /** Atualiza o display do cronômetro no formato HH:MM:SS */
    private fun updateTimerDisplay() {
        val hours = timerSeconds / 3600
        val minutes = (timerSeconds % 3600) / 60
        val seconds = timerSeconds % 60
        binding.tvTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    /** Exibe diálogo de confirmação antes de zerar */
    private fun showResetTimerDialog() {
        AlertDialog.Builder(this)
            .setTitle("Zerar Cronômetro?")
            .setMessage("O tempo atual (${binding.tvTimer.text}) será perdido.")
            .setPositiveButton("Zerar") { _, _ -> resetTimer() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // ─────────────────────────────────────────────────────────────
    //  CONTADOR DE REPETIÇÕES — Lógica completa
    // ─────────────────────────────────────────────────────────────

    /**
     * Configura os listeners dos botões do contador de repetições:
     * + (aumentar), − (diminuir), Zerar e + Série.
     */
    private fun setupRepsControls() {
        // Botão AUMENTAR (+)
        binding.btnRepsPlus.setOnClickListener {
            repsCount++
            updateRepsDisplay()
            // Feedback visual: pisca o número
            animateRepsDisplay()
        }

        // Botão DIMINUIR (−)
        binding.btnRepsMinus.setOnClickListener {
            if (repsCount > 0) {
                repsCount--
                updateRepsDisplay()
            } else {
                Toast.makeText(this, "Contador já está em zero.", Toast.LENGTH_SHORT).show()
            }
        }

        // Botão ZERAR repetições
        binding.btnRepsReset.setOnClickListener {
            if (repsCount > 0) {
                repsCount = 0
                updateRepsDisplay()
            }
        }

        // Botão + SÉRIE (registra a série atual e zera o contador)
        binding.btnAddSet.setOnClickListener {
            if (repsCount > 0) {
                setsCount++
                val exerciseName = binding.etExerciseName.text?.toString()?.trim()
                    ?.takeIf { it.isNotEmpty() } ?: "Exercício"

                // Adiciona nota automática
                val currentNotes = binding.etNotes.text?.toString() ?: ""
                val setNote = "Série $setsCount — $exerciseName: $repsCount reps\n"
                binding.etNotes.setText(currentNotes + setNote)

                // Feedback ao usuário
                Toast.makeText(
                    this,
                    "✅ Série $setsCount registrada: $repsCount reps de $exerciseName",
                    Toast.LENGTH_SHORT
                ).show()

                // Zera o contador para a próxima série
                repsCount = 0
                updateRepsDisplay()
                updateSetsDisplay()
            } else {
                Toast.makeText(
                    this,
                    "Faça ao menos 1 repetição antes de registrar a série.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /** Atualiza o display do contador de repetições */
    private fun updateRepsDisplay() {
        binding.tvRepsCount.text = repsCount.toString()
    }

    /** Atualiza o display do contador de séries */
    private fun updateSetsDisplay() {
        binding.tvSetsCount.text = setsCount.toString()
    }

    /** Animação simples de escala ao incrementar */
    private fun animateRepsDisplay() {
        binding.tvRepsCount.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(100)
            .withEndAction {
                binding.tvRepsCount.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    // ─────────────────────────────────────────────────────────────
    //  BOTÃO ENCERRAR CHAMADA
    // ─────────────────────────────────────────────────────────────

    private fun setupEndCallButton() {
        binding.fabEndCall.setOnClickListener {
            showEndCallDialog()
        }
    }

    private fun showEndCallDialog() {
        AlertDialog.Builder(this)
            .setTitle("Encerrar Treino?")
            .setMessage("Você irá sair da sala de videoconferência.")
            .setPositiveButton("Encerrar") { _, _ ->
                jitsiMeetView?.leave()
                finish()
            }
            .setNegativeButton("Continuar", null)
            .show()
    }

    // ─────────────────────────────────────────────────────────────
    //  CICLO DE VIDA
    // ─────────────────────────────────────────────────────────────

    override fun onResume() {
        super.onResume()
        JitsiMeetView.onHostResume(this)
    }

    override fun onPause() {
        super.onPause()
        JitsiMeetView.onHostPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Para o cronômetro ao destruir a Activity
        timerHandler.removeCallbacks(timerRunnable)
        // Libera o JitsiMeetView
        jitsiMeetView?.dispose()
        jitsiMeetView = null
        JitsiMeetView.onHostDestroy(this)
    }

    override fun onBackPressed() {
        showEndCallDialog()
    }

    override fun onNewIntent(intent: android.content.Intent?) {
        super.onNewIntent(intent)
        JitsiMeetView.onNewIntent(intent)
    }
}
