import sys
import os
from pathlib import Path

from src.processor import SalesStreamProcessor

def main():
    print("============================================")
    print("   SALES ANALYTICS CONSOLE RUNNER           ")
    print("============================================")

    data_path = Path("data/transaction_logs.csv")

    processor = SalesStreamProcessor()
    print(f"Loading data from {data_path}...")
    processor.load_transactions(data_path)
    
    print("\n--- ANALYSIS RESULTS ---")
    
    # 1. Total Revenue
    total_revenue = processor.get_total_revenue()
    print(f"\n1. Total Revenue: ${total_revenue:,.2f}")
    
    # 2. Revenue by Category
    print("\n2. Revenue by Category:")
    categories = processor.get_revenue_by_category()
    for cat, rev in categories.items():
        print(f"   - {cat:<15}: ${rev:,.2f}")
        
    # 3. Top Performing Sale
    top_sale = processor.get_top_performing_sale()
    if top_sale:
        print(f"\n3. Top Single Sale: {top_sale.product} (${top_sale.amount:,.2f})")
    else:
        print("\n3. Top Single Sale: N/A")
        
    # 4. Most Popular Product
    popular = processor.get_most_popular_product()
    print(f"\n4. Most Popular Product: {popular}")
    
    # 5. Statistics
    stats = processor.get_sales_statistics()
    print(f"\n5. Statistics:")
    print(f"   - Mean Sale:   ${stats['mean']:,.2f}")
    print(f"   - Median Sale: ${stats['median']:,.2f}")


main()

