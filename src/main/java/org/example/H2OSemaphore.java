package org.example;

import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class H2OSemaphore {
    static void main() {
        H2OGen h2o = new H2OGen();
        Thread thread1 = new Thread(() -> {
            try {
                while (true) {
                    h2o.hydrogen(() -> IO.print("H"));
                    sleep(300);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                while (true) {
                    h2o.oxygen(() -> IO.print("O"));
                    sleep(300);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread1.start();
        thread2.start();
    }
}

class H2OGen {
    Semaphore semH = new Semaphore(2, true);
    Semaphore semO = new Semaphore(0, true);

    public H2OGen() {
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        semH.acquire();
        releaseHydrogen.run();
        semO.release();
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        semO.acquire(2);
        releaseOxygen.run();
        semH.release(2);
    }
}

