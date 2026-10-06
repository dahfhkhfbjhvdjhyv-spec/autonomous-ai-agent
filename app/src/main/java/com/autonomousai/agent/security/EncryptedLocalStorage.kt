package com.autonomousai.agent.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.autonomousai.agent.config.AppConfig
import com.autonomousai.agent.service.AgentWorkflowService
import com.autonomousai.agent.service.ModelDownloader
import com.autonomousai.agent.service.TaskExecutionService
import com.autonomousai.agent.security.EncryptedLocalStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AgentUiState(
    val prompt: String = "",
    val modelDownloaded: Boolean = false,
    val taskTypes: List<String> = AppConfig.DEFAULT_TASK_TYPES,
    val logs: List<String> = listOf(
        "[INFO] Agent ready",
        "[INFO] Local model idle",
        "[INFO] Security guard enabled"
    )
)

class AgentViewModel(application: Application) : AndroidViewModel(application) {
    private val engine = AgentWorkflowService()
    private val taskExecutionService = TaskExecutionService()
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
            val downloadUrl = downloader.getDownloadUrl()
            storage.saveCredential("model_url", downloadUrl)
            _uiState.value = _uiState.value.copy(
                modelDownloaded = true,
                logs = buildList {
                    addAll(_uiState.value.logs)
                    add("[INFO] Download started from $downloadUrl")
                    add("[INFO] ${AppConfig.MODEL_NAME} is queued for local installation")
                }
            )
        }
    }

    fun runTask() {
        val prompt = _uiState.value.prompt.trim()
        if (prompt.isBlank()) return

        val task = engine.createWorkflow(prompt)
        val executionLog = taskExecutionService.summarize(task)
        val guardRequired = taskExecutionService.guardForSensitiveAction(task.type)

        _uiState.value = _uiState.value.copy(
            logs = executionLog + _uiState.value.logs,
            taskTypes = engine.detectTaskTypes(prompt)
        )

        if (guardRequired) {
            _uiState.value = _uiState.value.copy(
                logs = listOf("[SECURITY] Final user approval is required before payment or OTP submission") + _uiState.value.logs
            )
        }
    }

    fun runSecurityGuard() {
        _uiState.value = _uiState.value.copy(
            logs = buildList {
                add("[SECURITY] Human-in-the-loop check passed")
                add("[SECURITY] Payment and OTP actions remain blocked until explicit user approval")
                addAll(_uiState.value.logs)
            }
        )
    }
}
