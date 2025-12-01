# Thread Service (Assignment 1)

This project implements a classic **Producer-Consumer** pattern using Java concurrency primitives. It demonstrates thread synchronization, blocking queues, and the `wait/notify` mechanism.

## Features
- **BoundedMessageQueue**: A custom thread-safe queue with fixed capacity.
- **Synchronization**: Uses `synchronized` blocks and `wait()`/`notifyAll()` to handle backpressure.
- **Producer/Consumer Workers**: Separate runnable tasks for generating and processing data.
- **Spring Boot**: Uses `CommandLineRunner` to orchestrate the multi-threaded simulation.

## Prerequisites
- Java 17+
- Maven 3.6+

## Running the Application

Run the simulation using Maven:

```bash
mvn spring-boot:run
```

## Running Tests

Execute the JUnit 5 test suite, which covers capacity, blocking behavior, and concurrency:

```bash
mvn test
```

## Sample Output

```text
Starting Producer-Consumer Simulation...
Configuration: Producers=5, Consumers=2, Msgs/Producer=10, QueueCapacity=5
pool-2-thread-1 produced: Msg-18-1
pool-2-thread-4 produced: Msg-21-1
pool-2-thread-4 waiting to produce (Queue Full)
pool-2-thread-7 consumed: Msg-18-1
pool-2-thread-4 produced: Msg-21-2
...
Simulation Completed.
```

