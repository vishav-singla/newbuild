# Analytics Service (Assignment 2)

This project performs data analysis on sales records using **Python Functional Programming** paradigms. It demonstrates the use of streams, lambda expressions, and data aggregation without relying on heavy external data libraries like Pandas (using standard library `itertools`, `functools` instead).

## Features
- **Functional Processing**: Uses `map`, `reduce`, `filter`, and `sorted` for data transformation.
- **Data Aggregation**: Groups data using `itertools.groupby` and calculates statistics.
- **Lambda Expressions**: Extensive use of lambdas for key functions and transformations.
- **REST API**: Fast API endpoints to expose analysis results.

## Prerequisites
- Python 3.9+

## Setup

1. Create a virtual environment:
   ```bash
   python3 -m venv venv
   source venv/bin/activate
   ```

2. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```

3. Generate mock data:
   ```bash
   python3 generate_data.py
   ```

## Running the Analysis

### Console Mode
Run the console runner to see formatted analysis results in your terminal:

```bash
python3 console_runner.py
```

### API Mode
Start the FastAPI server:

```bash
uvicorn app:app --reload
```
API Docs available at: `http://localhost:8000/docs`

## Running Tests

Run the pytest suite to verify all analysis methods:

```bash
# Add current directory to PYTHONPATH
export PYTHONPATH=$PYTHONPATH:$(pwd)
pytest
```

## Sample Console Output

```text
============================================
   SALES ANALYTICS CONSOLE RUNNER           
============================================
Loading data from data/transaction_logs.csv...

--- ANALYSIS RESULTS ---

1. Total Revenue: $1,245,300.50

2. Revenue by Category:
   - Electronics    : $540,200.00
   - Furniture      : $320,100.50
   - Clothing       : $150,000.00
   - Office         : $235,000.00

3. Top Single Sale: Laptop Pro ($2,500.00)

4. Most Popular Product: Wireless Mouse

5. Statistics:
   - Mean Sale:   $450.25
   - Median Sale: $120.00
```
