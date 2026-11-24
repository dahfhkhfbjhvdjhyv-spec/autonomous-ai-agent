import json
from contextlib import asynccontextmanager
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from agent_backend.config import settings
from agent_backend.db import connect, init_db
from agent_backend.schemas import OpportunityCreate, ApprovalCreate, ApprovalDecision

@asynccontextmanager
async def lifespan(app: FastAPI):
    init_db()
    yield

app = FastAPI(title=settings.app_name, version="0.1.0", lifespan=lifespan)
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.allowed_origins,
    allow_credentials=False,
    allow_methods=["GET", "POST"],
    allow_headers=["Content-Type", "Authorization"],
)

def log_event(kind: str, message: str) -> None:
    with connect() as conn:
        conn.execute("INSERT INTO activity(event_type, message) VALUES (?, ?)", (kind, message))

@app.get("/")
def root():
    return {"name": settings.app_name, "status": "running", "docs": "/docs"}

@app.get("/health")
def health():
    with connect() as conn:
        conn.execute("SELECT 1").fetchone()
    return {"status": "ok", "service": settings.app_name, "version": "0.1.0"}

@app.post("/api/opportunities", status_code=201)
def create_opportunity(item: OpportunityCreate):
    with connect() as conn:
        cur = conn.execute(
            "INSERT INTO opportunities(title, source, url, description, budget) VALUES (?, ?, ?, ?, ?)",
            (item.title, item.source, str(item.url) if item.url else None, item.description, item.budget),
        )
        row = conn.execute("SELECT * FROM opportunities WHERE id = ?", (cur.lastrowid,)).fetchone()
    log_event("opportunity_created", f"Added opportunity #{row['id']}: {row['title']}")
    return dict(row)

@app.get("/api/opportunities")
def list_opportunities(status: str | None = None):
    with connect() as conn:
        if status:
            rows = conn.execute("SELECT * FROM opportunities WHERE status = ? ORDER BY id DESC", (status,)).fetchall()
        else:
            rows = conn.execute("SELECT * FROM opportunities ORDER BY id DESC").fetchall()
    return [dict(row) for row in rows]

@app.post("/api/approvals", status_code=201)
def create_approval(item: ApprovalCreate):
    with connect() as conn:
        cur = conn.execute(
            "INSERT INTO approvals(action_type, summary, payload_json) VALUES (?, ?, ?)",
            (item.action_type, item.summary, json.dumps(item.payload)),
        )
        row = conn.execute("SELECT * FROM approvals WHERE id = ?", (cur.lastrowid,)).fetchone()
    log_event("approval_requested", f"Approval #{row['id']} awaits human review: {row['action_type']}")
    return dict(row)

@app.get("/api/approvals")
def list_approvals(status: str | None = "pending"):
    with connect() as conn:
        if status:
            rows = conn.execute("SELECT * FROM approvals WHERE status = ? ORDER BY id DESC", (status,)).fetchall()
        else:
            rows = conn.execute("SELECT * FROM approvals ORDER BY id DESC").fetchall()
    result = []
    for row in rows:
        item = dict(row)
        item["payload"] = json.loads(item.pop("payload_json"))
        result.append(item)
    return result

@app.post("/api/approvals/{approval_id}/decision")
def decide_approval(approval_id: int, decision: ApprovalDecision):
    with connect() as conn:
        row = conn.execute("SELECT * FROM approvals WHERE id = ?", (approval_id,)).fetchone()
        if row is None:
            raise HTTPException(status_code=404, detail="Approval request not found")
        if row["status"] != "pending":
            raise HTTPException(status_code=409, detail="This approval has already been decided")
        conn.execute(
            "UPDATE approvals SET status = ?, decision_note = ?, decided_at = CURRENT_TIMESTAMP WHERE id = ?",
            (decision.decision, decision.note, approval_id),
        )
    log_event("approval_decided", f"Approval #{approval_id}: {decision.decision}")
    return {"id": approval_id, "status": decision.decision, "note": decision.note,
            "warning": "This records a decision only; it does not execute an external action."}

@app.get("/api/activity")
def list_activity(limit: int = 50):
    limit = max(1, min(limit, 200))
    with connect() as conn:
        rows = conn.execute("SELECT * FROM activity ORDER BY id DESC LIMIT ?", (limit,)).fetchall()
    return [dict(row) for row in rows]
