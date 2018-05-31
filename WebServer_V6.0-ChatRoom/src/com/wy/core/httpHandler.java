package com.wy.core;

import sun.org.mozilla.javascript.internal.regexp.SubString;

/**
 * 该类对服务器的内容进行解析，然后根据需要返回所需的值
 * method,protocol,uri
 */
public class httpHandler {
	private String line;
	private String method =null;
	private String uri = null;
	private String protocol =null;
	private String suffix;

	public httpHandler(String line) {
		this.line = line;
		String[] firstLine = line.split("\\s");		
		method = firstLine[0];
		uri = firstLine[1];
		protocol = firstLine[2];
		suffix = uri.substring(uri.lastIndexOf(".")+1);
		System.out.println("method ="+method+
				";uri ="+uri+";protocol ="+protocol+";suffix ="+suffix);
	}
	public String getLine() {
		return line;
	}
	public String getMethod() {
		return method;
	}
	public String getUri() {
		return uri;
	}
	public String getProtocol() {
		return protocol;
	}
	public String getSuffix() {
		return suffix;
	}
	
	
}
 