import pytest
from datetime import date
import sys
import os

# Ensure 'src' is importable when running from 'build/analytics-service'
sys.path.append(os.path.join(os.getcwd(), "src"))

from src.processor import SalesStreamProcessor
from src.model import SaleRecord

@pytest.fixture
def processor():
    """Fixture to provide a populated processor instance."""
    p = SalesStreamProcessor()
    p.sales_data = [
        SaleRecord(date.today(), "Laptop", "Electronics", 1000.0, "North"),
        SaleRecord(date.today(), "Mouse", "Electronics", 50.0, "North"),
        SaleRecord(date.today(), "Chair", "Furniture", 200.0, "South"),
        SaleRecord(date.today(), "Desk", "Furniture", 500.0, "West"),
        SaleRecord(date.today(), "Laptop", "Electronics", 1000.0, "East")
    ]
    return p

def test_total_revenue(processor):
    """Verify total revenue calculation (functional reduce)."""
    # 1000 + 50 + 200 + 500 + 1000 = 2750
    assert processor.get_total_revenue() == 2750.0

def test_revenue_by_category(processor):
    """Verify grouping and aggregation by category."""
    rev = processor.get_revenue_by_category()
    # Electronics: 1000 + 50 + 1000 = 2050
    assert rev["Electronics"] == 2050.0
    # Furniture: 200 + 500 = 700
    assert rev["Furniture"] == 700.0

def test_top_performing_sale(processor):
    """Verify max element finding (lambda key)."""
    top = processor.get_top_performing_sale()
    assert top.product == "Laptop"
    assert top.amount == 1000.0

def test_most_popular_product(processor):
    """Verify frequency analysis (most common)."""
    # Laptop appears twice
    assert processor.get_most_popular_product() == "Laptop"

def test_sales_statistics(processor):
    """Verify statistical mean and median."""
    stats = processor.get_sales_statistics()
    # Amounts: [50, 200, 500, 1000, 1000]
    # Mean: 2750 / 5 = 550.0
    # Median: 500.0 (middle value)
    assert stats["mean"] == 550.0
    assert stats["median"] == 500.0

def test_empty_processor():
    """Verify behavior with empty dataset."""
    p = SalesStreamProcessor()
    assert p.get_total_revenue() == 0.0
    assert p.get_top_performing_sale() is None
    assert p.get_most_popular_product() == "Unknown"
    stats = p.get_sales_statistics()
    assert stats["mean"] == 0.0
    assert stats["median"] == 0.0
