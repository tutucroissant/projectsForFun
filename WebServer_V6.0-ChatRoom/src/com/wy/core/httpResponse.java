package com.wy.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import com.wy.common.CommonChar;

/**
 * 在 HttpRequest类中, 获取信息内容，然后将文件发送出去？
 * 	
 */
public class httpResponse {
	private Socket socket = null;
	private File file = null;
	private int RESPONSE_STATUS = CommonChar.StATUS_OK;
	private httpHandler httphandler = null;
	
	public httpResponse(Socket socket){
		this.socket = socket; 
		init();
	}
	public void init() {
		byte[] content = uriAnalyz();//网址分析,返回所读取的内容
		if(content ==null) return;
		else sendToSite(content);//然后发送数据给浏览器
	}
	private byte[] uriAnalyz() {//网址分析,获取网页信息，返回主页面值
		try {
			InputStream input = socket.getInputStream();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(input));
			String line = br.readLine();
			System.out.println("line ="+line);
			if(line == null) return null;
			httphandler = new httpHandler(line);//存储所有第一行的信息,并拆解
			if(httphandler.getUri().startsWith("/Regist"))//如果是注册页面
				{
					System.out.println("处理注册请求……");
					//1为注册，2为登陆
					dataBaseQuery dbq = new dataBaseQuery(httphandler.getUri(),1);
					file = dbq.init();
				}
			else if(httphandler.getUri().startsWith("/Login"))//如果是注册页面
			{
				System.out.println("处理登录请求……");
				dataBaseQuery dbq = new dataBaseQuery(httphandler.getUri(),2);
				file = dbq.init();
			}	
			else if(line.length()>0){
				file = new File(CommonChar.htmlUrl+httphandler.getUri());
				if(!file.exists() || file==null)//如果文件不存在
				{
					RESPONSE_STATUS = CommonChar.StATUS_NOT_FOUND;
					file= new File(CommonChar.htmlUrl+"/404.html");
				}
			}			
				if(file==null) return null;
				BufferedInputStream bis = new BufferedInputStream(
						new FileInputStream(file));
				byte[] content = new byte[(int) file.length()];
				bis.read(content);
				return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private void sendToSite(byte[] content) {//传输文件数据给浏览器
			OutputStream out = null;
			PrintStream ps = null;
		try {
			out = socket.getOutputStream();
			ps = new PrintStream(out);
			ps.println(httphandler.getProtocol()+" "+RESPONSE_STATUS+" "+"OK");
			ps.println("Content-Type:"+
					CommonChar.typesMap.get(httphandler.getSuffix()));
			ps.println("Content-length:"+file.length());
			ps.println();
			ps.write(content);ps.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
