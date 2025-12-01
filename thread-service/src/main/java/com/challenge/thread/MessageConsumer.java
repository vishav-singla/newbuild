package com.challenge.thread;

import java.util.Random;

/**
 * Consumer task that reads and processes messages from the {@link BoundedMessageQueue}.
 * <p>
 * This class implements {@link Runnable} to be executed by a thread. It simulates
 * processing time by sleeping for a random interval after consuming a message.
 */
public class MessageConsumer implements Runnable {
    private final BoundedMessageQueue<String> queue;
    private final int messageCount;
    private final Random random = new Random();

    /**
     * Constructs a new MessageConsumer.
     *
     * @param queue        The shared blocking queue to read messages from.
     * @param messageCount The total number of messages this consumer should process.
     */
    public MessageConsumer(BoundedMessageQueue<String> queue, int messageCount) {
        this.queue = queue;
        this.messageCount = messageCount;
    }

    /**
     * Executes the consumer logic.
     * Loops messageCount times, attempting to take items from the queue.
     * Handles {@link InterruptedException} by restoring the interruption status and exiting.
     */
    @Override
    public void run() {
        try {
            for (int i = 0; i < messageCount; i++) {
                String message = queue.consume();
                simulateProcessing();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Consumer " + Thread.currentThread().getName() + " interrupted");
        }
    }

    /**
     * Simulates processing time to make the concurrency behavior observable.
     * Sleep time is random between 0 and 150 milliseconds.
     *
     * @throws InterruptedException if the sleep is interrupted.
     */
    private void simulateProcessing() throws InterruptedException {
        Thread.sleep(random.nextInt(150));
    }
}
