package com.autonomousai.agent.service

import com.autonomousai.agent.model.AgentTask
import com.autonomousai.agent.model.TaskType

class TaskExecutionService {
    fun summarize(task: AgentTask): List<String> {
        return listOf(
            "[INFO] Agent analyzed intent",
            "[TASK] Type: ${task.type}",
            "[PLAN] ${task.plan.joinToString(" -> ")}",
            "[SAFETY] Human confirmation required before payment, OTP, or final order confirmation",
            "[NEXT] Browser automation or API workflow is ready"
        )
    }

    fun guardForSensitiveAction(taskType: TaskType): Boolean {
        return when (taskType) {
            TaskType.BOOKING, TaskType.ECOMMERCE -> true
            TaskType.SCRAPING, TaskType.GENERAL -> false
        }
    }
}
