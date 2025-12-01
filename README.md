# Python Analytics Service (Assignment 2)

This service performs functional data analysis on sales records using Python APIs and functional programming paradigms.

## Structure
*   `src/processor.py`: Logic for revenue calculation, grouping, and statistics.
*   `src/model.py`: Data model.
*   `app.py`: FastAPI application (REST API).
*   `console_runner.py`: Console application to print analysis results.
*   `generate_data.py`: Utility to create mock data.

## Functional Programming Features
The implementation explicitly demonstrates:
*   **Streaming/Iterators**: Using `map()` to transform data lazily.
*   **Aggregation**: Using `functools.reduce()` and `sum()` for reducing streams.
*   **Grouping**: Using `itertools.groupby()` for categorizing data streams.
*   **Lambda Expressions**: Used extensively for key functions in sorting, max, and mapping operations.

## How to Run

### Setup
```bash
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

### 1. Generate Data
```bash
python3 generate_data.py
```

### 2. Run Console Analysis (Meets "Print to Console" Requirement)
```bash
python3 console_runner.py
```

### 3. Start REST API
```bash
uvicorn app:app --reload --port 8000
```

### 4. Run Tests
```bash
pytest tests
```
