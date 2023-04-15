package com.example.demo.VolatileDemo;

import java.util.concurrent.TimeUnit;

public class OriginalTest {
	// 来源：https://blog.csdn.net/J169YBZ/article/details/119151121
	public static void main(String[] args) {

//		extractedvolatile(shop);
		for (int i = 0; i < 100; i++) {
			Shop shop = new Shop();
			extractedatomicity(shop);
		}
	}

	private static void extractedatomicity(Shop shop) {
		for (int i = 0; i < 200; i++) {
			new Thread(() -> {
				shop.addGoods();
			}).start();
		}

		// 保证所有20个线程都跑完，只剩下2个线程（主线程和GC线程）的时候代码才继续往下走
		// 其中 Thread.yield() 方法表示主线程不执行，让给其他线程执行
		while (Thread.activeCount() > 2) {
			Thread.yield();
		}
		if (shop.a < 200 || shop.a == 201) {
			System.out.println("如果保证了原子性，应该的结果是本来的1+20 = 21，但实际的值：" + shop.a);
		}
	}

	private static void extractedvolatile(Shop shop) {
		new Thread(() -> {
			System.out.println("线程A初始化");
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			shop.saleOne();
			System.out.println("线程A购买商品完成，剩余商品量：" + shop.a);
		}, "线程A").start();

		while (shop.a == 1) {
//			try {
//				TimeUnit.SECONDS.sleep(3);
//				System.out.println("主线程还活着，剩余商品量：" + shop.a);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		System.out.println("主线程，剩余商品量：" + shop.a);
	}
}
