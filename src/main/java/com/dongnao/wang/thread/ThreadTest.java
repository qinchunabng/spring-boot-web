package com.dongnao.wang.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSONObject;

/**
 * 1、多线程访问接口
 * 2、利用CountDownLatch实现高并发访问
 */
public class ThreadTest {
	static int visitCount = 2;//并发访问量
	static CountDownLatch cdl = new CountDownLatch(visitCount);
	//并发执行
	public void concurrence() throws Exception {
		for (int i = 0; i < visitCount; i++) {
			HttpClient httpClient = new HttpClient();
			new Thread(new Send(httpClient, cdl)).start();
			cdl.countDown();
		}
		Thread.sleep(3000);
	}
	//
	public static void main(String[] args) throws Exception {
		ThreadTest thr = new ThreadTest();
		thr.concurrence();
	}
}

class Send implements Runnable {
	public static AtomicInteger count = new AtomicInteger(0);
	private final CountDownLatch cdl;
	static HttpClient httpClient;
	//构造方法初始化
	public Send(HttpClient client, CountDownLatch cdl) {
		this.cdl = cdl;
		httpClient = client;
	}
	public void run() {
		try {
			cdl.await();
			String json = httpPostWithJSON();
			//打印输出信息
			System.out.println("线程名称:"+Thread.currentThread().getName());
			System.out.println("AtomicInteger:"+count.getAndIncrement());
			System.out.println("response:"+json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// json方式
	public static String httpPostWithJSON() throws Exception {
		//打印输出信息
		System.out.println("线程名称:"+Thread.currentThread().getName());
		System.out.println("Send message AtomicInteger:"+count.getAndIncrement());
		String url = "http://192.168.6.185:8080/web-app/aliPay/transfer/apply";
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpClient client = HttpClients.createDefault();
		String respContent = null;
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("account", "o0MORwAnbBgkmGmCk5sC-oej0NlU");
		jsonParam.put("amount", "1");
		jsonParam.put("catalog", "3");
		jsonParam.put("confirmCode", "111111");
		jsonParam.put("isInner", "N");
		jsonParam.put("phone", "13683163659");
		jsonParam.put("remark", "test微信提现");
		jsonParam.put("userId", "jV72Pvb91");
		StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpResponse resp = client.execute(httpPost);
		if (resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity he = resp.getEntity();
			respContent = EntityUtils.toString(he, "UTF-8");
		}
		return respContent;
	}
	// 表单方式
	public static String httpPostWithForm() throws Exception {
		//打印输出信息
		System.out.println("线程名称:"+Thread.currentThread().getName());
		System.out.println("Send message AtomicInteger:"+count.getAndIncrement());
		String url = "http://192.168.6.185:8080/web-app/aliPay/transfer/apply";
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpClient client = HttpClients.createDefault();
		String respContent = null;
		List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
		pairList.add(new BasicNameValuePair("account", "o0MORwC4tV8oM_m7pmWaQMIjZTU0"));
		pairList.add(new BasicNameValuePair("amount", "100"));
		pairList.add(new BasicNameValuePair("catalog", "3"));
		pairList.add(new BasicNameValuePair("confirmCode", "000000"));
		pairList.add(new BasicNameValuePair("isInner", "N"));
		pairList.add(new BasicNameValuePair("phone", "15710035377"));
		pairList.add(new BasicNameValuePair("remark", "test微信提现"));
		pairList.add(new BasicNameValuePair("userId", "8FciIkJOo"));
		httpPost.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));
		HttpResponse resp = client.execute(httpPost);
		if (resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity he = resp.getEntity();
			respContent = EntityUtils.toString(he, "UTF-8");
		}
		return respContent;
	}
	
	// 表单提交
	public static int sendHttpClientPost() throws Exception{
		//打印输出信息
		System.out.println("线程名称:"+Thread.currentThread().getName());
		System.out.println("Send message AtomicInteger:"+count.getAndIncrement());
		PostMethod post = new PostMethod("http://192.168.6.185:8080/web-app/aliPay/transfer/apply");
		post.addParameter("account", "o0MORwC4tV8oM_m7pmWaQMIjZTU0");
		post.addParameter("amount", "100");
		post.addParameter("catalog", "3");
		post.addParameter("confirmCode", "000000");
		post.addParameter("isInner", "N");
		post.addParameter("phone", "15710035377");
		post.addParameter("remark", "test微信提现");
		post.addParameter("userId", "8FciIkJOo");
		int executeMethod = httpClient.executeMethod(post);
		if(executeMethod == 200){
			System.out.println("请求成功啦~~~");
		}else{
			System.out.println("请求失败,失败错误码："+executeMethod);
		}
		return executeMethod;
	}
	
}
