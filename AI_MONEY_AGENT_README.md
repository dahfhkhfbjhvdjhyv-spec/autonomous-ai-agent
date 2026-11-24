# AI MONEY AGENT

A safety-first starter platform for discovering legitimate work opportunities, tracking projects, drafting proposals, and managing human approvals.

## v1 features
- FastAPI backend with SQLite persistence
- Opportunity inbox and status tracking
- Human approval queue for consequential actions
- Activity log and health endpoint
- Docker deployment and GitHub Actions tests

Important: this starter does not automatically apply to jobs, message clients, sign contracts, spend money, or withdraw funds. Those actions require human review and must comply with each platform's terms. No income is guaranteed.

## Quick start
1. Install Python 3.12.
2. Copy .env.example to .env and set a long random APP_SECRET_KEY.
3. Create and activate a virtual environment.
4. Run pip install -r requirements.txt
5. Run PYTHONPATH=backend uvicorn agent_backend.main:app --reload
   - Windows PowerShell: $env:PYTHONPATH="backend"; uvicorn agent_backend.main:app --reload
6. Open http://127.0.0.1:8000/docs

## Docker
Run docker compose up --build. Data persists in the agent_data volume.

## API
- GET /health
- POST and GET /api/opportunities
- POST and GET /api/approvals
- POST /api/approvals/{approval_id}/decision
- GET /api/activity

## Deployment
This v1 prototype has no user authentication yet: keep it on localhost only. Do not expose it to the public internet or deploy it as a public 24/7 service until authentication, HTTPS, rate limiting, backups, and monitoring are added. After hardening, deploy the container to a trusted cloud host for 24/7 availability. Configure APP_SECRET_KEY, DATABASE_URL, and CORS_ORIGINS as host environment variables. Never commit secrets, bot tokens, API keys, cookies, or client credentials.

## Roadmap
1. Connect one approved opportunity source/API.
2. Add AI-assisted opportunity scoring and proposal drafts.
3. Add project delivery workspace and test runner.
4. Add authenticated dashboard, audit controls, and notifications.
5. Add platform integrations only where API access and terms explicitly permit them.

Designed for development on a Windows 11 laptop with Core Ultra 9 275HX, RTX 5070 Ti 12GB, 32GB RAM, and 2TB SSD. The cloud service handles 24/7 operation; local GPU inference is optional.
