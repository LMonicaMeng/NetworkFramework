package com.demo.http.download;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {

    public static class MyRunnable implements Runnable {
        private volatile boolean flag = true;

        @Override
        public void run() {
            while (flag && !Thread.interrupted()) {
                try {
                    System.out.println("running");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
//        final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,4,60, TimeUnit.MILLISECONDS,queue);
//        for (int i = 0; i < 14; i++) {
//            final int index = i;
//            threadPoolExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println("index "+index+" queue size = "+queue.size());
//                }
//            });
//        }

        MyRunnable runnable = new MyRunnable();
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runnable.flag = false;
        thread.interrupt();
//        runnable.flag = false;
    }
}
