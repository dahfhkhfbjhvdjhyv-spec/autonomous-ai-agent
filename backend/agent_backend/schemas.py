from pydantic import BaseModel, Field, HttpUrl
from typing import Literal

class OpportunityCreate(BaseModel):
    title: str = Field(min_length=3, max_length=200)
    source: str = Field(min_length=2, max_length=80)
    url: HttpUrl | None = None
    description: str = Field(default="", max_length=10000)
    budget: str | None = Field(default=None, max_length=100)

class ApprovalCreate(BaseModel):
    action_type: Literal["send_proposal", "message_client", "accept_contract", "spend_money", "publish", "other"]
    summary: str = Field(min_length=5, max_length=1000)
    payload: dict = Field(default_factory=dict)

class ApprovalDecision(BaseModel):
    decision: Literal["approved", "rejected"]
    note: str = Field(default="", max_length=1000)
