package com.autonomousai.agent.config

object AppConfig {
    const val APP_NAME = "Autonomous AI Agent"
    const val MODEL_NAME = "Gemma 3 1B"
    const val MODEL_DOWNLOAD_URL = "https://huggingface.co/google/gemma-3-1b-it/resolve/main/gemma-3-1b-it-q4_k_m.gguf"
    const val ENCRYPTED_PREFS_NAME = "agent_secure_store"
    const val HUMAN_IN_THE_LOOP_REQUIRED = true
    const val DEFAULT_TASK_TYPES = listOf("Booking", "Scraping", "E-commerce")
}
