package com.example.demo.ThreadLocalDemo;
public class ThreadLocalTest {
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
 
    public static void main(String[] args) {
        Integer reqId = new Integer(5);
        ThreadLocalTest tt = new ThreadLocalTest();
        tt.setReqId(reqId);
    }
    public static void  setReqId(Integer integer){
        threadLocal.set(integer);
        doBussiness();
    }
 
    private static void doBussiness() {
        System.out.println("首先打印reqId"+threadLocal.get());
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("子线程启动");
                System.out.println("在子线程获取reqId"+threadLocal.get());
            }
        }).start();
    }
}

