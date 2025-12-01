# Project

This repository contains the implementation of two coding assignments, organized as independent microservices.

## Project Structure

```text
build/
├── thread-service/       # Assignment 1: Java Producer-Consumer
│   ├── src/main/java     # Source code (Spring Boot)
│   ├── src/test/java     # JUnit 5 Tests
│   └── README.md         # Instructions & Sample Output
│
└── analytics-service/    # Assignment 2: Python Data Analysis
    ├── src/              # Core Logic (Functional Programming)
    ├── tests/            # Pytest Suite
    ├── data/             # CSV Data
    └── README.md         # Instructions & Sample Output
```

## Assignment 1: Producer-Consumer (Java)

Located in `thread-service/`.
Implements a thread-safe `BoundedMessageQueue` using `wait()` and `notify()` to manage concurrent producers and consumers.

- **Key Concepts**: Thread Synchronization, Blocking Queues, Concurrency.
- **Run**: `cd thread-service && mvn spring-boot:run`

## Assignment 2: Sales Analytics (Python)

Located in `analytics-service/`.
Performs statistical analysis on CSV sales data using Python's functional programming features (`map`, `reduce`, `lambda`, `itertools`).

- **Key Concepts**: Functional Programming, Streams, Aggregation.
- **Run**: `cd analytics-service && python3 console_runner.py`

---
*Refer to the individual `README.md` files in each service directory for detailed setup and testing instructions.*
