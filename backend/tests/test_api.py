import pytest
from fastapi.testclient import TestClient
from agent_backend.main import app

@pytest.fixture
def client():
    with TestClient(app) as test_client:
        yield test_client

def test_health(client):
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "ok"

def test_opportunity_create_and_list(client):
    response = client.post("/api/opportunities", json={
        "title": "Build a landing page",
        "source": "manual",
        "description": "A sample opportunity for testing"
    })
    assert response.status_code == 201
    assert response.json()["title"] == "Build a landing page"
    assert any(x["title"] == "Build a landing page" for x in client.get("/api/opportunities").json())

def test_approval_is_only_a_record(client):
    created = client.post("/api/approvals", json={
        "action_type": "send_proposal",
        "summary": "Review this proposal before sending"
    })
    assert created.status_code == 201
    approval_id = created.json()["id"]
    result = client.post(f"/api/approvals/{approval_id}/decision", json={
        "decision": "approved", "note": "Reviewed by account owner"
    })
    assert result.status_code == 200
    assert "does not execute" in result.json()["warning"]
