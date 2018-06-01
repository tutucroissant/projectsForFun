package com.jt.test;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestHttpClient {
	
	/**
	 * 1.创建httpClient对象
	 * 2.指定请求的网址
	 * 3.需要指定请求的类型(如果是表单提交post,如果是一般的请求get)
	 * 4.处理返回值数据信息(判断状态码是否为200)
	 * 5.接收返回值数据
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void test01() throws ClientProtocolException, IOException{
		CloseableHttpClient httpClient = 
				HttpClients.createDefault();
		String uri = "https://item.jd.com/4085994.html";
		HttpGet httpGet = new HttpGet(uri);
		HttpPost httpPost = new HttpPost(uri);
		//发起请求获取返回值信息
		CloseableHttpResponse response
		 = httpClient.execute(httpPost);
		//判断状态码是否正确
		if(response.getStatusLine().getStatusCode() == 200){
			String entity = 
			EntityUtils.toString(response.getEntity());
			System.out.println(entity);
		}
	}
}
