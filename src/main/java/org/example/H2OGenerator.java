package org.example;

import static java.lang.Thread.sleep;

public class H2OGenerator {
    static void main() {
        H2O h2o = new H2O();


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


class H2O {
    private int hydrogenCount = 0;
    private int oxygenCount = 0;
    private final Object lock = new Object();

    public H2O() {
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        synchronized (lock) {
            while (hydrogenCount == 2) {
                lock.wait();
            }
            hydrogenCount++;
            releaseHydrogen.run();
            if (hydrogenCount == 2 && oxygenCount == 1) {
                hydrogenCount = 0;
                oxygenCount = 0;
                IO.println();
            }
            lock.notifyAll();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        synchronized (lock) {
            while (oxygenCount == 1) {
                lock.wait();
            }
            oxygenCount++;
            releaseOxygen.run();
            if (hydrogenCount == 2 && oxygenCount == 1) {
                hydrogenCount = 0;
                oxygenCount = 0;
                IO.println();
            }
            lock.notifyAll();
        }
    }
}

