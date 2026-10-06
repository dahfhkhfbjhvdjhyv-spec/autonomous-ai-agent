package com.autonomousai.agent.model

enum class TaskType {
    BOOKING,
    ECOMMERCE,
    SCRAPING,
    GENERAL
}

data class AgentTask(
    val type: TaskType,
    val prompt: String,
    val plan: List<String>
)
