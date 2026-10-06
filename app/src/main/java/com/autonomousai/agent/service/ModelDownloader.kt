package com.autonomousai.agent.service

import com.autonomousai.agent.model.AgentTask
import com.autonomousai.agent.model.TaskType

class LocalAgentEngine {

    fun detectTaskTypes(prompt: String): List<String> {
        val text = prompt.lowercase()
        val detected = mutableListOf<String>()

        if (text.contains("ticket") || text.contains("flight") || text.contains("booking")) {
            detected += "Booking"
        }
        if (text.contains("order") || text.contains("buy") || text.contains("cart")) {
            detected += "E-commerce"
        }
        if (text.contains("scrape") || text.contains("collect") || text.contains("data") || text.contains("search")) {
            detected += "Scraping"
        }

        return if (detected.isEmpty()) listOf("General") else detected
    }

    fun orchestrate(prompt: String): AgentTask {
        val lowerPrompt = prompt.lowercase()
        val taskType = when {
            lowerPrompt.contains("ticket") || lowerPrompt.contains("flight") || lowerPrompt.contains("booking") -> TaskType.BOOKING
            lowerPrompt.contains("order") || lowerPrompt.contains("buy") || lowerPrompt.contains("cart") -> TaskType.ECOMMERCE
            lowerPrompt.contains("scrape") || lowerPrompt.contains("data") || lowerPrompt.contains("collect") -> TaskType.SCRAPING
            else -> TaskType.GENERAL
        }

        val plan = when (taskType) {
            TaskType.BOOKING -> listOf(
                "Identify travel intent",
                "Search available ticket options",
                "Compare fares and routes",
                "Fill booking form",
                "Wait for user confirmation before payment"
            )
            TaskType.ECOMMERCE -> listOf(
                "Parse shopping intent",
                "Find product on retailer site",
                "Add item to cart",
                "Check total and checkout flow",
                "Require explicit user confirmation for final payment"
            )
            TaskType.SCRAPING -> listOf(
                "Identify target website",
                "Extract relevant fields",
                "Normalize the dataset",
                "Save structured results locally",
                "Present clean summary to user"
            )
            TaskType.GENERAL -> listOf(
                "Understand user intent",
                "Create sub-tasks",
                "Execute browser or API workflow",
                "Generate concise final result"
            )
        }

        return AgentTask(taskType, prompt, plan)
    }
}
