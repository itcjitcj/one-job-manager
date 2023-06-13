package com.java.temp.juc;

import lombok.SneakyThrows;

import java.util.concurrent.*;

public class ExecutorTest {
    public static void main(String[] args) {
        testThreadPoolExecutor();
    }

    //测试固定大小的线程池 newFixedThreadPool
    public static void testFixThreadPool() {
        //创建一个可重用固定线程数的线程池
         ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                     System.out.println(Thread.currentThread().getId());
                     System.out.println("Asynchronous task");
                }
            });
        }
         //优雅关闭线程池,不再接受新的任务，等待所有已经提交的任务执行完毕之后，关闭线程池
         executorService.shutdown();
    }

    //测试缓存的，可动态伸缩的线程池 newCachedThreadPool
    public static void testCacheThreadPool() {
        //创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程
         ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                     System.out.println(Thread.currentThread().getId());
                     System.out.println("Asynchronous task");
                }
            });
        }
         //优雅关闭线程池,不再接受新的任务，等待所有已经提交的任务执行完毕之后，关闭线程池
         executorService.shutdown();
    }

    //测试单线程的线程池 newSingleThreadExecutor
    public static void testSingleThreadExecutor() {
        //创建一个单线程化的线程池
         ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                     System.out.println(Thread.currentThread().getId());
                     System.out.println("Asynchronous task");
                }
            });
        }
         //优雅关闭线程池,不再接受新的任务，等待所有已经提交的任务执行完毕之后，关闭线程池
         executorService.shutdown();
    }

    /**
     * 创建一个缓存的，可动态伸缩的线程池。
     * 可以看出来：核心线程数为0，最大线程数为Integer.MAX_VALUE，如果任务数在某一瞬间暴涨，
     * 这个线程池很可能会把 服务器撑爆。
     * 另外需要注意的是，它们底层都是使用了 ThreadPoolExecutor，只不过帮我们配好了参数
     */
    public static void testScheduledThreadPool() {
        //创建一个可调度的线程池
         ExecutorService executorService = Executors.newScheduledThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                     System.out.println(Thread.currentThread().getId());
                     System.out.println("Asynchronous task");
                }
            });
        }
         //优雅关闭线程池,不再接受新的任务，等待所有已经提交的任务执行完毕之后，关闭线程池
         executorService.shutdown();
    }

    //测试ThreadPoolExecutor创建线程池
    public static void testThreadPoolExecutor() {
        //使用ThreadPoolExecutor创建线程池
        //corePoolSize:核心线程数 当线程池中的线程数量达到corePoolSize时 新提交的任务会被放入workQueue中 而不是创建新的线程  如果调用了prestartAllCoreThreads方法 那么线程池会提前创建并启动所有核心线程
        //maximumPoolSize:最大线程数 当workQueue使用有界队列时 该参数才有效 当workQueue使用无界队列时 该参数无效  一般设置为cpu核心数+1
        //keepAliveTime:线程空闲时间 当线程空闲时间达到keepAliveTime时 线程会退出 直到线程数量=corePoolSize 如果allowCoreThreadTimeOut=true 那么核心线程也会退出 直到线程数量=0 如果allowCoreThreadTimeOut=false 那么核心线程不会退出 但是当线程数量>corePoolSize时 那么多余的线程会退出 直到线程数量=corePoolSize
        //unit:时间单位
        //workQueue:任务队列 有多种实现方式 一般使用阻塞队列 比如ArrayBlockingQueue LinkedBlockingQueue SynchronousQueue 三种 默认使用LinkedBlockingQueue 无界队列 除非系统资源耗尽 否则无界队列不存在任务入队失败的情况 但是会造成内存占用过多的问题 一般需要设置有界队列 比如ArrayBlockingQueue 有界队列 任务入队失败的情况 一般使用有界队列 但是需要设置合理的队列大小 否则可能会造成任务执行缓慢 甚至系统OOM
        //threadFactory:线程工厂 一般使用默认的即可
        //handler:拒绝策略 当任务太多来不及处理 该如何拒绝任务 有多种实现方式 一般使用AbortPolicy CallerRunsPolicy DiscardPolicy DiscardOldestPolicy 四种 默认使用AbortPolicy 会抛出异常 一般需要自定义拒绝策略 一般使用CallerRunsPolicy 该策略会直接在execute方法的调用线程中运行被拒绝的任务 除非线程池已经shutdown 在这种情况下 任务将会被丢弃
         ExecutorService executorService = new ThreadPoolExecutor(1, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < 20; i++) {
            executorService.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                     System.out.println(Thread.currentThread().getId()+"Asynchronous task");
                     Thread.sleep(1000);
                }
            });
        }
         //优雅关闭线程池,不再接受新的任务，等待所有已经提交的任务执行完毕之后，关闭线程池
         executorService.shutdown();
    }

    //测试futureTask
    public static void testFutureTask() {
        //创建一个线程数固定的线程池
         ExecutorService executorService = Executors.newFixedThreadPool(10);
         //创建一个FutureTask
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                 System.out.println("Asynchronous Callable");
                 return 1;
            }
        });
         //提交FutureTask
        executorService.submit(futureTask);
         //获取计算结果
        try {
             Integer result = futureTask.get();
             System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //优雅关闭线程池,不再接受新的任务，等待所有已经提交的任务执行完毕之后，关闭线程池
         executorService.shutdown();
    }

}
