package cz.kul.snippets.concurrency.example07_producerConsumer;

import java.util.Random;

public class MainProducerConsumer {

    public static void main(String[] args) {
        ProducerConsumerBuffer buffer = new ProducerConsumerBuffer();
        new Thread(new Producer(buffer)).start();
        new Thread(new Consumer(buffer), "Consumer1").start();
    }

    public static class ProducerConsumerBuffer {

        private Object product;
        private boolean empty = true;

        public synchronized Object consume() throws InterruptedException {
            // if there is nothing to consume, wait
            while (empty) {
                wait();
            }

            // get product and notify other threads, which wait on this instance
            empty = true;
            notifyAll();
            return product;
        }

        public synchronized void produce(Object product) throws InterruptedException {
            // if the product is already here, wait
            while (!empty) {
                wait();
            }

            // save the product here and notify other threads, which wait on this instance
            empty = false;
            this.product = product;
            notifyAll();
        }
    }

    public static class Consumer implements Runnable {
        private ProducerConsumerBuffer buffer;

        public Consumer(ProducerConsumerBuffer place) {
            this.buffer = place;
        }

        public void run() {
            String tName = Thread.currentThread().getName();
            System.out.println(tName + " has started");
            try {
                for (Object product = buffer.consume(); !product.equals("DONE"); product = buffer.consume()) {
                    System.out.format("%s consumed the product: %s%n", tName, product);
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
            System.out.println(tName + " has quit");
        }
    }

    public static class Producer implements Runnable {

        private ProducerConsumerBuffer buffer;

        public Producer(ProducerConsumerBuffer buffer) {
            this.buffer = buffer;
        }

        public void run() {
            try {
                runCore();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }

        private void runCore() throws InterruptedException {
            Random random = new Random();
            for (int i = 0; i < 5; i++) {
                buffer.produce(new Integer(i));
                Thread.sleep(random.nextInt(2000));
            }
            buffer.produce("DONE");
        }
    }
}
