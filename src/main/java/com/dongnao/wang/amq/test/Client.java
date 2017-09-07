package com.dongnao.wang.amq.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 *压力测试
 */
public class Client {
	static CountDownLatch cdl = new CountDownLatch(200);
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 200; i++) {
			HttpClient httpClient = new HttpClient();
			new Thread(new Sender(httpClient, cdl)).start();
			//并发
			cdl.countDown();
		}
		Thread.sleep(1000);
	}
}

class Sender implements Runnable {
	public static AtomicInteger count = new AtomicInteger(0);
	//
	HttpClient httpClient;
	CountDownLatch cdl;
	public Sender(HttpClient client, CountDownLatch cdl) {
		httpClient = client;
		this.cdl = cdl;
	}

	public void run() {
		try {
			cdl.await();
			System.out.println(Thread.currentThread().getName() + "---Send message-" + count.getAndIncrement());
			PostMethod post = new PostMethod("http://127.0.0.1:8080/SendMessage");
			post.addParameter("msg", "Hello world!");
			httpClient.executeMethod(post);
			System.out.println(Thread.currentThread().getName() + "---Success-" + count.getAndIncrement());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}