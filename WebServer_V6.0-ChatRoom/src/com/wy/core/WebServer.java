package com.wy.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.wy.common.CommonChar;

/**
 * 
 * @author WY
 *	该类为服务器端类。有以下功能：
 *	1.创建ServiceSocket，接受来自浏览器的信息。
 *	2.辨认和处理信息，并且做出响应。
 *	
 */
public class WebServer {
	private ExecutorService threadPool = null;
	private ServerSocket server = null;
	private void start() {
		try {
			threadPool = Executors.newFixedThreadPool(CommonChar.PORT);
			server = new ServerSocket(11111);
			System.out.println("端口"+CommonChar.PORT+"正在等待处理……");
			while(true){//接受网页信息，并且做出处理
				Socket socket = server.accept();
				threadPool.execute(new ClientHandler(socket));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		WebServer ws = new WebServer();
		ws.start();
	}	
}//for class
