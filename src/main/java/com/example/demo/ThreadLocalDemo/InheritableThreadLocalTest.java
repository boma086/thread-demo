package com.example.demo.ThreadLocalDemo;
public class InheritableThreadLocalTest {
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    private static InheritableThreadLocal inheritableThreadLocal = new InheritableThreadLocal();
    public static void main(String[] args) {
        Integer reqId = new Integer(5);
        InheritableThreadLocalTest tt = new InheritableThreadLocalTest();
        tt.setReqId(reqId);
    }
    public static void  setReqId(Integer integer){
        inheritableThreadLocal.set(integer);
        doBussiness();
    }
 
    private static void doBussiness() {
        System.out.println("首先打印reqId"+inheritableThreadLocal.get());
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("子线程启动");
                System.out.println("在子线程获取reqId"+inheritableThreadLocal.get());
            }
        }).start();
    }
}


