package com.example.demo.ForkJoinPoolDemo;

import java.util.concurrent.RecursiveTask;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class WorkTaskCallable<T> extends RecursiveTask<T> {
    /**
     * 断言操作控制
     */
    @Getter
    private Predicate<T> predicate;
    /**
     * 执行参数化分割条件
     */
    @Getter
    private T splitParam;
    /**
     * 操作拆分方法操作机制
     */
    @Getter
    private Function<Object,Object[]> splitFunction;
    /**
     * 操作合并方法操作机制
     */
    @Getter
    private BiFunction<Object,Object,T> mergeFunction;
    /**
     * 操作处理机制
     */
    @Setter
    @Getter
    private Function<T,T> processHandler;
    /**
     * 构造器是否进行分割操作
     * @param predicate 判断是否进行下一步分割的条件关系
     * @param splitParam 分割参数
     * @param splitFunction 分割方法
     * @param mergeFunction 合并数据操作
     */
    public WorkTaskCallable(Predicate predicate,T splitParam,Function<Object,Object[]> splitFunction,BiFunction<Object,Object,T> mergeFunction,Function<T,T> processHandler){
        this.predicate = predicate;
        this.splitParam = splitParam;
        this.splitFunction = splitFunction;
        this.mergeFunction = mergeFunction;
        this.processHandler = processHandler;
    }
    /**
     * 实际执行调用操作机制
     * @return
     */
    @Override
    protected T compute() {
        if(predicate.test(splitParam)){
            Object[] result = splitFunction.apply(splitParam);
            WorkTaskCallable workTaskCallable1 = new WorkTaskCallable(predicate,result[0],splitFunction,mergeFunction,processHandler);
            workTaskCallable1.fork();
            WorkTaskCallable workTaskCallable2 = new WorkTaskCallable(predicate,result[1],splitFunction,mergeFunction,processHandler);
            workTaskCallable2.fork();
            return mergeFunction.apply(workTaskCallable1.join(),workTaskCallable2.join());
        }else{
            return processHandler.apply(splitParam);
        }
    }
}