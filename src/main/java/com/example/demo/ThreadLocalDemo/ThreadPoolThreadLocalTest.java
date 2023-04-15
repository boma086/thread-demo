package com.example.demo.ThreadLocalDemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolThreadLocalTest {
    //模拟tomcat线程池
    private static ExecutorService tomcatExecutors = Executors.newFixedThreadPool(10);
    //业务线程池 默认Control中异步任务执行线程池
    private static ExecutorService businessExecutors = Executors.newFixedThreadPool(5);
    //线程上下文环境，模拟在Control这一层，设置环境变量，然后在这里提交一个异步任务，模拟在子线程中，是否可以访问到刚设置的环境变量值。
    private static InheritableThreadLocal<Integer> requestIdThreadLocal = new InheritableThreadLocal<>();
 
    public static void main(String[] args) {
        for (int i = 0; i <10 ; i++) {
            // 模式10个请求，每个请求执行ControlThread的逻辑，其具体实现就是，先输出父线程的名称，
            // 然后设置本地环境变量，并将父线程名称传入到子线程中，在子线程中尝试获取在父线程中的设置的环境变量
            tomcatExecutors.submit(new ControlThread(i));
        }
    }
 
    /**
     * 模拟Control任务
     */
    static class ControlThread implements Runnable {
        private int i;
 
        public ControlThread(int i) {
            this.i = i;
        }
 
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ":" + i);
            requestIdThreadLocal.set(i);
            businessExecutors.submit(new BusinessTask(Thread.currentThread().getName()));
        }
    }
 
    /**
     * 业务任务，主要模拟Control控制层，提交任务到线程池执行
     */
    static class BusinessTask implements Runnable {
        private String parentThreadName;
 
        public BusinessTask(String parentThreadName) {
            this.parentThreadName = parentThreadName;
        }
 
        @Override
        public void run() {
            System.out.println("parentThreadName:"+parentThreadName+":"+requestIdThreadLocal.get());
        }
    }
}


