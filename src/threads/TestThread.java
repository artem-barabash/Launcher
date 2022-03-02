package threads;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TestThread {
    static class Task extends Thread {

        private final Queue<Integer> queue;

        public Task(Queue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i=0; i<=100; ++i) {
                queue.add(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final BlockingQueue<Integer> queue1 = new ArrayBlockingQueue<Integer>(100);
        final BlockingQueue<Integer> queue2 = new ArrayBlockingQueue<Integer>(100);

        final Thread thread1 = new Task(queue1);
        thread1.start();
        final Thread thread2 = new Task(queue2);
        thread2.start();

        while(true) {
            System.out.println(queue1.take() + queue2.take());
        }

    }
}
