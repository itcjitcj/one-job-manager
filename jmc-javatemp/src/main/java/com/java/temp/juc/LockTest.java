package com.java.temp.juc;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockTest {
    //创建一个固定大小的线程池
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        testCountDownLatch();
    }

    //测试 ReentrantLock 可重入锁
    public static void testLock() {
        //创建一个可重入锁
        ReentrantLock lock = new ReentrantLock();
        //创建一个list进行测试
        ArrayList<Long> list = new ArrayList<>();
        //提交任务
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //获取锁
                    lock.lock();
                    //执行任务
                    try {
                        long id = Thread.currentThread().getId();
                        System.out.println(id+"获取锁:"+list);
                        list.add(id);
                        Thread.sleep(5000);
                        System.out.println(id+"释放锁:"+list);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        //释放锁
                        lock.unlock();
                    }
                }
            });
        }
        executorService.shutdown();
    }
    //测试 ReentrantReadWriteLock  读写可重入锁
    public static void testReadWriteLock() {
        //创建一个读写锁
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        //创建一个list进行测试
        ArrayList<Long> list = new ArrayList<>();
        //提交任务
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //获取锁
                    lock.readLock().lock();
                    //执行任务
                    try {
                        long id = Thread.currentThread().getId();
                        System.out.println(id+" read获取锁:"+list);
                        list.add(id);
                        Thread.sleep(5000);
                        System.out.println(id+" read释放锁:"+list);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        //释放锁
                        lock.readLock().unlock();
                    }
                }
            });
        }
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //获取锁
                    lock.writeLock().lock();
                    //执行任务
                    try {
                        long id = Thread.currentThread().getId();
                        System.out.println(id+" write获取锁:"+list);
                        list.add(id);
                        Thread.sleep(5000);
                        System.out.println(id+" write释放锁:"+list);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        //释放锁
                        lock.writeLock().unlock();
                    }
                }
            });
        }
        executorService.shutdown();
    }

    //使用自定义MyDefineLock测试
    public static void testMyDefineLock() {
        //创建一个自定义锁
        MyDefineLock lock = new MyDefineLock();
        //创建一个list进行测试
        ArrayList<Long> list = new ArrayList<>();
        //提交任务
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //获取锁
                    lock.lock();
                    //执行任务
                    try {
                        long id = Thread.currentThread().getId();
                        System.out.println(id+"获取锁:"+list);
                        list.add(id);
                        Thread.sleep(1000);
                        System.out.println(id+"释放锁:"+list);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        //释放锁
                        lock.unlock();
                    }
                }
            });
        }
        executorService.shutdown();
    }

    /**
     * 测试线程信息量Semaphore
     * 在这个示例中，我们使用 Java 的 Semaphore 类来创建了一个信号量，它的初始值为 3。我们创建了三个线程，每个线程首先尝试获取一个许可（减少信号量），之后等待一段时间，最后释放许可（增加信号量）。这里假设线程在执行期间需要占用某种资源或执行某个操作，如果信号量不足，则线程需要等待其他线程释放许可后才能获取许可并继续执行。
     * Semaphore 信号量，可用于控制一定时间内，并发执行的线程数，基于 AQS 实现。可应用于网关限流、资源限制 (如 最大可发起连接数)。由于 release() 释放许可时，未对释放许可数做限制，所以可以通过该方法增加总的许可数量。
     * 获取许可 支持公平和非公平模式，默认非公平模式。公平模式无论是否有许可，都会先判断是否有线程在排队，如果有线程排队，则进入排队，否则尝试获取许可；非公平模式无论许可是否充足，直接尝试获取许可。
     */
    public static void testSemaphore() {
        //创建一个信号量
        Semaphore semaphore = new Semaphore(3);
        //提交任务
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //执行任务
                    long id = Thread.currentThread().getId();
                    try {
                        semaphore.acquire(); // 获取一个许可，减少信号量
                        System.out.println(id+" 获取一个许可，减少信号量");
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }finally {
                        semaphore.release(); // 释放一个许可，增加信号量
                        System.out.println(id+" 释放一个许可，增加信号量");
                    }
                }
            });
        }
        executorService.shutdown();
    }

    /**
     * 测试countdownlatch
     * 每个线程在执行期间都会执行一些任务，并在任务完成后调用 countDown() 方法，将计数器的值减1。在主线程中，我们调用 latch.await() 方法来等待计数器的值归零，表示所有任务都已完成。当计数器归零时，我们打印出一条消息，表示所有任务都已完成。
     */
    public static void testCountDownLatch() {
        //创建一个countdownlatch
        CountDownLatch countDownLatch = new CountDownLatch(3);
        //提交任务
        for (int i = 0; i < 3; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //执行任务
                    long id = Thread.currentThread().getId();
                    try {
                        System.out.println(id+" 执行任务");
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }finally {
                        countDownLatch.countDown(); // 计数器减一
                        System.out.println(id+" 计数器减一");
                    }
                }
            });
        }
        try {
            countDownLatch.await(); // 等待计数器归零
            System.out.println("计数器归零");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executorService.shutdown();
    }
}
