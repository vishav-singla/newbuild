from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Dict, Optional
from pathlib import Path
import os
import sys

# Add src to path
sys.path.append(os.path.join(os.path.dirname(__file__), "src"))

from src.processor import SalesStreamProcessor
from src.model import SaleRecord

app = FastAPI(title="Analytics Service", version="1.0")

# Global processor instance
processor = SalesStreamProcessor()
DATA_PATH = Path("data/transaction_logs.csv")

@app.on_event("startup")
async def startup_event():
    """Load data on startup if available."""
    if DATA_PATH.exists():
        processor.load_transactions(DATA_PATH)
        print(f"Loaded {len(processor.sales_data)} records.")
    else:
        print("No data file found. Waiting for data generation.")

@app.get("/")
def read_root():
    return {"status": "online", "service": "analytics-service"}

@app.get("/analytics/revenue/total")
def get_total_revenue():
    return {"total_revenue": processor.get_total_revenue()}

@app.get("/analytics/revenue/category")
def get_revenue_by_category():
    return processor.get_revenue_by_category()

@app.get("/analytics/products/top-sale")
def get_top_sale():
    sale = processor.get_top_performing_sale()
    if not sale:
        return {"message": "No sales data"}
    return sale

@app.get("/analytics/products/popular")
def get_popular_product():
    return {"most_popular_product": processor.get_most_popular_product()}

@app.get("/analytics/stats")
def get_stats():
    return processor.get_sales_statistics()

@app.post("/data/reload")
def reload_data():
    if DATA_PATH.exists():
        processor.load_transactions(DATA_PATH)
        return {"message": "Data reloaded", "count": len(processor.sales_data)}
    raise HTTPException(status_code=404, detail="Data file not found")

