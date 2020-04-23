package org.example.thread;

import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.SneakyThrows;

import java.util.concurrent.*;

/**
 * @ClassName ThreadUtils
 * @Description 线程相关参数
 * @Date 2020/4/23 14:12
 * @Author wangyong
 * @Version 1.0
 **/
public class ThreadUtils {




    public static void main(String[] args) {
        DefaultThreadFactory factory = new DefaultThreadFactory(ThreadUtils.class);
        factory.newThread(() -> System.out.println("abcdefg"));
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ExecutorService pool = new ThreadPoolExecutor(5, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), threadFactory, new ThreadPoolExecutor.AbortPolicy());
        //gracefully shutdown
        pool.execute(()-> System.out.println(Thread.currentThread().getName()));
        pool.execute(()-> System.out.println(Thread.currentThread().getName()));
        pool.execute(()-> System.out.println(Thread.currentThread().getName()));
        pool.execute(()-> System.out.println(Thread.currentThread().getName()));
        pool.execute(()-> System.out.println(Thread.currentThread().getName()));
        pool.execute(()-> System.out.println(Thread.currentThread().getName()));
        pool.execute(()-> System.out.println(Thread.currentThread().getName()));
        pool.shutdown();
    }

}
class FixedPoolDemo{

    public static void main(String[] args) {
        //创建固定大小线程池
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        //创建10个任务给pool
        for (int i = 0; i < 10; i++) {
            Runnable runnable = new TaskDemo();
            //执行任务
            executorService.execute(runnable);
        }
        //关闭线程池,并暂停执行的任务区别于 shutdown (阻止新任务 但不暂停当前已经执行的任务)  一般用shutdow
        //executorService.shutdownNow();
        executorService.shutdown();
        while (!executorService.isTerminated()){
        }
        System.out.println("finished");
    }
}


class CachedPool{
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            Runnable runnable = new TaskDemo();
            pool.execute(runnable);
        }
    }
}

class SingerPoolDemo{

    public static void main(String[] args) {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            Runnable runnable = new TaskDemo();
            pool.execute(runnable);
        }
    }

}

class ScheduleDemo{
    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 10; i++) {
            Runnable runnable = new TaskDemo();
            executorService.execute(runnable);
        }
        //关闭线程池
        executorService.shutdown();
    }
}
class TaskDemo implements Runnable{

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("我是一个task,task任务名称为: "  +  Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(3);
    }

}