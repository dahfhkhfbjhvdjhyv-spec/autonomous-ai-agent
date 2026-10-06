package com.autonomousai.agent.service

import com.autonomousai.agent.config.AppConfig

class ModelDownloader {
    fun getDownloadUrl(): String {
        return AppConfig.MODEL_DOWNLOAD_URL
    }
}
