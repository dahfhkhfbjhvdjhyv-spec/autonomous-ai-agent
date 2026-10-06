package com.autonomousai.agent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.autonomousai.agent.ui.AgentApp
import com.autonomousai.agent.ui.theme.AutonomousAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AutonomousAITheme {
                AgentApp()
            }
        }
    }
}
