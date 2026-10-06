# Autonomous AI Agent App

This repository contains an Android prototype for an autonomous AI agent app based on the provided idea and requirements document.

## Product vision

The app is designed to let a user describe a goal in Bengali or English, after which the agent can:

- orchestrate sub-tasks
- detect intent such as booking, shopping, or scraping
- download a local AI model
- secure user credentials in encrypted local storage
- enforce human-in-the-loop approval before final payment, OTP submission, or critical actions

## Current implementation status

This codebase is a working foundation with the following features:

- Jetpack Compose UI for a simple agent dashboard
- Local task detection and orchestration model
- Model download entry point for a Gemma 3 1B download URL
- Secure encrypted storage using Android Keystore + EncryptedSharedPreferences
- Execution logs and safety guard status
- Project-ready structure for future browser automation and LLM runtime integration

## Tech stack

- Kotlin
- Jetpack Compose
- AndroidX lifecycle and security libraries
- Android Accessibility Service ready for future automation
- Planned integration with llama.cpp / Android local LLM runtime
- Planned browser automation via Playwright or native automation layer

## Potential use cases

- Travel booking and flight comparison
- Food ordering and e-commerce checkout assistance
- Web data collection and scraping
- Multi-step AI-driven browser tasks

## Running the app

1. Open the repo in Android Studio.
2. Sync Gradle.
3. Run the `app` module on emulator or real device.

## Security notes

- Credentials are stored only in encrypted local storage.
- Final payment and OTP actions are intentionally blocked until user permission.
- This is a prototype. Production use should add app sandboxing, stricter permissions, and a review layer for external websites.

## Next extension roadmap

1. Integrate `llama.cpp` or Android LLM runtime for actual Gemma 3 1B execution.
2. Add Accessibility Service and page element detection.
3. Add browser automation for real checkout / booking flows.
4. Add secure credential vault for multiple websites.
5. Add backend orchestration service and cloud fallback mode.

## Example prompts

- "Book a flight to Dhaka from Chittagong next Friday and compare options."
- "Order a pizza from my favorite food app."
- "Collect smartphone prices from five online stores and summarize them."

## Repository

https://github.com/dahfhkhfbjhvdjhyv-spec/autonomous-ai-agent
