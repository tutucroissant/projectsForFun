package com.wy.core;
/**
 * 	此类是用来完成操作由浏览器发送来的操作
 */
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
	private Socket socket = null;
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	public void run() {
		//1. 服务器响应浏览器，分析并发送html给浏览器
		httpResponse response = new httpResponse(socket);
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			socket = null;
		}
	}
	
}
