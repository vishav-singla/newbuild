from fastapi.testclient import TestClient
from app import app, processor, DATA_PATH
import pytest
from src.model import SaleRecord
from datetime import date
from pathlib import Path

@pytest.fixture(autouse=True)
def mock_data():
    """Mock the processor data for consistent API testing."""
    processor.sales_data = [
        SaleRecord(date.today(), "Laptop", "Electronics", 1000.0, "North"),
        SaleRecord(date.today(), "Mouse", "Electronics", 50.0, "North"),
        SaleRecord(date.today(), "Chair", "Furniture", 200.0, "South"),
    ]

@pytest.fixture
def client():
    # Workaround for Starlette/HTTPX version conflict
    # Explicitly specify the transport to avoid passing 'app' to httpx.Client
    return TestClient(app=app)

def test_read_root(client):
    response = client.get("/")
    assert response.status_code == 200
    assert response.json() == {"status": "online", "service": "analytics-service"}

def test_get_total_revenue(client):
    response = client.get("/analytics/revenue/total")
    assert response.status_code == 200
    assert response.json() == {"total_revenue": 1250.0}

def test_get_revenue_by_category(client):
    response = client.get("/analytics/revenue/category")
    assert response.status_code == 200
    data = response.json()
    assert data["Electronics"] == 1050.0
    assert data["Furniture"] == 200.0

def test_get_top_sale(client):
    response = client.get("/analytics/products/top-sale")
    assert response.status_code == 200
    data = response.json()
    assert data["product"] == "Laptop"
    assert data["amount"] == 1000.0

def test_get_stats(client):
    response = client.get("/analytics/stats")
    assert response.status_code == 200
    data = response.json()
    assert data["mean"] == pytest.approx(416.66, 0.01)
    assert data["median"] == 200.0

def test_reload_data_no_file(client):
    original_path = Path("data/transaction_logs.csv")
    temp_path = Path("data/transaction_logs.bak")
    
    if original_path.exists():
        original_path.rename(temp_path)
        
    try:
        response = client.post("/data/reload")
        assert response.status_code == 404
        assert response.json()["detail"] == "Data file not found"
    finally:
        if temp_path.exists():
            temp_path.rename(original_path)
