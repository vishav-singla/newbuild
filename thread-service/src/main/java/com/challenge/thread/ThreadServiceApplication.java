package com.challenge.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application for the Thread Service (Producer-Consumer Challenge).
 * <p>
 * This application demonstrates a multi-threaded Producer-Consumer scenario using
 * a custom {@link BoundedMessageQueue}. It sets up a thread pool, launches multiple
 * producer and consumer threads, and waits for their completion.
 */
@SpringBootApplication
public class ThreadServiceApplication implements CommandLineRunner {

    // Application Configuration Constants
    private static final int PRODUCER_COUNT = 5;
    private static final int CONSUMER_COUNT = 2;
    private static final int MESSAGES_PER_PRODUCER = 10;
    private static final int QUEUE_CAPACITY = 5;
    private static final int TOTAL_MESSAGES = PRODUCER_COUNT * MESSAGES_PER_PRODUCER;

    /**
     * Entry point of the Spring Boot application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(ThreadServiceApplication.class, args);
    }

    /**
     * Executes the producer-consumer simulation.
     *
     * @param args Command line arguments passed to the runner.
     * @throws Exception if any error occurs during execution.
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting Producer-Consumer Simulation...");
        System.out.println("Configuration: Producers=" + PRODUCER_COUNT + ", Consumers=" + CONSUMER_COUNT +
                ", Msgs/Producer=" + MESSAGES_PER_PRODUCER + ", QueueCapacity=" + QUEUE_CAPACITY);

        BoundedMessageQueue<String> queue = new BoundedMessageQueue<>(QUEUE_CAPACITY);
        
        // Use a fixed thread pool to manage threads explicitly
        ExecutorService executor = Executors.newFixedThreadPool(PRODUCER_COUNT + CONSUMER_COUNT);

        // CountDownLatch to wait for all tasks to complete (optional, for clean shutdown demonstration)
        // In a real long-running service, we might not wait like this.
        CountDownLatch latch = new CountDownLatch(PRODUCER_COUNT + CONSUMER_COUNT);

        // Start Producers
        for (int i = 0; i < PRODUCER_COUNT; i++) {
            executor.submit(() -> {
                try {
                    new MessageProducer(queue, MESSAGES_PER_PRODUCER).run();
                } finally {
                    latch.countDown();
                }
            });
        }

        // Start Consumers
        // Distribute total messages among consumers. 
        // Note: In a real app, consumers usually run indefinitely. Here we stop them after consuming known total.
        int msgsPerConsumer = TOTAL_MESSAGES / CONSUMER_COUNT;
        int remainingMsgs = TOTAL_MESSAGES % CONSUMER_COUNT;

        for (int i = 0; i < CONSUMER_COUNT; i++) {
            final int msgsToConsume = msgsPerConsumer + (i < remainingMsgs ? 1 : 0);
            executor.submit(() -> {
                try {
                    new MessageConsumer(queue, msgsToConsume).run();
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for completion or timeout
        // Wait longer than expected execution time to allow simulation to finish
        latch.await(30, TimeUnit.SECONDS); 
        
        executor.shutdown();
        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }

        System.out.println("Simulation Completed.");
    }
}
