package com.autonomousai.agent.service

import com.autonomousai.agent.config.AppConfig
import com.autonomousai.agent.model.AgentTask
import com.autonomousai.agent.model.TaskType

class AgentWorkflowService {
    fun detectTaskTypes(prompt: String): List<String> {
        val text = prompt.lowercase()
        val detected = mutableListOf<String>()

        if (text.contains("ticket") || text.contains("flight") || text.contains("booking") || text.contains("travel")) {
            detected += "Booking"
        }
        if (text.contains("order") || text.contains("buy") || text.contains("cart") || text.contains("shop") || text.contains("purchase")) {
            detected += "E-commerce"
        }
        if (text.contains("scrape") || text.contains("collect") || text.contains("data") || text.contains("search") || text.contains("crawl")) {
            detected += "Scraping"
        }

        return if (detected.isEmpty()) listOf("General") else detected
    }

    fun createWorkflow(prompt: String): AgentTask {
        val lower = prompt.lowercase()
        val type = when {
            lower.contains("ticket") || lower.contains("flight") || lower.contains("booking") || lower.contains("travel") -> TaskType.BOOKING
            lower.contains("order") || lower.contains("buy") || lower.contains("cart") || lower.contains("shop") || lower.contains("purchase") -> TaskType.ECOMMERCE
            lower.contains("scrape") || lower.contains("collect") || lower.contains("data") || lower.contains("search") || lower.contains("crawl") -> TaskType.SCRAPING
            else -> TaskType.GENERAL
        }

        val steps = when (type) {
            TaskType.BOOKING -> listOf(
                "Identify destination and travel dates",
                "Fetch fare options from airline/travel domains",
                "Compare price, time, and policies",
                "Fill booking form and verify details",
                "Stop for user confirmation before payment"
            )
            TaskType.ECOMMERCE -> listOf(
                "Parse the desired product and quantity",
                "Search local/online merchant catalog",
                "Add to cart and validate price",
                "Perform secure checkout workflow",
                "Wait for explicit final authorization"
            )
            TaskType.SCRAPING -> listOf(
                "Locate the target page or dataset source",
                "Extract relevant data points",
                "Normalize and deduplicate results",
                "Save or export structured results",
                "Summarize findings for the user"
            )
            TaskType.GENERAL -> listOf(
                "Resolve user intent",
                "Break the task into sub-steps",
                "Run browser or API workflow",
                "Report result and ask for confirmation if needed"
            )
        }

        return AgentTask(type = type, prompt = prompt, plan = steps)
    }

    fun createModelDownloadMessage(): String {
        return "Downloading ${AppConfig.MODEL_NAME} from Hugging Face ..."
    }
}
