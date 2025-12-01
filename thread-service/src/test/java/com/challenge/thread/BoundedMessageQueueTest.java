package com.challenge.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Unit tests for {@link BoundedMessageQueue}.
 * Covers:
 * - Basic queue operations (produce/consume)
 * - Blocking behavior (wait/notify)
 * - Thread safety and concurrency
 */
class BoundedMessageQueueTest {

    /**
     * Tests that the queue respects its capacity and FIFO order.
     */
    @Test
    void testQueueCapacityAndOrder() throws InterruptedException {
        BoundedMessageQueue<Integer> queue = new BoundedMessageQueue<>(3);
        
        // Add up to capacity
        queue.produce(1);
        queue.produce(2);
        queue.produce(3);
        
        assertEquals(3, queue.size());
        assertEquals(3, queue.getCapacity());

        // Consume and verify order
        assertEquals(1, queue.consume());
        assertEquals(2, queue.consume());
        assertEquals(3, queue.consume());
        assertEquals(0, queue.size());
    }

    /**
     * Tests that produce blocks when the queue is full.
     */
    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testProduceBlockingWhenFull() throws InterruptedException {
        BoundedMessageQueue<Integer> queue = new BoundedMessageQueue<>(1);
        queue.produce(1); // Queue is full now

        Thread producerThread = new Thread(() -> {
            try {
                queue.produce(2); // Should block
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producerThread.start();
        Thread.sleep(100); // Give it time to start and block

        assertEquals(1, queue.size());
        assertEquals(Thread.State.WAITING, producerThread.getState()); // Verify it is waiting

        queue.consume(); // Make space
        Thread.sleep(100); // Allow producer to proceed
        
        assertEquals(1, queue.size()); // Should now contain the second item
        assertEquals(2, queue.consume());
    }

    /**
     * Tests that consume blocks when the queue is empty.
     */
    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testConsumeBlockingWhenEmpty() throws InterruptedException {
        BoundedMessageQueue<Integer> queue = new BoundedMessageQueue<>(1);

        Thread consumerThread = new Thread(() -> {
            try {
                queue.consume(); // Should block
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumerThread.start();
        Thread.sleep(100); // Give it time to start and block

        assertEquals(Thread.State.WAITING, consumerThread.getState()); // Verify it is waiting

        queue.produce(42); // Add item
        Thread.sleep(100); // Allow consumer to proceed
        
        assertEquals(0, queue.size()); // Should be empty after consumption
    }

    /**
     * Tests multiple producers and consumers working concurrently.
     * Verifies that all produced items are consumed exactly once.
     */
    @Test
    void testConcurrentProducersConsumers() throws InterruptedException {
        int capacity = 10;
        BoundedMessageQueue<Integer> queue = new BoundedMessageQueue<>(capacity);
        int producerCount = 5;
        int consumerCount = 5;
        int messagesPerProducer = 100;
        int totalMessages = producerCount * messagesPerProducer;

        ExecutorService service = Executors.newFixedThreadPool(producerCount + consumerCount);
        CountDownLatch latch = new CountDownLatch(producerCount + consumerCount);
        AtomicInteger consumedCount = new AtomicInteger(0);

        // Start Producers
        for (int i = 0; i < producerCount; i++) {
            service.submit(() -> {
                try {
                    for (int j = 0; j < messagesPerProducer; j++) {
                        queue.produce(j);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        // Start Consumers
        for (int i = 0; i < consumerCount; i++) {
            service.submit(() -> {
                try {
                    // Each consumer tries to consume expected share, but in test we just consume until total reached
                    // Note: Simplification for test stability without infinite loop
                    while (consumedCount.get() < totalMessages) {
                        // We might block here if producers are slow, which is fine
                        // But we need a way to break if done. 
                        // For this specific test, we'll assume we can pull enough items.
                        // In a real robust test, we'd use Poison Pill or specialized termination.
                        // Here we rely on the total count check.
                         if (consumedCount.incrementAndGet() <= totalMessages) {
                             queue.consume();
                         } else {
                             consumedCount.decrementAndGet(); // Revert if overshot
                             break;
                         }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        // We can't easily use latch for consumers with the while loop above because they might finish early or block.
        // A better approach for test:
        // Producers run until done. Consumers run until they consume expected count.
        
        // Let's re-structure the consumer logic for the test to be deterministic
        
        service.shutdownNow(); // Clean up previous attempt setup
        
        // RETRY with cleaner logic
        ExecutorService executor = Executors.newFixedThreadPool(producerCount + consumerCount);
        CountDownLatch doneLatch = new CountDownLatch(totalMessages);
        
        // Producers
        for (int i = 0; i < producerCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < messagesPerProducer; j++) {
                        queue.produce(1);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Consumers
        for (int i = 0; i < consumerCount; i++) {
            executor.submit(() -> {
                try {
                    while (doneLatch.getCount() > 0) {
                         queue.consume();
                         doneLatch.countDown();
                    }
                } catch (InterruptedException e) {
                    // Expected during shutdown
                }
            });
        }
        
        boolean finished = doneLatch.await(5, TimeUnit.SECONDS);
        assertTrue(finished, "Did not consume all messages in time");
        assertEquals(0, queue.size(), "Queue should be empty after all consumed");
        
        executor.shutdownNow();
    }

    @Test
    void testNegativeCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new BoundedMessageQueue<>(-1));
        assertThrows(IllegalArgumentException.class, () -> new BoundedMessageQueue<>(0));
    }
}
