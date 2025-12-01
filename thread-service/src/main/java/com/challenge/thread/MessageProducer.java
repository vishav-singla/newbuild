package com.challenge.thread;

import java.util.Random;

/**
 * Producer task that generates messages and adds them to the {@link BoundedMessageQueue}.
 * <p>
 * This class implements {@link Runnable} to be executed by a thread. It simulates
 * workload by sleeping for a random interval between message production.
 */
public class MessageProducer implements Runnable {
    private final BoundedMessageQueue<String> queue;
    private final int messageCount;
    private final Random random = new Random();

    /**
     * Constructs a new MessageProducer.
     *
     * @param queue        The shared blocking queue to add messages to.
     * @param messageCount The total number of messages this producer should generate.
     */
    public MessageProducer(BoundedMessageQueue<String> queue, int messageCount) {
        this.queue = queue;
        this.messageCount = messageCount;
    }

    /**
     * Executes the producer logic.
     * Iterates from 1 to messageCount, creating messages and attempting to add them to the queue.
     * Handles {@link InterruptedException} by restoring the interruption status and exiting.
     */
    @Override
    public void run() {
        try {
            for (int i = 1; i <= messageCount; i++) {
                String message = "Msg-" + Thread.currentThread().getId() + "-" + i;
                queue.produce(message);
                simulateWork();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Producer " + Thread.currentThread().getName() + " interrupted");
        }
    }

    /**
     * Simulates work latency to make the concurrency behavior observable.
     * Sleep time is random between 0 and 100 milliseconds.
     *
     * @throws InterruptedException if the sleep is interrupted.
     */
    private void simulateWork() throws InterruptedException {
        Thread.sleep(random.nextInt(100));
    }
}
