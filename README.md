# Implementation

This repository contains the completed assignments, organized into two distinct microservices.

## Repository Structure

*   **`thread-service/`** (Assignment 1)
    *   **Language**: Java (Spring Boot)
    *   **Focus**: Producer-Consumer pattern, Thread Synchronization (`wait`/`notify`), Blocking Queues.
    *   **Key Files**: `BoundedMessageQueue.java`, `MessageProducer.java`, `MessageConsumer.java`.

*   **`analytics-service/`** (Assignment 2)
    *   **Language**: Python
    *   **Focus**: Functional Programming (Streams, Lambda), Data Aggregation, API Analysis.
    *   **Key Files**: `processor.py`, `app.py` (FastAPI), `console_runner.py`.

---

## Assignment 1: Thread Service (Java)

### Description
Implements a thread-safe `BoundedMessageQueue` handling concurrent data transfer between multiple Producers and Consumers.

### How to Run
1.  **Navigate to directory**:
    ```bash
    cd thread-service
    ```
2.  **Run Tests**:
    ```bash
    mvn test
    ```
3.  **Run Application**:
    ```bash
    mvn spring-boot:run
    ```
    *Output will demonstrate the concurrent processing logs in the console.*

---

## Assignment 2: Analytics Service (Python)

### Description
Performs data analysis on sales records using functional programming paradigms (`map`, `reduce`, `groupby`).

### Setup
1.  **Navigate to directory**:
    ```bash
    cd analytics-service
    ```
2.  **Create Environment**:
    ```bash
    python3 -m venv venv
    source venv/bin/activate
    pip install -r requirements.txt
    ```
3.  **Generate Data**:
    ```bash
    python3 generate_data.py
    ```

### How to Run
1.  **Console Output (Requirement)**:
    ```bash
    python3 console_runner.py
    ```
    *Prints total revenue, category breakdown, and statistics to the terminal.*

2.  **Run Unit Tests**:
    ```bash
    export PYTHONPATH=$PYTHONPATH:$(pwd)
    pytest tests
    ```

3.  **Start API (Optional)**:
    ```bash
    uvicorn app:app --reload --port 8000
    ```

