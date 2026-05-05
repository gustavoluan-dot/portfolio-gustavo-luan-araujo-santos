package com.fitmeet.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fitmeet.app.databinding.ActivityMainBinding
import java.util.UUID

/**
 * MainActivity — Tela de Lobby do FitMeet
 *
 * Responsável por:
 * - Coletar o nome do usuário e o código da sala
 * - Solicitar permissões de câmera e microfone
 * - Navegar para a TrainingRoomActivity com os dados configurados
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val EXTRA_ROOM_NAME = "extra_room_name"
        const val EXTRA_USER_NAME = "extra_user_name"
        const val EXTRA_IS_TRAINER = "extra_is_trainer"
        private const val PERMISSION_REQUEST_CODE = 101
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gera um código de sala aleatório ao iniciar
        generateRandomRoomCode()

        setupListeners()
    }

    private fun setupListeners() {
        // Botão principal: entrar na sala de treino
        binding.btnEnterRoom.setOnClickListener {
            val userName = binding.etUserName.text?.toString()?.trim() ?: ""
            val roomName = binding.etRoomName.text?.toString()?.trim() ?: ""

            when {
                userName.isEmpty() -> {
                    binding.tilUserName.error = "Informe seu nome"
                    binding.etUserName.requestFocus()
                }
                roomName.isEmpty() -> {
                    binding.tilRoomName.error = "Informe o código da sala"
                    binding.etRoomName.requestFocus()
                }
                else -> {
                    binding.tilUserName.error = null
                    binding.tilRoomName.error = null
                    checkPermissionsAndEnterRoom(userName, roomName)
                }
            }
        }

        // Botão de gerar sala aleatória (ícone no TextInputLayout)
        binding.tilRoomName.setEndIconOnClickListener {
            generateRandomRoomCode()
        }

        // Limpar erros ao digitar
        binding.etUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { binding.tilUserName.error = null }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etRoomName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { binding.tilRoomName.error = null }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    /**
     * Gera um código de sala aleatório no formato "fitmeet-XXXX"
     */
    private fun generateRandomRoomCode() {
        val randomSuffix = UUID.randomUUID().toString().take(8)
        binding.etRoomName.setText("fitmeet-$randomSuffix")
    }

    /**
     * Verifica se as permissões necessárias foram concedidas.
     * Se sim, navega para a sala. Se não, solicita ao usuário.
     */
    private fun checkPermissionsAndEnterRoom(userName: String, roomName: String) {
        val missingPermissions = REQUIRED_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isEmpty()) {
            navigateToTrainingRoom(userName, roomName)
        } else {
            ActivityCompat.requestPermissions(
                this,
                missingPermissions.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
            // Armazena temporariamente para usar após a permissão
            pendingUserName = userName
            pendingRoomName = roomName
        }
    }

    private var pendingUserName: String = ""
    private var pendingRoomName: String = ""

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allGranted) {
                navigateToTrainingRoom(pendingUserName, pendingRoomName)
            } else {
                Toast.makeText(
                    this,
                    "Câmera e microfone são necessários para a videochamada.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Navega para a TrainingRoomActivity passando os dados da sala.
     */
    private fun navigateToTrainingRoom(userName: String, roomName: String) {
        val isTrainer = binding.rbTrainer.isChecked

        val intent = Intent(this, TrainingRoomActivity::class.java).apply {
            putExtra(EXTRA_ROOM_NAME, roomName)
            putExtra(EXTRA_USER_NAME, userName)
            putExtra(EXTRA_IS_TRAINER, isTrainer)
        }
        startActivity(intent)
    }
}
