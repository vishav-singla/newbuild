import csv
import itertools
import functools
from datetime import date
from statistics import mean, median
from collections import Counter
from src.model import SaleRecord

class SalesStreamProcessor:
    """
    Service class for processing sales data using Python's functional programming tools.
    Demonstrates:
      - Streams (Iterators)
      - Data Aggregation (reduce, sum)
      - Grouping (itertools.groupby)
      - Lambda Expressions
    """
    def __init__(self):
        self.sales_data = []

    def load_transactions(self, path):
        """Loads sales data from a CSV file."""
        records = []
        with open(path, mode='r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                records.append(SaleRecord(
                    date=date.fromisoformat(row['Date']),
                    product=row['Product'],
                    category=row['Category'],
                    amount=float(row['Amount']),
                    region=row['Region']
                ))
        self.sales_data = records

    def get_total_revenue(self):
        """
        Calculates total revenue using map() and reduce() to demonstrate stream aggregation.
        Stream: sales_data -> map(amount) -> reduce(sum)
        """
        # 1. Create a stream (iterator) of amounts using map and a lambda
        amounts_stream = map(lambda s: s.amount, self.sales_data)
        
        # 2. Aggregate the stream using reduce (functional summation)
        total = functools.reduce(lambda acc, x: acc + x, amounts_stream, 0.0)
        
        return total

    def get_revenue_by_category(self):
        """
        Groups sales by category using itertools.groupby and aggregates revenue.
        Demonstrates sorting -> grouping -> aggregation pipeline.
        """
        # groupby requires data to be sorted by the key first
        sorted_data = sorted(self.sales_data, key=lambda s: s.category)
        
        # Stream operation: Grouping
        grouped_stream = itertools.groupby(sorted_data, key=lambda s: s.category)
        
        # Stream operation: Aggregation (Summing amounts per group)
        revenue_map = {
            category: sum(map(lambda s: s.amount, group))
            for category, group in grouped_stream
        }
        
        return revenue_map

    def get_top_performing_sale(self):
        """
        Finds the max element in the stream based on a lambda key.
        """
        if not self.sales_data:
            return None
        # Functional aggregation: max() with key lambda
        return max(self.sales_data, key=lambda s: s.amount)

    def get_most_popular_product(self):
        """
        Finds most popular product using mapping and frequency analysis.
        """
        if not self.sales_data:
            return "Unknown"
        
        # Stream: map records to product names
        product_stream = map(lambda s: s.product, self.sales_data)
        
        # Aggregation: Count frequencies
        counts = Counter(product_stream)
        
        return counts.most_common(1)[0][0]
    
    def get_sales_statistics(self):
        """
        Performs statistical analysis using standard functional APIs.
        """
        if not self.sales_data:
            return {"mean": 0.0, "median": 0.0}
        
        # Stream: Convert objects to raw values list for API usage
        amounts = list(map(lambda s: s.amount, self.sales_data))
        
        return {
            "mean": mean(amounts),
            "median": median(amounts)
        }
