package com.example.demo.ForkJoinPoolDemo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class TestForkAndJoinPlus {
    private static final Integer MAX = 400;
    static class WorkTask extends RecursiveTask<Integer> {
        // 子任务开始计算的值
        private Integer startValue;
        // 子任务结束计算的值
        private Integer endValue;
        public WorkTask(Integer startValue , Integer endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }
        @Override
        protected Integer compute() {
            // 如果小于最小分片阈值，则说明要进行相关的数据操作
            // 可以正式进行累加计算了
            if(endValue - startValue < MAX) {
                System.out.println("开始计算的部分：startValue = " + startValue + ";endValue = " + endValue);
                Integer totalValue = 0;
                for(int index = this.startValue ; index <= this.endValue  ; index++) {
                    totalValue += index;
                }
                return totalValue;
            }
            // 否则再进行任务拆分，拆分成两个任务
            else {
                 // 因为采用二分法，拆分，所以进行1/2切分数据量
                WorkTask subTask1 = new WorkTask(startValue, (startValue + endValue) / 2);
                subTask1.fork();//进行拆分机制控制
                WorkTask subTask2 = new WorkTask((startValue + endValue) / 2 + 1 , endValue);
                subTask2.fork();
                return subTask1.join() + subTask2.join();
            }
        }
    }
    public static void main(String[] args) {
        // 这是Fork/Join框架的线程池
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Integer> taskFuture =  pool.submit(new WorkTask(1,1001));
        try {
            Integer result = taskFuture.get();
            System.out.println("result = " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace(System.out);
        }
    }
}