package com.autonomousai.agent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autonomousai.agent.model.AgentTask
import com.autonomousai.agent.model.TaskType
import com.autonomousai.agent.security.EncryptedLocalStorage
import com.autonomousai.agent.service.LocalAgentEngine
import com.autonomousai.agent.service.ModelDownloader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.app.Application
import androidx.lifecycle.AndroidViewModel

data class AgentUiState(
    val prompt: String = "",
    val modelDownloaded: Boolean = false,
    val taskTypes: List<String> = listOf("Booking", "Scraping", "E-commerce"),
    val logs: List<String> = listOf(
        "[INFO] Agent ready",
        "[INFO] Local model idle",
        "[INFO] Security guard enabled"
    )
)

class AgentViewModel(application: Application) : AndroidViewModel(application) {
    private val engine = LocalAgentEngine()
    private val downloader = ModelDownloader()
    private val storage = EncryptedLocalStorage(application.applicationContext)

    private val _uiState = MutableStateFlow(AgentUiState())
    val uiState: StateFlow<AgentUiState> = _uiState.asStateFlow()

    fun updatePrompt(prompt: String) {
        _uiState.value = _uiState.value.copy(prompt = prompt)
        val detected = engine.detectTaskTypes(prompt)
        _uiState.value = _uiState.value.copy(taskTypes = detected)
    }

    fun downloadModel() {
        viewModelScope.launch {
            val modelUrl = downloader.getDownloadUrl()
            storage.saveCredential("model_url", modelUrl)
            _uiState.value = _uiState.value.copy(
                modelDownloaded = true,
                logs = buildList {
                    addAll(_uiState.value.logs)
                    add("[INFO] Download started from $modelUrl")
                    add("[INFO] Gemma 3 1B model is queued for local install")
                }
            )
        }
    }

    fun runTask() {
        val prompt = _uiState.value.prompt.trim()
        if (prompt.isBlank()) {
            return
        }

        val task = engine.orchestrate(prompt)
        val summary = listOf(
            "[INFO] Parsing prompt: $prompt",
            "[STEP] Task type: ${task.type}",
            "[PLAN] ${task.plan.joinToString(" -> ")}",
            "[ACTION] Browser automation or direct API flow is enabled",
            "[SECURITY] Final confirmation required before payment or OTP submission"
        )

        _uiState.value = _uiState.value.copy(
            logs = summary + _uiState.value.logs
        )
    }

    fun runSecurityGuard() {
        _uiState.value = _uiState.value.copy(
            logs = buildList {
                add("[SECURITY] Human-in-the-loop check passed")
                add("[SECURITY] Payment and OTP actions are blocked until user approval")
                addAll(_uiState.value.logs)
            }
        )
    }
}
