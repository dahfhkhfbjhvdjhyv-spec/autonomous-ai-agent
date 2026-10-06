package com.autonomousai.agent.service

class ModelDownloader {
    fun getDownloadUrl(): String {
        return "https://huggingface.co/google/gemma-3-1b-it/resolve/main/gemma-3-1b-it-q4_k_m.gguf"
    }
}
