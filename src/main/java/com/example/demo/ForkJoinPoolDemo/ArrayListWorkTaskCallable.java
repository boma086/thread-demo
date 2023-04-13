package com.example.demo.ForkJoinPoolDemo;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.collect.Lists;
import com.example.demo.ForkJoinPoolDemo.WorkTaskCallable;

/**
 * @project-name:wiz-shrding-framework
 * @package-name:com.wiz.sharding.framework.boot.common.thread.forkjoin
 * @author:LiBo/Alex
 * @create-date:2021-09-09 17:26
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class ArrayListWorkTaskCallable extends WorkTaskCallable<List>{
    static Predicate<List> predicateFunction = param->param.size() > 3;
    static Function<List,List[]> splitFunction = (param)-> {
        if(predicateFunction.test(param)){
            return new List[]{param.subList(0,param.size()/ 2),param.subList(param.size()/2,param.size())};
        }else{
            return new List[]{param.subList(0,param.size()+1),Lists.newArrayList()};
        }
    };
    static BiFunction<List,List,List> mergeFunction = (param1,param2)->{
        List datalist = Lists.newArrayList();
        datalist.addAll(param2);
        datalist.addAll(param1);
        return datalist;
    };
    /**
     * 构造器是否进行分割操作
     * @param predicate     判断是否进行下一步分割的条件关系
     * @param splitParam    分割参数
     * @param splitFunction 分割方法
     * @param mergeFunction 合并数据操作
     */
    public ArrayListWorkTaskCallable(Predicate<List> predicate, List splitParam, Function splitFunction, BiFunction mergeFunction,
                                     Function<List,List> processHandler) {
        super(predicate, splitParam, splitFunction, mergeFunction,processHandler);
    }
    public ArrayListWorkTaskCallable(List splitParam, Function splitFunction, BiFunction mergeFunction,
                                     Function<List,List> processHandler) {
        super(predicateFunction, splitParam, splitFunction, mergeFunction,processHandler);
    }
    public ArrayListWorkTaskCallable(Predicate<List> predicate,List splitParam,Function<List,List> processHandler) {
        this(predicate, splitParam, splitFunction, mergeFunction,processHandler);
    }
    public ArrayListWorkTaskCallable(List splitParam,Function<List,List> processHandler) {
        this(predicateFunction, splitParam, splitFunction, mergeFunction,processHandler);
    }
    public static void main(String[] args){
        List dataList = Lists.newArrayList(0,1,2,3,4,5,6,7,8,9);
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        ForkJoinTask<List> forkJoinResult = forkJoinPool.submit(new ArrayListWorkTaskCallable(dataList,param->Lists.newArrayList(param.size())));
        try {
            System.out.println(forkJoinResult.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}