package com.example.demo.volatiledemo;
public class Shop{
//	int a = 1;
	volatile int a = 1;

    public void saleOne(){
        this.a = a-1;
    }
    
    public void addGoods(){
        a++;
    }
}

